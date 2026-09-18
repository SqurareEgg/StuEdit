package com.stuedit.service;

import java.util.List;

import com.stuedit.vo.GradeVO;

/**
 * 성적 관리 서비스 인터페이스
 */
public interface GradeService {

    /** 성적 목록 조회 (페이징 + 검색) */
    List<GradeVO> selectGradeList(GradeVO vo) throws Exception;

    /** 성적 전체 건수 */
    int selectGradeCount(GradeVO vo) throws Exception;

    /** 성적 상세 조회 */
    GradeVO selectGrade(int gradeId) throws Exception;

    /** 성적 등록 (중복 검사 포함) */
    void insertGrade(GradeVO vo) throws Exception;

    /** 성적 수정 (중복 검사 포함) */
    void updateGrade(GradeVO vo) throws Exception;

    /** 성적 삭제 */
    void deleteGrade(int gradeId) throws Exception;

    /** 학생별 평균 학점 계산 */
    GradeVO selectGradeAvg(int studentId) throws Exception;

    /** 학기 목록 (검색 드롭다운용) */
    List<String> selectSemesterList() throws Exception;
}
