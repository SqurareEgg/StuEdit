package com.stuedit.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stuedit.dao.StudentDAO;
import com.stuedit.service.MailService;
import com.stuedit.service.StudentService;
import com.stuedit.vo.StudentVO;

/**
 * 학생 관리 서비스 구현체
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LogManager.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentDAO studentDAO;

    /** 메일 발송 실패가 학생 등록을 막지 않도록 required=false 로 선언 */
    @Autowired(required = false)
    private MailService mailService;

    /** 학생 목록 조회 (페이징 + 검색) */
    @Override
    public List<StudentVO> selectStudentList(StudentVO vo) throws Exception {
        // pageSize 기본값 설정
        if (vo.getPageSize() == 0) vo.setPageSize(10);
        if (vo.getPageIndex() == 0) vo.setPageIndex(1);
        // offset 계산
        vo.setOffset((vo.getPageIndex() - 1) * vo.getPageSize());
        logger.debug("학생 목록 조회 - page: {}, size: {}, offset: {}",
                vo.getPageIndex(), vo.getPageSize(), vo.getOffset());
        return studentDAO.selectStudentList(vo);
    }

    /** 학생 전체 건수 */
    @Override
    public int selectStudentCount(StudentVO vo) throws Exception {
        return studentDAO.selectStudentCount(vo);
    }

    /** 학생 상세 조회 */
    @Override
    public StudentVO selectStudent(int studentId) throws Exception {
        StudentVO vo = studentDAO.selectStudent(studentId);
        if (vo == null) {
            logger.warn("학생 정보 없음 - studentId: {}", studentId);
            throw new RuntimeException("존재하지 않는 학생입니다.");
        }
        return vo;
    }

    /** 학생 등록 (학번 중복 검사 포함) */
    @Override
    public void insertStudent(StudentVO vo) throws Exception {
        // 학번 중복 검사
        int dupCount = studentDAO.selectStudentNumCount(vo.getStudentNum());
        if (dupCount > 0) {
            logger.warn("학번 중복 - studentNum: {}", vo.getStudentNum());
            throw new RuntimeException("이미 등록된 학번입니다: " + vo.getStudentNum());
        }
        studentDAO.insertStudent(vo);
        logger.info("학생 등록 완료 - studentNum: {}, name: {}", vo.getStudentNum(), vo.getStudentName());

        // 등록 완료 안내 메일 발송 (비동기, 실패해도 트랜잭션 영향 없음)
        if (mailService != null && vo.getEmail() != null && !vo.getEmail().trim().isEmpty()) {
            try {
                mailService.sendStudentRegistrationMail(vo);
            } catch (Exception e) {
                logger.warn("학생 등록 메일 발송 중 예외 (무시) - {}", e.getMessage());
            }
        }
    }

    /** 학생 수정 */
    @Override
    public void updateStudent(StudentVO vo) throws Exception {
        studentDAO.updateStudent(vo);
        logger.info("학생 수정 완료 - studentId: {}", vo.getStudentId());
    }

    /** 학생 소프트삭제 */
    @Override
    public void deleteStudent(int studentId) throws Exception {
        studentDAO.deleteStudent(studentId);
        logger.info("학생 삭제 완료 - studentId: {}", studentId);
    }

    /** 대시보드 통계 데이터 조회 */
    @Override
    public Map<String, Object> selectDashboardData() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("totalStudentCount",  studentDAO.selectTotalStudentCount());
        data.put("monthlyNewCount",    studentDAO.selectMonthlyNewCount());
        data.put("deptStatList",       studentDAO.selectDeptStatList());
        data.put("recentStudentList",  studentDAO.selectRecentStudentList());
        return data;
    }

    /** 엑셀 다운로드용 전체 학생 목록 */
    @Override
    public List<StudentVO> selectStudentListAll(StudentVO vo) throws Exception {
        return studentDAO.selectStudentListAll(vo);
    }

}
