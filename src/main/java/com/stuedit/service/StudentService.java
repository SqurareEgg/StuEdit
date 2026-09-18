package com.stuedit.service;

import java.util.List;
import java.util.Map;

import com.stuedit.vo.StudentVO;

/**
 * 학생 관리 서비스 인터페이스
 */
public interface StudentService {

    /** 학생 목록 조회 (페이징 + 검색) */
    List<StudentVO> selectStudentList(StudentVO vo) throws Exception;

    /** 학생 전체 건수 */
    int selectStudentCount(StudentVO vo) throws Exception;

    /** 학생 상세 조회 */
    StudentVO selectStudent(int studentId) throws Exception;

    /** 학생 등록 */
    void insertStudent(StudentVO vo) throws Exception;

    /** 학생 수정 */
    void updateStudent(StudentVO vo) throws Exception;

    /** 학생 소프트삭제 */
    void deleteStudent(int studentId) throws Exception;

    /** 대시보드 통계 데이터 조회 */
    Map<String, Object> selectDashboardData() throws Exception;

    /** 엑셀 다운로드용 전체 학생 목록 */
    List<StudentVO> selectStudentListAll(StudentVO vo) throws Exception;
}
