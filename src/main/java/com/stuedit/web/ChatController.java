package com.stuedit.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stuedit.service.AttendService;
import com.stuedit.service.ChatService;
import com.stuedit.service.GradeService;
import com.stuedit.service.StudentService;
import com.stuedit.service.UserService;
import com.stuedit.vo.AttendVO;
import com.stuedit.vo.ChatLogVO;
import com.stuedit.vo.GradeVO;
import com.stuedit.vo.StudentVO;
import com.stuedit.vo.UserVO;

@Controller
@RequestMapping("/chatbot")
public class ChatController {

    private static final Logger logger = LogManager.getLogger(ChatController.class);

    @Autowired private ChatService    chatService;
    @Autowired private UserService    userService;
    @Autowired private StudentService studentService;
    @Autowired private GradeService   gradeService;
    @Autowired private AttendService  attendService;

    /* ──────────────────────────────────────────────────────────
     * 채팅 페이지  GET /chatbot/chat.do
     * ────────────────────────────────────────────────────────── */
    @RequestMapping(value = "/chat.do", method = RequestMethod.GET)
    public String chatPage(ModelMap model) throws Exception {
        String loginId = getLoginId();
        UserVO user = userService.selectUserByLoginId(loginId);

        if (user != null) {
            List<ChatLogVO> history = chatService.getChatHistory(user.getUserId());
            model.addAttribute("chatHistory", history);
            model.addAttribute("currentUser", user);

            // 학생 계정이면 학생 정보도 모델에 추가 (페이지 헤더 표시용)
            if (user.getStudentId() > 0) {
                StudentVO student = studentService.selectStudent(user.getStudentId());
                model.addAttribute("studentInfo", student);
            }
        }

        return "chatbot/chat";
    }

    /* ──────────────────────────────────────────────────────────
     * 메시지 전송  POST /chatbot/send.do  (Ajax)
     * ────────────────────────────────────────────────────────── */
    @RequestMapping(value = "/send.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sendMessage(@RequestParam("message") String message) {

        Map<String, Object> result = new HashMap<>();

        try {
            String loginId = getLoginId();
            UserVO user = userService.selectUserByLoginId(loginId);

            if (user == null) {
                result.put("success", false);
                result.put("response", "로그인이 필요합니다.");
                return result;
            }

            if (message == null || message.trim().isEmpty()) {
                result.put("success", false);
                result.put("response", "메시지를 입력해주세요.");
                return result;
            }

            // 학생 계정: 성적 + 출결 데이터 조회
            StudentVO student    = null;
            List<GradeVO> gradeList = null;
            AttendVO attendStat  = null;

            if (user.getStudentId() > 0) {
                try {
                    student = studentService.selectStudent(user.getStudentId());

                    GradeVO gradeParam = new GradeVO();
                    gradeParam.setStudentId(user.getStudentId());
                    gradeParam.setPageIndex(1);
                    gradeParam.setPageSize(50);
                    gradeList = gradeService.selectGradeList(gradeParam);

                    AttendVO attendParam = new AttendVO();
                    attendParam.setStudentId(user.getStudentId());
                    attendStat = attendService.selectAttendStat(attendParam);
                } catch (Exception e) {
                    logger.warn("학생 데이터 조회 실패 (studentId={}): {}", user.getStudentId(), e.getMessage());
                }
            }

            String response = chatService.sendMessage(message.trim(), user.getUserId(), student, gradeList, attendStat);
            result.put("success", true);
            result.put("response", response);

        } catch (Exception e) {
            logger.error("챗봇 메시지 처리 오류", e);
            result.put("success", false);
            result.put("response", "오류가 발생했습니다: " + e.getMessage());
        }

        return result;
    }

    private String getLoginId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "anonymous";
    }
}
