package com.stuedit.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stuedit.service.UserService;
import com.stuedit.vo.UserVO;

/**
 * 사용자/보안 컨트롤러
 * URL 패턴: /user/*.do
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /* ===================================================================
     * 로그인 페이지
     * GET /user/login.do
     * Spring Security가 /user/loginProc.do POST를 직접 처리하므로
     * 여기서는 페이지 표시만 담당
     * =================================================================== */
    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public String loginForm(
            @RequestParam(value = "error",   required = false) String error,
            @RequestParam(value = "expired", required = false) String expired,
            ModelMap model) {

        if (error != null) {
            model.addAttribute("errorMsg", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        if (expired != null) {
            model.addAttribute("errorMsg", "다른 곳에서 로그인하여 세션이 만료되었습니다.");
        }
        return "user/login";
    }

    /* ===================================================================
     * 마이페이지 - 본인 정보 조회
     * GET /user/mypage.do
     * =================================================================== */
    @RequestMapping(value = "/mypage.do", method = RequestMethod.GET)
    public String mypage(ModelMap model) throws Exception {
        UserVO loginUser = getLoginUser();
        if (loginUser == null) {
            return "redirect:/user/login.do";
        }
        model.addAttribute("user", loginUser);
        return "user/mypage";
    }

    /* ===================================================================
     * 마이페이지 - 연락처·이메일 수정
     * POST /user/mypageUpdate.do
     * =================================================================== */
    @RequestMapping(value = "/mypageUpdate.do", method = RequestMethod.POST)
    public String mypageUpdate(
            @ModelAttribute UserVO userVO,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            userService.updateUserInfo(userVO);
            redirectAttr.addFlashAttribute("resultMsg", "정보가 수정되었습니다.");
        } catch (Exception e) {
            logger.error("마이페이지 수정 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/user/mypage.do";
    }

    /* ===================================================================
     * 비밀번호 변경 폼
     * GET /user/pwChange.do
     * =================================================================== */
    @RequestMapping(value = "/pwChange.do", method = RequestMethod.GET)
    public String pwChangeForm(ModelMap model) {
        model.addAttribute("userVO", new UserVO());
        return "user/pwChange";
    }

    /* ===================================================================
     * 비밀번호 변경 처리
     * POST /user/pwChange.do
     * =================================================================== */
    @RequestMapping(value = "/pwChange.do", method = RequestMethod.POST)
    public String pwChange(
            @ModelAttribute UserVO userVO,
            RedirectAttributes redirectAttr) throws Exception {

        UserVO loginUser = getLoginUser();
        if (loginUser == null) {
            return "redirect:/user/login.do";
        }
        userVO.setUserId(loginUser.getUserId());

        try {
            userService.updateUserPw(userVO);
            redirectAttr.addFlashAttribute("resultMsg", "비밀번호가 변경되었습니다. 다시 로그인해주세요.");
            // 비밀번호 변경 후 세션 무효화 → 재로그인 유도
            SecurityContextHolder.clearContext();
            return "redirect:/user/login.do";
        } catch (Exception e) {
            logger.error("비밀번호 변경 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/user/pwChange.do";
        }
    }

    /* ===================================================================
     * 현재 로그인 사용자 정보 조회 헬퍼
     * =================================================================== */
    private UserVO getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        try {
            String loginId = auth.getName();
            return userService.selectUserByLoginId(loginId);
        } catch (Exception e) {
            logger.error("로그인 사용자 조회 오류", e);
            return null;
        }
    }
}
