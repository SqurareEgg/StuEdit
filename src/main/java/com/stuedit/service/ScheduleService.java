package com.stuedit.service;

import java.util.List;

import com.stuedit.vo.ScheduleVO;

/**
 * 학사 일정 서비스 인터페이스
 */
public interface ScheduleService {

    /** 학사 일정 목록 조회 (페이징 + 검색) */
    List<ScheduleVO> selectScheduleList(ScheduleVO vo) throws Exception;

    /** 학사 일정 전체 건수 */
    int selectScheduleCount(ScheduleVO vo) throws Exception;

    /** 학사 일정 상세 조회 */
    ScheduleVO selectSchedule(int scheduleId) throws Exception;

    /** 대시보드: 오늘 이후 일정 5개 */
    List<ScheduleVO> selectUpcomingSchedules() throws Exception;

    /** 학사 일정 등록 */
    void insertSchedule(ScheduleVO vo) throws Exception;

    /** 학사 일정 수정 */
    void updateSchedule(ScheduleVO vo) throws Exception;

    /** 학사 일정 삭제 (소프트삭제) */
    void deleteSchedule(int scheduleId) throws Exception;
}
