package com.stuedit.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.stuedit.vo.SurveyAnswerVO;
import com.stuedit.vo.SurveyOptionVO;
import com.stuedit.vo.SurveyQuestionVO;
import com.stuedit.vo.SurveyResponseVO;
import com.stuedit.vo.SurveyVO;

/**
 * 설문조사 DAO
 */
@Repository("surveyDAO")
public class SurveyDAO extends AbstractBaseDAO {

    private static final Logger logger = LogManager.getLogger(SurveyDAO.class);

    /** 설문조사 목록 조회 (페이징 + 제목 검색) */
    public List<SurveyVO> selectSurveyList(SurveyVO vo) {
        logger.debug("selectSurveyList - keyword: {}, page: {}", vo.getSearchKeyword(), vo.getPageIndex());
        return getSqlSession().selectList("surveySQL.selectSurveyList", vo);
    }

    /** 설문조사 전체 건수 */
    public int selectSurveyCount(SurveyVO vo) {
        return getSqlSession().selectOne("surveySQL.selectSurveyCount", vo);
    }

    /** 설문조사 상세 조회 (기본 정보만) */
    public SurveyVO selectSurvey(int surveyId) {
        return getSqlSession().selectOne("surveySQL.selectSurvey", surveyId);
    }

    /** 설문조사 등록 */
    public void insertSurvey(SurveyVO vo) {
        getSqlSession().insert("surveySQL.insertSurvey", vo);
    }

    /** 설문 상태 변경 */
    public void updateSurveyStatus(SurveyVO vo) {
        getSqlSession().update("surveySQL.updateSurveyStatus", vo);
    }

    /** 설문조사 소프트삭제 */
    public void deleteSurvey(int surveyId) {
        getSqlSession().update("surveySQL.deleteSurvey", surveyId);
    }

    /** 문항 목록 조회 (survey_id 기준, order_num 순) */
    public List<SurveyQuestionVO> selectQuestionsBySurvey(int surveyId) {
        return getSqlSession().selectList("surveySQL.selectQuestionsBySurvey", surveyId);
    }

    /** 문항 등록 */
    public void insertQuestion(SurveyQuestionVO vo) {
        getSqlSession().insert("surveySQL.insertQuestion", vo);
    }

    /** 선택지 목록 조회 (question_id 기준) */
    public List<SurveyOptionVO> selectOptionsByQuestion(int questionId) {
        return getSqlSession().selectList("surveySQL.selectOptionsByQuestion", questionId);
    }

    /** 선택지 등록 */
    public void insertOption(SurveyOptionVO vo) {
        getSqlSession().insert("surveySQL.insertOption", vo);
    }

    /** 참여 여부 확인 */
    public int checkParticipation(SurveyResponseVO vo) {
        return getSqlSession().selectOne("surveySQL.checkParticipation", vo);
    }

    /** 참여 기록 등록 */
    public void insertResponse(SurveyResponseVO vo) {
        getSqlSession().insert("surveySQL.insertResponse", vo);
    }

    /** 답변 등록 */
    public void insertAnswer(SurveyAnswerVO vo) {
        getSqlSession().insert("surveySQL.insertAnswer", vo);
    }

    /** 선택지별 응답 수 집계 */
    public List<SurveyOptionVO> selectOptionStats(int surveyId) {
        return getSqlSession().selectList("surveySQL.selectOptionStats", surveyId);
    }

    /** 총 참여자 수 조회 */
    public int selectTotalResponseCount(int surveyId) {
        return getSqlSession().selectOne("surveySQL.selectTotalResponseCount", surveyId);
    }
}
