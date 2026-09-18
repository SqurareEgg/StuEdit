package com.stuedit.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.stuedit.dao.UserDAO;
import com.stuedit.vo.UserVO;

/**
 * 애플리케이션 최초 구동 시 초기 계정 자동 생성
 * ─ 이미 존재하면 스킵
 * ─ 관리자: admin / admin1234
 * ─ 학생  : student01 / student1234
 */
@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LogManager.getLogger(DataInitializer.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    private boolean initialized = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // ContextRefreshedEvent 는 부모/자식 컨텍스트 양쪽에서 발생 → 중복 방지
        if (initialized) return;
        initialized = true;

        try {
            createIfAbsent("admin",      "admin1234",    "시스템관리자", "admin@stuedit.com",    "ROLE_ADMIN");
            createIfAbsent("student01",  "student1234",  "홍길동",       "student01@stuedit.com","ROLE_STUDENT");
        } catch (Exception e) {
            logger.warn("초기 계정 생성 중 오류 (DB 미연결 시 무시): {}", e.getMessage());
        }
    }

    private void createIfAbsent(String loginId, String rawPw, String name, String email, String role) {
        try {
            int cnt = userDAO.selectLoginIdCount(loginId);
            if (cnt == 0) {
                UserVO vo = new UserVO();
                vo.setLoginId(loginId);
                vo.setUserPw(bcryptPasswordEncoder.encode(rawPw));
                vo.setUserName(name);
                vo.setEmail(email);
                vo.setRole(role);
                vo.setUseYn("Y");
                userDAO.insertUser(vo);
                logger.info("초기 계정 생성 완료 - loginId: {}, role: {}", loginId, role);
            } else {
                logger.debug("계정 이미 존재 - loginId: {}", loginId);
            }
        } catch (Exception e) {
            logger.warn("계정 생성 실패 - loginId: {}, 사유: {}", loginId, e.getMessage());
        }
    }
}
