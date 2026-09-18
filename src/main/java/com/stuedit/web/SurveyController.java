package com.stuedit.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stuedit.service.SurveyService;
import com.stuedit.service.UserService;
import com.stuedit.vo.SurveyAnswerVO;
import com.stuedit.vo.SurveyQuestionVO;
import com.stuedit.vo.SurveyVO;
import com.stuedit.vo.UserVO;

/**
 * 설문조사 컨트롤러
 * - 목록/참여/결과: 로그인 사용자 전체
 * - 등록/삭제: ROLE_ADMIN 전용 (코드 내 권한 체크)
 */
@Controller
@RequestMapping("/survey")
public class SurveyController {

    private static final Logger logger = LogManager.getLogger(SurveyController.class);

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private UserService userService;

    /* ===================================================================
     * 설문조사 목록
     * GET /survey/list.do
     * =================================================================== */
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String selectSurveyList(
            @ModelAttribute("searchVO") SurveyVO searchVO,
            ModelMap model) throws Exception {

        if (searchVO.getPageSize()  == 0) searchVO.setPageSize(10);
        if (searchVO.getPageIndex() == 0) searchVO.setPageIndex(1);

        int totalCount = surveyService.selectSurveyCount(searchVO);
        List<SurveyVO> surveyList = surveyService.selectSurveyList(searchVO);

        int pageSize   = searchVO.getPageSize();
        int pageIndex  = searchVO.getPageIndex();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int blockSize  = 10;
        int startPage  = ((pageIndex - 1) / blockSize) * blockSize + 1;
        int endPage    = Math.min(startPage + blockSize - 1, totalPages);

        // 현재 로그인 사용자 참여 여부 조회
        int currentUserId = getCurrentUserId();
        model.addAttribute("currentUserId", currentUserId);

        model.addAttribute("surveyList",  surveyList);
        model.addAttribute("totalCount",  totalCount);
        model.addAttribute("totalPages",  totalPages);
        model.addAttribute("startPage",   startPage);
        model.addAttribute("endPage",     endPage);

        return "survey/list";
    }

    /* ===================================================================
     * 설문 참여 폼
     * GET /survey/participate.do?surveyId=
     * 이미 참여한 경우 결과 페이지로 redirect
     * =================================================================== */
    @RequestMapping(value = "/participate.do", method = RequestMethod.GET)
    public String participateForm(
            @RequestParam("surveyId") int surveyId,
            ModelMap model,
            RedirectAttributes redirectAttr) throws Exception {

        int userId = getCurrentUserId();
        if (userId <= 0) {
            redirectAttr.addFlashAttribute("errorMsg", "로그인이 필요합니다.");
            return "redirect:/survey/list.do";
        }

        // 이미 참여한 경우 결과 페이지로 이동
        int participated = surveyService.checkParticipation(surveyId, userId);
        if (participated > 0) {
            redirectAttr.addFlashAttribute("resultMsg", "이미 참여한 설문입니다. 결과를 확인합니다.");
            return "redirect:/survey/result.do?surveyId=" + surveyId;
        }

        SurveyVO survey = surveyService.selectSurveyDetail(surveyId);

        // 종료된 설문인 경우
        if ("종료".equals(survey.getStatus())) {
            redirectAttr.addFlashAttribute("errorMsg", "종료된 설문입니다.");
            return "redirect:/survey/list.do";
        }

        model.addAttribute("survey", survey);
        return "survey/participate";
    }

    /* ===================================================================
     * 설문 참여 처리
     * POST /survey/participate.do
     * =================================================================== */
    @RequestMapping(value = "/participate.do", method = RequestMethod.POST)
    public String participateSubmit(
            HttpServletRequest request,
            @RequestParam("surveyId") int surveyId,
            RedirectAttributes redirectAttr) throws Exception {

        int userId = getCurrentUserId();
        if (userId <= 0) {
            redirectAttr.addFlashAttribute("errorMsg", "로그인이 필요합니다.");
            return "redirect:/survey/list.do";
        }

        try {
            // 설문 상세(문항 목록) 조회
            SurveyVO survey = surveyService.selectSurveyDetail(surveyId);
            List<SurveyAnswerVO> answers = new ArrayList<SurveyAnswerVO>();

            if (survey.getQuestions() != null) {
                for (SurveyQuestionVO q : survey.getQuestions()) {
                    int questionId = q.getQuestionId();
                    String questionType = q.getQuestionType();
                    String paramName = "answer_" + questionId;

                    if ("text".equals(questionType)) {
                        // 주관식: 텍스트 답변
                        String answerText = request.getParameter(paramName);
                        if (answerText != null && !answerText.trim().isEmpty()) {
                            SurveyAnswerVO answer = new SurveyAnswerVO();
                            answer.setQuestionId(questionId);
                            answer.setAnswerText(answerText.trim());
                            answers.add(answer);
                        }
                    } else if ("single".equals(questionType)) {
                        // 단일 선택
                        String optionIdStr = request.getParameter(paramName);
                        if (optionIdStr != null && !optionIdStr.trim().isEmpty()) {
                            try {
                                SurveyAnswerVO answer = new SurveyAnswerVO();
                                answer.setQuestionId(questionId);
                                answer.setOptionId(Integer.parseInt(optionIdStr.trim()));
                                answers.add(answer);
                            } catch (NumberFormatException e) {
                                logger.warn("optionId 파싱 실패: {}", optionIdStr);
                            }
                        }
                    } else if ("multi".equals(questionType)) {
                        // 다중 선택
                        String[] optionIds = request.getParameterValues(paramName);
                        if (optionIds != null) {
                            for (String optionIdStr : optionIds) {
                                if (optionIdStr != null && !optionIdStr.trim().isEmpty()) {
                                    try {
                                        SurveyAnswerVO answer = new SurveyAnswerVO();
                                        answer.setQuestionId(questionId);
                                        answer.setOptionId(Integer.parseInt(optionIdStr.trim()));
                                        answers.add(answer);
                                    } catch (NumberFormatException e) {
                                        logger.warn("optionId 파싱 실패: {}", optionIdStr);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            surveyService.participate(surveyId, userId, answers);
            redirectAttr.addFlashAttribute("resultMsg", "설문에 참여해 주셔서 감사합니다.");
            return "redirect:/survey/result.do?surveyId=" + surveyId;

        } catch (RuntimeException e) {
            logger.error("설문 참여 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/survey/participate.do?surveyId=" + surveyId;
        }
    }

    /* ===================================================================
     * 설문 결과 조회
     * GET /survey/result.do?surveyId=
     * =================================================================== */
    @RequestMapping(value = "/result.do", method = RequestMethod.GET)
    public String selectSurveyResult(
            @RequestParam("surveyId") int surveyId,
            ModelMap model) throws Exception {

        SurveyVO survey = surveyService.selectSurveyResult(surveyId);
        int totalResponseCount = 0;

        // 총 참여자 수 조회 (DAO를 직접 호출하는 대신 서비스를 통해 처리)
        // SurveyServiceImpl의 selectSurveyResult에서 survey에 담겨 있지 않으므로
        // checkParticipation과 별도 집계 활용
        // 총 참여자 수는 별도 서비스 메서드가 없으므로 DAO를 직접 활용하는 대신
        // 결과를 통해 각 문항의 응답 수의 최대값으로 근사하거나
        // 서비스에 totalResponseCount를 추가하는 방식 사용
        // 여기서는 model에 직접 계산해서 추가
        if (survey.getQuestions() != null && !survey.getQuestions().isEmpty()) {
            SurveyQuestionVO firstQ = survey.getQuestions().get(0);
            if (firstQ.getOptions() != null && !firstQ.getOptions().isEmpty()) {
                for (SurveyOptionVO o : firstQ.getOptions()) {
                    totalResponseCount += o.getResponseCount();
                }
            }
        }

        model.addAttribute("survey", survey);
        model.addAttribute("totalResponseCount", totalResponseCount);
        return "survey/result";
    }

    /* ===================================================================
     * 설문 등록 폼 (ROLE_ADMIN)
     * GET /survey/insertForm.do
     * =================================================================== */
    @RequestMapping(value = "/insertForm.do", method = RequestMethod.GET)
    public String insertForm(ModelMap model, RedirectAttributes redirectAttr) {
        if (!isAdmin()) {
            redirectAttr.addFlashAttribute("errorMsg", "관리자만 접근 가능합니다.");
            return "redirect:/survey/list.do";
        }
        model.addAttribute("survey", new SurveyVO());
        return "survey/insertForm";
    }

    /* ===================================================================
     * 설문 등록 처리 (ROLE_ADMIN)
     * POST /survey/insert.do
     * 문항/선택지: questions[0].questionText, questions[0].questionType,
     *              questions[0].options[0].optionText 형식 파라미터 처리
     * =================================================================== */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    public String insertSurvey(
            HttpServletRequest request,
            @ModelAttribute SurveyVO surveyVO,
            RedirectAttributes redirectAttr) throws Exception {

        if (!isAdmin()) {
            redirectAttr.addFlashAttribute("errorMsg", "관리자만 접근 가능합니다.");
            return "redirect:/survey/list.do";
        }

        try {
            // 문항 파싱: questions[0].questionText, questions[0].questionType
            List<SurveyQuestionVO> questions = new ArrayList<SurveyQuestionVO>();
            int qIdx = 0;
            while (true) {
                String questionText = request.getParameter("questions[" + qIdx + "].questionText");
                if (questionText == null || questionText.trim().isEmpty()) break;

                String questionType = request.getParameter("questions[" + qIdx + "].questionType");
                if (questionType == null || questionType.trim().isEmpty()) questionType = "single";

                SurveyQuestionVO question = new SurveyQuestionVO();
                question.setQuestionText(questionText.trim());
                question.setQuestionType(questionType.trim());
                question.setOrderNum(qIdx + 1);

                // 선택지 파싱
                if (!"text".equals(questionType.trim())) {
                    List<SurveyOptionVO> options = new ArrayList<SurveyOptionVO>();
                    int oIdx = 0;
                    while (true) {
                        String optionText = request.getParameter(
                            "questions[" + qIdx + "].options[" + oIdx + "].optionText");
                        if (optionText == null || optionText.trim().isEmpty()) break;
                        SurveyOptionVO option = new SurveyOptionVO();
                        option.setOptionText(optionText.trim());
                        option.setOrderNum(oIdx + 1);
                        options.add(option);
                        oIdx++;
                    }
                    question.setOptions(options);
                }

                questions.add(question);
                qIdx++;
            }

            surveyVO.setQuestions(questions);
            surveyService.insertSurvey(surveyVO);
            redirectAttr.addFlashAttribute("resultMsg", "설문조사가 등록되었습니다.");
            return "redirect:/survey/list.do";

        } catch (Exception e) {
            logger.error("설문조사 등록 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/survey/insertForm.do";
        }
    }

    /* ===================================================================
     * 설문 삭제 (ROLE_ADMIN, 소프트삭제)
     * GET /survey/delete.do?surveyId=
     * =================================================================== */
    @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
    public String deleteSurvey(
            @RequestParam("surveyId") int surveyId,
            RedirectAttributes redirectAttr) throws Exception {

        if (!isAdmin()) {
            redirectAttr.addFlashAttribute("errorMsg", "관리자만 접근 가능합니다.");
            return "redirect:/survey/list.do";
        }

        try {
            surveyService.deleteSurvey(surveyId);
            redirectAttr.addFlashAttribute("resultMsg", "설문조사가 삭제되었습니다.");
        } catch (Exception e) {
            logger.error("설문조사 삭제 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/survey/list.do";
    }

    /* ─────────────── 내부 유틸 ─────────────── */

    /** 현재 로그인 사용자 loginId 조회 */
    private String getLoginId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "anonymous";
    }

    /** 현재 로그인 사용자 userId(PK) 조회 */
    private int getCurrentUserId() {
        try {
            String loginId = getLoginId();
            if ("anonymous".equals(loginId)) return 0;
            UserVO user = userService.selectUserByLoginId(loginId);
            return user != null ? user.getUserId() : 0;
        } catch (Exception e) {
            logger.warn("getCurrentUserId 오류: {}", e.getMessage());
            return 0;
        }
    }

    /** 현재 로그인 사용자가 ROLE_ADMIN인지 확인 */
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
