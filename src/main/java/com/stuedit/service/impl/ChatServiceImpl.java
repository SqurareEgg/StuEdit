package com.stuedit.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.stuedit.dao.ChatLogDAO;
import com.stuedit.service.ChatService;
import com.stuedit.vo.AttendVO;
import com.stuedit.vo.ChatLogVO;
import com.stuedit.vo.GradeVO;
import com.stuedit.vo.StudentVO;

@Service("chatService")
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LogManager.getLogger(ChatServiceImpl.class);

    @Autowired
    private ChatLogDAO chatLogDAO;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String apiUrl;

    @Override
    public String sendMessage(String userMessage, int userId, StudentVO student, List<GradeVO> gradeList, AttendVO attendStat) throws Exception {
        logger.debug("sendMessage - userId: {}", userId);

        String systemPrompt = buildSystemPrompt(student, gradeList, attendStat);
        String botResponse = callGeminiApi(userMessage, systemPrompt);

        ChatLogVO log = new ChatLogVO();
        log.setUserId(userId);
        log.setUserMessage(userMessage);
        log.setBotResponse(botResponse);
        chatLogDAO.insertChatLog(log);

        return botResponse;
    }

    /**
     * 학생 정보 + 성적 + 출결 데이터를 바탕으로 system prompt 생성
     */
    private String buildSystemPrompt(StudentVO student, List<GradeVO> gradeList, AttendVO attendStat) {
        StringBuilder sb = new StringBuilder();
        sb.append("당신은 StuEdit 학생관리시스템의 학사 상담 AI 어시스턴트입니다.\n");
        sb.append("한국어로 친절하고 구체적으로 답변하세요.\n\n");

        if (student == null) {
            sb.append("현재 관리자가 접속 중입니다.\n");
            sb.append("학생 관리, 성적 처리, 출결 관리, 공지사항, 학사 일정 등 학사 행정 전반에 대해 도움을 드립니다.");
            return sb.toString();
        }

        // 학생 기본 정보
        sb.append("[학생 기본 정보]\n");
        sb.append("- 이름: ").append(student.getStudentName()).append("\n");
        sb.append("- 학번: ").append(student.getStudentNum()).append("\n");
        sb.append("- 학과: ").append(student.getDeptName()).append("\n");
        sb.append("- 학년: ").append(student.getGrade()).append("학년\n\n");

        // 성적 내역
        if (gradeList != null && !gradeList.isEmpty()) {
            sb.append("[성적 내역]\n");
            sb.append(String.format("%-10s %-20s %6s %4s %6s\n", "학기", "과목명", "점수", "학점", "이수학점"));
            sb.append("-".repeat(52)).append("\n");

            double totalScore = 0;
            int totalCredit = 0;

            for (GradeVO g : gradeList) {
                sb.append(String.format("%-10s %-20s %5.1f점  %-4s %d학점\n",
                        g.getSemester(),
                        g.getSubjectName(),
                        g.getScore(),
                        g.getGradePoint() != null ? g.getGradePoint() : "-",
                        g.getCredit()));
                totalScore += g.getScore();
                totalCredit += g.getCredit();
            }

            double avgScore = totalScore / gradeList.size();
            double gpa = scoreToGpa(avgScore);

            sb.append("\n[성적 통계]\n");
            sb.append(String.format("- 총 이수학점: %d학점\n", totalCredit));
            sb.append(String.format("- 평균 점수: %.1f점\n", avgScore));
            sb.append(String.format("- 예상 GPA: %.2f / 4.5\n", gpa));
        } else {
            sb.append("[성적 내역]\n성적 데이터가 없습니다.\n");
        }

        // 출결 현황
        sb.append("\n[출결 현황]\n");
        if (attendStat != null && attendStat.getTotalCount() > 0) {
            int total   = attendStat.getTotalCount();
            int present = attendStat.getPresentCount();
            int late    = attendStat.getLateCount();
            int absent  = attendStat.getAbsentCount();
            int official= attendStat.getOfficialCount();
            double rate = attendStat.getAttendRate();

            sb.append(String.format("- 전체 수업일수: %d일\n", total));
            sb.append(String.format("- 출석: %d일 / 지각: %d일 / 결석: %d일 / 공결: %d일\n",
                    present, late, absent, official));
            sb.append(String.format("- 출석률: %.1f%%\n", rate));

            // 결석 위험도 판정
            // 지각 3회 = 결석 1회 환산, 공결은 출석으로 인정
            // 일반적 기준: 전체의 1/3 초과 결석 시 F 또는 수강취소
            double effectiveAbsent = absent + (late / 3.0);
            double maxAllowed = total / 3.0;
            double remaining = maxAllowed - effectiveAbsent;

            sb.append("\n[출결 위험도 분석]\n");
            sb.append(String.format("- 환산 결석(결석 + 지각÷3): %.1f회\n", effectiveAbsent));
            sb.append(String.format("- 최대 허용 결석(전체 1/3): %.1f회\n", maxAllowed));

            if (effectiveAbsent >= maxAllowed) {
                sb.append("⚠️ 위험: 이미 최대 허용 결석 횟수를 초과했습니다! 수강취소 또는 F학점 위험이 있습니다.\n");
            } else if (remaining <= 2) {
                sb.append(String.format("⚠️ 경고: 추가 결석 가능 횟수가 %.0f회 밖에 남지 않았습니다. 매우 주의가 필요합니다.\n", remaining));
            } else if (rate < 75.0) {
                sb.append(String.format("주의: 출석률이 75%% 미만(%.1f%%)입니다. 출석에 더 신경 써야 합니다.\n", rate));
            } else {
                sb.append(String.format("양호: 출석률 %.1f%%, 추가 결석 가능 횟수 약 %.0f회 남음.\n", rate, remaining));
            }
        } else {
            sb.append("출결 데이터가 없습니다.\n");
        }

        sb.append("\n위 학생의 실제 성적 및 출결 데이터를 바탕으로 학업 상담을 진행하세요.\n");
        sb.append("성적 분석, 취약 과목 파악, 학점 향상 전략, 출결 위험 경고, 졸업 요건 등에 대해 구체적이고 맞춤형 조언을 제공하세요.\n");
        sb.append("학생이 묻지 않은 정보는 먼저 언급하지 말고, 질문에 집중하여 답변하세요.");

        return sb.toString();
    }

    /** 점수 → GPA 4.5 환산 */
    private double scoreToGpa(double score) {
        if (score >= 95) return 4.5;
        if (score >= 90) return 4.0;
        if (score >= 85) return 3.5;
        if (score >= 80) return 3.0;
        if (score >= 75) return 2.5;
        if (score >= 70) return 2.0;
        if (score >= 65) return 1.5;
        if (score >= 60) return 1.0;
        return 0.0;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String callGeminiApi(String message, String systemPrompt) {
        if (apiKey == null || apiKey.trim().isEmpty() || "YOUR_GEMINI_API_KEY".equals(apiKey.trim())) {
            logger.warn("Gemini API 키가 설정되지 않았습니다.");
            return "AI 챗봇 서비스를 사용하려면 Gemini API 키를 설정해야 합니다. gemini.properties의 gemini.api.key를 입력하세요.";
        }

        String url = apiUrl + "?key=" + apiKey;

        // systemInstruction + contents 구성
        Map<String, Object> requestBody = new HashMap<>();

        // system_instruction
        Map<String, Object> sysInstr = new HashMap<>();
        List<Map<String, Object>> sysParts = new ArrayList<>();
        Map<String, Object> sysPart = new HashMap<>();
        sysPart.put("text", systemPrompt);
        sysParts.add(sysPart);
        sysInstr.put("parts", sysParts);
        requestBody.put("systemInstruction", sysInstr);

        // contents (user message)
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, Object>> parts = new ArrayList<>();
        Map<String, Object> part = new HashMap<>();
        part.put("text", message);
        parts.add(part);
        content.put("parts", parts);
        contents.add(content);
        requestBody.put("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
                Map body = response.getBody();

                if (body == null) {
                    logger.error("Gemini API 응답 body가 null입니다.");
                    return "응답을 받을 수 없습니다.";
                }

                List candidates = (List) body.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map contentMap = (Map) candidate.get("content");
                    if (contentMap != null) {
                        List partsList = (List) contentMap.get("parts");
                        if (partsList != null && !partsList.isEmpty()) {
                            Map firstPart = (Map) partsList.get(0);
                            String text = (String) firstPart.get("text");
                            if (text != null) return text.trim();
                        }
                    }
                }

                if (body.get("error") != null) {
                    Map error = (Map) body.get("error");
                    String errMsg = (String) error.get("message");
                    logger.error("Gemini API 오류: {}", errMsg);
                    return "AI 응답 오류: " + errMsg;
                }

            } catch (HttpServerErrorException e) {
                // 503 Service Unavailable: 서버 과부하 시 재시도
                if (e.getStatusCode().value() == 503 && attempt < maxRetries) {
                    long delay = attempt * 2000L;
                    logger.warn("Gemini API 503 오류, {}ms 후 재시도 ({}/{})", delay, attempt, maxRetries);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return "죄송합니다. 요청이 중단되었습니다.";
                    }
                    continue;
                }
                logger.error("Gemini API 호출 실패 ({}회 시도)", attempt, e);
                return "죄송합니다. 현재 AI 서비스에 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.";
            } catch (Exception e) {
                logger.error("Gemini API 호출 실패", e);
                return "죄송합니다. 응답을 생성하는 중 오류가 발생했습니다: " + e.getMessage();
            }
        }

        return "응답을 받을 수 없습니다.";
    }

    @Override
    public List<ChatLogVO> getChatHistory(int userId) throws Exception {
        logger.debug("getChatHistory - userId: {}", userId);
        return chatLogDAO.selectChatHistory(userId);
    }
}
