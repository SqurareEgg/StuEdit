package com.stuedit.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import com.stuedit.service.UserService;

/**
 * 로그인 성공 핸들러
 * 로그인 성공 시 마지막 로그인 일시를 DB에 기록
 * ※ context-security.xml 에서 <bean id="loginSuccessHandler"> 로 등록됨
 *    @Component 중복 등록 방지를 위해 어노테이션 제거
 */
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LogManager.getLogger(CustomLoginSuccessHandler.class);

    @Autowired
    private UserService userService;

    public CustomLoginSuccessHandler() {
        // 로그인 성공 후 기본 이동 URL
        setDefaultTargetUrl("/main.do");
        setAlwaysUseDefaultTargetUrl(false);
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String loginId = authentication.getName();
        try {
            // 마지막 로그인 시간 갱신
            userService.updateLastLoginDate(loginId);
            logger.info("로그인 성공 - loginId: {}", loginId);
        } catch (Exception e) {
            logger.error("마지막 로그인 시간 갱신 오류", e);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
