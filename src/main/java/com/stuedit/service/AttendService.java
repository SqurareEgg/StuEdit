package com.stuedit.service;

import java.util.List;

import com.stuedit.vo.AttendVO;

/**
 * 출결 관리 서비스 인터페이스
 */
public interface AttendService {

    /** 출결 목록 조회 (페이징 + 기간·상태 검색) */
    List<AttendVO> selectAttendList(AttendVO vo) throws Exception;

    /** 출결 전체 건수 */
    int selectAttendCount(AttendVO vo) throws Exception;

    /** 출결 상세 조회 */
    AttendVO selectAttend(int attendId) throws Exception;

    /** 출결 등록 (날짜 중복 방지) */
    void insertAttend(AttendVO vo) throws Exception;

    /** 출결 수정 */
    void updateAttend(AttendVO vo) throws Exception;

    /** 출결 삭제 */
    void deleteAttend(int attendId) throws Exception;

    /** 학생별 출석률 집계 (기간 검색 가능) */
    AttendVO selectAttendStat(AttendVO vo) throws Exception;

    /** 오늘 전체 출석률 (대시보드용) */
    double selectTodayAttendRate() throws Exception;
}
