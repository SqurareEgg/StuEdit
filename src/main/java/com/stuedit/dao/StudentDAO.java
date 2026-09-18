package com.stuedit.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.stuedit.vo.StudentVO;

/**
 * 학생 정보 DAO
 * SqlSessionDaoSupport 상속 → getSqlSession()으로 MyBatis 접근
 */
@Repository("studentDAO")
public class StudentDAO extends AbstractBaseDAO {

    private static final Logger logger = LogManager.getLogger(StudentDAO.class);

    /** 학생 목록 조회 (페이징 + 검색) */
    public List<StudentVO> selectStudentList(StudentVO vo) {
        logger.debug("selectStudentList - searchType: {}, keyword: {}, page: {}",
                vo.getSearchType(), vo.getSearchKeyword(), vo.getPageIndex());
        return getSqlSession().selectList("studentSQL.selectStudentList", vo);
    }

    /** 학생 전체 건수 (검색 조건 반영) */
    public int selectStudentCount(StudentVO vo) {
        return getSqlSession().selectOne("studentSQL.selectStudentCount", vo);
    }

    /** 학생 상세 조회 */
    public StudentVO selectStudent(int studentId) {
        return getSqlSession().selectOne("studentSQL.selectStudent", studentId);
    }

    /** 학생 등록 */
    public void insertStudent(StudentVO vo) {
        getSqlSession().insert("studentSQL.insertStudent", vo);
    }

    /** 학생 수정 */
    public void updateStudent(StudentVO vo) {
        getSqlSession().update("studentSQL.updateStudent", vo);
    }

    /** 학생 소프트삭제 */
    public void deleteStudent(int studentId) {
        getSqlSession().update("studentSQL.deleteStudent", studentId);
    }

    /** 학번 중복 확인 (0이면 중복 없음) */
    public int selectStudentNumCount(String studentNum) {
        return getSqlSession().selectOne("studentSQL.selectStudentNumCount", studentNum);
    }

    /** 대시보드: 전체 학생 수 */
    public int selectTotalStudentCount() {
        return getSqlSession().selectOne("studentSQL.selectTotalStudentCount");
    }

    /** 대시보드: 이번 달 신규 등록 수 */
    public int selectMonthlyNewCount() {
        return getSqlSession().selectOne("studentSQL.selectMonthlyNewCount");
    }

    /** 대시보드: 학과별 학생 현황 */
    public List<StudentVO> selectDeptStatList() {
        return getSqlSession().selectList("studentSQL.selectDeptStatList");
    }

    /** 대시보드: 최근 등록 학생 5명 */
    public List<StudentVO> selectRecentStudentList() {
        return getSqlSession().selectList("studentSQL.selectRecentStudentList");
    }

    /** 엑셀 다운로드용: 전체 학생 목록 (페이징 없음) */
    public List<StudentVO> selectStudentListAll(StudentVO vo) {
        return getSqlSession().selectList("studentSQL.selectStudentListAll", vo);
    }
}
