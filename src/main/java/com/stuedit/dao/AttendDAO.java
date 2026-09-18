package com.stuedit.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.stuedit.vo.AttendVO;

/**
 * 출결 관리 DAO
 */
@Repository("attendDAO")
public class AttendDAO extends AbstractBaseDAO {

    private static final Logger logger = LogManager.getLogger(AttendDAO.class);

    /** 출결 목록 조회 */
    public List<AttendVO> selectAttendList(AttendVO vo) {
        logger.debug("selectAttendList - studentId: {}, date: {}~{}",
                vo.getStudentId(), vo.getStartDate(), vo.getEndDate());
        return getSqlSession().selectList("attendSQL.selectAttendList", vo);
    }

    /** 출결 전체 건수 */
    public int selectAttendCount(AttendVO vo) {
        return getSqlSession().selectOne("attendSQL.selectAttendCount", vo);
    }

    /** 출결 상세 조회 */
    public AttendVO selectAttend(int attendId) {
        return getSqlSession().selectOne("attendSQL.selectAttend", attendId);
    }

    /** 출결 등록 */
    public void insertAttend(AttendVO vo) {
        getSqlSession().insert("attendSQL.insertAttend", vo);
    }

    /** 출결 수정 */
    public void updateAttend(AttendVO vo) {
        getSqlSession().update("attendSQL.updateAttend", vo);
    }

    /** 출결 삭제 */
    public void deleteAttend(int attendId) {
        getSqlSession().delete("attendSQL.deleteAttend", attendId);
    }

    /** 날짜+학생 중복 확인 */
    public int selectDuplicateCount(AttendVO vo) {
        return getSqlSession().selectOne("attendSQL.selectDuplicateCount", vo);
    }

    /** 학생별 출석률 집계 */
    public AttendVO selectAttendStat(AttendVO vo) {
        return getSqlSession().selectOne("attendSQL.selectAttendStat", vo);
    }

    /** 오늘 전체 출석률 (대시보드용) */
    public double selectTodayAttendRate() {
        Double rate = getSqlSession().selectOne("attendSQL.selectTodayAttendRate");
        return rate != null ? rate : 0.0;
    }
}
