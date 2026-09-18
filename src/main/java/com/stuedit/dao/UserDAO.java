package com.stuedit.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.stuedit.vo.UserVO;

/**
 * 사용자 정보 DAO
 */
@Repository("userDAO")
public class UserDAO extends AbstractBaseDAO {

    private static final Logger logger = LogManager.getLogger(UserDAO.class);

    /** 로그인 ID로 사용자 조회 (Spring Security 인증용) */
    public UserVO selectUserByLoginId(String loginId) {
        logger.debug("selectUserByLoginId - loginId: {}", loginId);
        return getSqlSession().selectOne("userSQL.selectUserByLoginId", loginId);
    }

    /** PK로 사용자 조회 */
    public UserVO selectUser(int userId) {
        return getSqlSession().selectOne("userSQL.selectUser", userId);
    }

    /** 사용자 등록 */
    public void insertUser(UserVO vo) {
        getSqlSession().insert("userSQL.insertUser", vo);
    }

    /** 연락처·이메일 수정 */
    public void updateUserInfo(UserVO vo) {
        getSqlSession().update("userSQL.updateUserInfo", vo);
    }

    /** 비밀번호 변경 */
    public void updateUserPw(UserVO vo) {
        getSqlSession().update("userSQL.updateUserPw", vo);
    }

    /** 마지막 로그인 시간 갱신 */
    public void updateLastLoginDate(String loginId) {
        getSqlSession().update("userSQL.updateLastLoginDate", loginId);
    }

    /** 로그인 ID 중복 확인 */
    public int selectLoginIdCount(String loginId) {
        return getSqlSession().selectOne("userSQL.selectLoginIdCount", loginId);
    }
}
