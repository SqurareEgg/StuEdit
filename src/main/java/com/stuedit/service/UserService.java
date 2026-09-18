package com.stuedit.service;

import com.stuedit.vo.UserVO;

/**
 * 사용자/보안 서비스 인터페이스
 */
public interface UserService {

    /** 로그인 ID로 사용자 조회 */
    UserVO selectUserByLoginId(String loginId) throws Exception;

    /** PK로 사용자 조회 */
    UserVO selectUser(int userId) throws Exception;

    /** 사용자 등록 (BCrypt 암호화 포함) */
    void insertUser(UserVO vo) throws Exception;

    /** 마이페이지 - 연락처·이메일 수정 */
    void updateUserInfo(UserVO vo) throws Exception;

    /** 마이페이지 - 비밀번호 변경 (현재 비밀번호 검증 포함) */
    void updateUserPw(UserVO vo) throws Exception;

    /** 마지막 로그인 시간 갱신 */
    void updateLastLoginDate(String loginId) throws Exception;
}
