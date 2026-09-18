package com.stuedit.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stuedit.dao.UserDAO;
import com.stuedit.vo.UserVO;

/**
 * Spring Security UserDetailsService 구현체
 * context-security.xml의 user-service-ref="userDetailsService"로 참조됨
 * 로그인 시 DB에서 사용자 정보를 조회하여 인증 처리
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        logger.debug("loadUserByUsername - loginId: {}", loginId);

        // DB에서 사용자 조회
        UserVO userVO = userDAO.selectUserByLoginId(loginId);

        if (userVO == null) {
            logger.warn("로그인 실패 - 존재하지 않는 아이디: {}", loginId);
            throw new UsernameNotFoundException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        // 권한 설정 (ROLE_ADMIN 또는 ROLE_STUDENT)
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userVO.getRole()));

        logger.debug("인증 성공 - loginId: {}, role: {}", loginId, userVO.getRole());

        return User.builder()
                .username(userVO.getLoginId())
                .password(userVO.getUserPw())   // BCrypt 해시값
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!"Y".equals(userVO.getUseYn()))
                .build();
    }
}
