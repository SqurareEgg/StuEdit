package com.stuedit.service;

import java.util.List;

import com.stuedit.vo.SurveyAnswerVO;
import com.stuedit.vo.SurveyVO;

/**
 * 설문조사 서비스 인터페이스
 */
public interface SurveyService {

    /** 설문조사 목록 조회 (페이징 + 제목 검색) */
    List<SurveyVO> selectSurveyList(SurveyVO vo) throws Exception;

    /** 설문조사 전체 건수 */
    int selectSurveyCount(SurveyVO vo) throws Exception;

    /**
     * 설문조사 상세 조회 (설문 기본정보 + 문항 + 선택지 함께 조회)
     * @param surveyId 설문 PK
     * @return 설문 상세 정보 (questions 필드에 문항 목록 포함, 각 문항의 options 필드에 선택지 포함)
     */
    SurveyVO selectSurveyDetail(int surveyId) throws Exception;

    /**
     * 설문조사 등록 (설문 + 문항 + 선택지 일괄 등록)
     * @param vo 설문 정보 (questions 필드에 문항/선택지 포함)
     */
    void insertSurvey(SurveyVO vo) throws Exception;

    /**
     * 설문 참여 처리
     * 중복 참여 시 RuntimeException("이미 참여한 설문입니다.") 발생
     * @param surveyId 설문 PK
     * @param userId   참여자 userId (TB_USER PK)
     * @param answers  답변 목록
     */
    void participate(int surveyId, int userId, List<SurveyAnswerVO> answers) throws Exception;

    /**
     * 설문 결과 집계 조회 (문항별 선택지 응답 수 포함)
     * @param surveyId 설문 PK
     * @return 설문 상세 정보 (각 옵션의 responseCount 포함)
     */
    SurveyVO selectSurveyResult(int surveyId) throws Exception;

    /** 설문조사 소프트삭제 */
    void deleteSurvey(int surveyId) throws Exception;

    /** 특정 사용자의 특정 설문 참여 여부 확인 (0: 미참여, 1 이상: 참여) */
    int checkParticipation(int surveyId, int userId) throws Exception;
}
