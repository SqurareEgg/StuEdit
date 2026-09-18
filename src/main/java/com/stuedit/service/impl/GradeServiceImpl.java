package com.stuedit.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stuedit.dao.GradeDAO;
import com.stuedit.service.GradeService;
import com.stuedit.vo.GradeVO;

/**
 * 성적 관리 서비스 구현체
 */
@Service("gradeService")
public class GradeServiceImpl implements GradeService {

    private static final Logger logger = LogManager.getLogger(GradeServiceImpl.class);

    @Autowired
    private GradeDAO gradeDAO;

    /** 성적 목록 조회 */
    @Override
    public List<GradeVO> selectGradeList(GradeVO vo) throws Exception {
        if (vo.getPageSize()  == 0) vo.setPageSize(10);
        if (vo.getPageIndex() == 0) vo.setPageIndex(1);
        vo.setOffset((vo.getPageIndex() - 1) * vo.getPageSize());
        return gradeDAO.selectGradeList(vo);
    }

    /** 성적 전체 건수 */
    @Override
    public int selectGradeCount(GradeVO vo) throws Exception {
        return gradeDAO.selectGradeCount(vo);
    }

    /** 성적 상세 조회 */
    @Override
    public GradeVO selectGrade(int gradeId) throws Exception {
        GradeVO vo = gradeDAO.selectGrade(gradeId);
        if (vo == null) {
            throw new RuntimeException("존재하지 않는 성적 정보입니다.");
        }
        return vo;
    }

    /** 성적 등록: 동일 과목·학기 중복 방지 */
    @Override
    public void insertGrade(GradeVO vo) throws Exception {
        checkDuplicate(vo);
        gradeDAO.insertGrade(vo);
        logger.info("성적 등록 완료 - studentId: {}, subject: {}, semester: {}",
                vo.getStudentId(), vo.getSubjectName(), vo.getSemester());
    }

    /** 성적 수정: 동일 과목·학기 중복 방지 (자기 자신 제외) */
    @Override
    public void updateGrade(GradeVO vo) throws Exception {
        checkDuplicate(vo);
        gradeDAO.updateGrade(vo);
        logger.info("성적 수정 완료 - gradeId: {}", vo.getGradeId());
    }

    /** 성적 삭제 */
    @Override
    public void deleteGrade(int gradeId) throws Exception {
        gradeDAO.deleteGrade(gradeId);
        logger.info("성적 삭제 완료 - gradeId: {}", gradeId);
    }

    /** 학생별 평균 학점 계산 */
    @Override
    public GradeVO selectGradeAvg(int studentId) throws Exception {
        return gradeDAO.selectGradeAvg(studentId);
    }

    /** 학기 목록 */
    @Override
    public List<String> selectSemesterList() throws Exception {
        return gradeDAO.selectSemesterList();
    }

    /** 동일 과목·학기 중복 검사 공통 메서드 */
    private void checkDuplicate(GradeVO vo) throws Exception {
        int dupCount = gradeDAO.selectDuplicateCount(vo);
        if (dupCount > 0) {
            logger.warn("성적 중복 등록 시도 - studentId: {}, subject: {}, semester: {}",
                    vo.getStudentId(), vo.getSubjectName(), vo.getSemester());
            throw new RuntimeException(
                    "이미 등록된 성적입니다. [" + vo.getSemester() + " / " + vo.getSubjectName() + "]");
        }
    }
}
