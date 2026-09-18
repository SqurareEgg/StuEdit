package com.stuedit.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.stuedit.vo.GradeVO;

/**
 * 성적 관리 DAO
 */
@Repository("gradeDAO")
public class GradeDAO extends AbstractBaseDAO {

    private static final Logger logger = LogManager.getLogger(GradeDAO.class);

    /** 성적 목록 조회 (페이징 + 검색) */
    public List<GradeVO> selectGradeList(GradeVO vo) {
        logger.debug("selectGradeList - studentId: {}, semester: {}", vo.getStudentId(), vo.getSearchSemester());
        return getSqlSession().selectList("gradeSQL.selectGradeList", vo);
    }

    /** 성적 전체 건수 */
    public int selectGradeCount(GradeVO vo) {
        return getSqlSession().selectOne("gradeSQL.selectGradeCount", vo);
    }

    /** 성적 상세 조회 */
    public GradeVO selectGrade(int gradeId) {
        return getSqlSession().selectOne("gradeSQL.selectGrade", gradeId);
    }

    /** 성적 등록 */
    public void insertGrade(GradeVO vo) {
        getSqlSession().insert("gradeSQL.insertGrade", vo);
    }

    /** 성적 수정 */
    public void updateGrade(GradeVO vo) {
        getSqlSession().update("gradeSQL.updateGrade", vo);
    }

    /** 성적 삭제 */
    public void deleteGrade(int gradeId) {
        getSqlSession().delete("gradeSQL.deleteGrade", gradeId);
    }

    /** 동일 과목·학기 중복 확인 */
    public int selectDuplicateCount(GradeVO vo) {
        return getSqlSession().selectOne("gradeSQL.selectDuplicateCount", vo);
    }

    /** 학생별 평균 학점 계산 */
    public GradeVO selectGradeAvg(int studentId) {
        return getSqlSession().selectOne("gradeSQL.selectGradeAvg", studentId);
    }

    /** 학기 목록 */
    public List<String> selectSemesterList() {
        return getSqlSession().selectList("gradeSQL.selectSemesterList");
    }
}
