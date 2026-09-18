package com.stuedit.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.stuedit.vo.ScheduleVO;

/**
 * 학사 일정 DAO
 */
@Repository("scheduleDAO")
public class ScheduleDAO extends AbstractBaseDAO {

    private static final Logger logger = LogManager.getLogger(ScheduleDAO.class);

    /** 학사 일정 목록 조회 */
    public List<ScheduleVO> selectScheduleList(ScheduleVO vo) {
        logger.debug("selectScheduleList - keyword: {}, page: {}", vo.getSearchKeyword(), vo.getPageIndex());
        return getSqlSession().selectList("scheduleSQL.selectScheduleList", vo);
    }

    /** 학사 일정 전체 건수 */
    public int selectScheduleCount(ScheduleVO vo) {
        return getSqlSession().selectOne("scheduleSQL.selectScheduleCount", vo);
    }

    /** 학사 일정 상세 조회 */
    public ScheduleVO selectSchedule(int scheduleId) {
        return getSqlSession().selectOne("scheduleSQL.selectSchedule", scheduleId);
    }

    /** 대시보드: 오늘 이후 일정 5개 */
    public List<ScheduleVO> selectUpcomingSchedules() {
        return getSqlSession().selectList("scheduleSQL.selectUpcomingSchedules");
    }

    /** 학사 일정 등록 */
    public void insertSchedule(ScheduleVO vo) {
        getSqlSession().insert("scheduleSQL.insertSchedule", vo);
    }

    /** 학사 일정 수정 */
    public void updateSchedule(ScheduleVO vo) {
        getSqlSession().update("scheduleSQL.updateSchedule", vo);
    }

    /** 학사 일정 소프트삭제 */
    public void deleteSchedule(int scheduleId) {
        getSqlSession().update("scheduleSQL.deleteSchedule", scheduleId);
    }
}
