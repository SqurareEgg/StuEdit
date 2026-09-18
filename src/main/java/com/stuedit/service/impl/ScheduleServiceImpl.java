package com.stuedit.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stuedit.dao.ScheduleDAO;
import com.stuedit.service.ScheduleService;
import com.stuedit.vo.ScheduleVO;

/**
 * 학사 일정 서비스 구현체
 */
@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger logger = LogManager.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ScheduleDAO scheduleDAO;

    /** 학사 일정 목록 조회 */
    @Override
    public List<ScheduleVO> selectScheduleList(ScheduleVO vo) throws Exception {
        if (vo.getPageSize()  == 0) vo.setPageSize(10);
        if (vo.getPageIndex() == 0) vo.setPageIndex(1);
        vo.setOffset((vo.getPageIndex() - 1) * vo.getPageSize());
        return scheduleDAO.selectScheduleList(vo);
    }

    /** 학사 일정 전체 건수 */
    @Override
    public int selectScheduleCount(ScheduleVO vo) throws Exception {
        return scheduleDAO.selectScheduleCount(vo);
    }

    /** 학사 일정 상세 조회 */
    @Override
    public ScheduleVO selectSchedule(int scheduleId) throws Exception {
        ScheduleVO vo = scheduleDAO.selectSchedule(scheduleId);
        if (vo == null) {
            throw new RuntimeException("존재하지 않는 일정입니다.");
        }
        return vo;
    }

    /** 대시보드: 오늘 이후 일정 5개 */
    @Override
    public List<ScheduleVO> selectUpcomingSchedules() throws Exception {
        return scheduleDAO.selectUpcomingSchedules();
    }

    /** 학사 일정 등록 */
    @Override
    public void insertSchedule(ScheduleVO vo) throws Exception {
        if (vo.getScheduleType() == null || vo.getScheduleType().isEmpty()) {
            vo.setScheduleType("일반");
        }
        scheduleDAO.insertSchedule(vo);
        logger.info("학사 일정 등록 완료 - title: {}, type: {}", vo.getTitle(), vo.getScheduleType());
    }

    /** 학사 일정 수정 */
    @Override
    public void updateSchedule(ScheduleVO vo) throws Exception {
        if (vo.getScheduleType() == null || vo.getScheduleType().isEmpty()) {
            vo.setScheduleType("일반");
        }
        scheduleDAO.updateSchedule(vo);
        logger.info("학사 일정 수정 완료 - scheduleId: {}", vo.getScheduleId());
    }

    /** 학사 일정 소프트삭제 */
    @Override
    public void deleteSchedule(int scheduleId) throws Exception {
        scheduleDAO.deleteSchedule(scheduleId);
        logger.info("학사 일정 삭제 완료 - scheduleId: {}", scheduleId);
    }
}
