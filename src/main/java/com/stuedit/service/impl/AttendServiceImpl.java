package com.stuedit.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stuedit.dao.AttendDAO;
import com.stuedit.service.AttendService;
import com.stuedit.vo.AttendVO;

/**
 * 출결 관리 서비스 구현체
 */
@Service("attendService")
public class AttendServiceImpl implements AttendService {

    private static final Logger logger = LogManager.getLogger(AttendServiceImpl.class);

    @Autowired
    private AttendDAO attendDAO;

    /** 출결 목록 조회 */
    @Override
    public List<AttendVO> selectAttendList(AttendVO vo) throws Exception {
        if (vo.getPageSize()  == 0) vo.setPageSize(15);
        if (vo.getPageIndex() == 0) vo.setPageIndex(1);
        vo.setOffset((vo.getPageIndex() - 1) * vo.getPageSize());
        return attendDAO.selectAttendList(vo);
    }

    /** 출결 전체 건수 */
    @Override
    public int selectAttendCount(AttendVO vo) throws Exception {
        return attendDAO.selectAttendCount(vo);
    }

    /** 출결 상세 조회 */
    @Override
    public AttendVO selectAttend(int attendId) throws Exception {
        AttendVO vo = attendDAO.selectAttend(attendId);
        if (vo == null) {
            throw new RuntimeException("존재하지 않는 출결 정보입니다.");
        }
        return vo;
    }

    /** 출결 등록: 같은 학생의 같은 날짜 중복 방지 */
    @Override
    public void insertAttend(AttendVO vo) throws Exception {
        int dupCount = attendDAO.selectDuplicateCount(vo);
        if (dupCount > 0) {
            logger.warn("출결 중복 등록 시도 - studentId: {}, date: {}", vo.getStudentId(), vo.getAttendDate());
            throw new RuntimeException(
                    "해당 날짜에 이미 출결이 등록되어 있습니다. 수정 기능을 이용해주세요.");
        }
        attendDAO.insertAttend(vo);
        logger.info("출결 등록 완료 - studentId: {}, date: {}, status: {}",
                vo.getStudentId(), vo.getAttendDate(), vo.getAttendStatus());
    }

    /** 출결 수정 */
    @Override
    public void updateAttend(AttendVO vo) throws Exception {
        // 수정 시에도 다른 날짜로 바꾸는 경우 중복 확인
        int dupCount = attendDAO.selectDuplicateCount(vo);
        if (dupCount > 0) {
            throw new RuntimeException("변경하려는 날짜에 이미 출결이 등록되어 있습니다.");
        }
        attendDAO.updateAttend(vo);
        logger.info("출결 수정 완료 - attendId: {}", vo.getAttendId());
    }

    /** 출결 삭제 */
    @Override
    public void deleteAttend(int attendId) throws Exception {
        attendDAO.deleteAttend(attendId);
        logger.info("출결 삭제 완료 - attendId: {}", attendId);
    }

    /** 학생별 출석률 집계 */
    @Override
    public AttendVO selectAttendStat(AttendVO vo) throws Exception {
        return attendDAO.selectAttendStat(vo);
    }

    /** 오늘 전체 출석률 */
    @Override
    public double selectTodayAttendRate() throws Exception {
        return attendDAO.selectTodayAttendRate();
    }
}
