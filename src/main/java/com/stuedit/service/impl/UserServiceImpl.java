package com.stuedit.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.stuedit.dao.UserDAO;
import com.stuedit.service.UserService;
import com.stuedit.vo.UserVO;

/**
 * 사용자/보안 서비스 구현체
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    /** 로그인 ID로 사용자 조회 */
    @Override
    public UserVO selectUserByLoginId(String loginId) throws Exception {
        return userDAO.selectUserByLoginId(loginId);
    }

    /** PK로 사용자 조회 */
    @Override
    public UserVO selectUser(int userId) throws Exception {
        UserVO vo = userDAO.selectUser(userId);
        if (vo == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
        return vo;
    }

    /** 사용자 등록 (BCrypt 암호화 후 저장) */
    @Override
    public void insertUser(UserVO vo) throws Exception {
        // 로그인 ID 중복 확인
        int dupCount = userDAO.selectLoginIdCount(vo.getLoginId());
        if (dupCount > 0) {
            throw new RuntimeException("이미 사용 중인 아이디입니다: " + vo.getLoginId());
        }
        // 비밀번호 BCrypt 암호화
        String encodedPw = bcryptPasswordEncoder.encode(vo.getUserPw());
        vo.setUserPw(encodedPw);
        userDAO.insertUser(vo);
        logger.info("사용자 등록 완료 - loginId: {}, role: {}", vo.getLoginId(), vo.getRole());
    }

    /** 마이페이지 - 연락처·이메일 수정 */
    @Override
    public void updateUserInfo(UserVO vo) throws Exception {
        userDAO.updateUserInfo(vo);
        logger.info("사용자 정보 수정 - userId: {}", vo.getUserId());
    }

    /** 마이페이지 - 비밀번호 변경 (현재 비밀번호 검증 포함) */
    @Override
    public void updateUserPw(UserVO vo) throws Exception {
        // 현재 비밀번호 검증
        UserVO currentUser = userDAO.selectUser(vo.getUserId());
        if (currentUser == null) {
            throw new RuntimeException("사용자 정보를 찾을 수 없습니다.");
        }
        if (!bcryptPasswordEncoder.matches(vo.getCurrentPw(), currentUser.getUserPw())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }
        // 새 비밀번호 확인 일치 검증
        if (!vo.getNewPw().equals(vo.getNewPwConfirm())) {
            throw new RuntimeException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }
        // 새 비밀번호 암호화 후 저장
        vo.setUserPw(bcryptPasswordEncoder.encode(vo.getNewPw()));
        userDAO.updateUserPw(vo);
        logger.info("비밀번호 변경 완료 - userId: {}", vo.getUserId());
    }

    /** 마지막 로그인 시간 갱신 */
    @Override
    public void updateLastLoginDate(String loginId) throws Exception {
        userDAO.updateLastLoginDate(loginId);
    }
}
