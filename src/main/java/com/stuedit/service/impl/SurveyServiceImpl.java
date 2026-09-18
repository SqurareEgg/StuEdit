package com.stuedit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stuedit.dao.SurveyDAO;
import com.stuedit.service.SurveyService;
import com.stuedit.vo.SurveyAnswerVO;
import com.stuedit.vo.SurveyOptionVO;
import com.stuedit.vo.SurveyQuestionVO;
import com.stuedit.vo.SurveyResponseVO;
import com.stuedit.vo.SurveyVO;

/**
 * 설문조사 서비스 구현체
 */
@Service("surveyService")
public class SurveyServiceImpl implements SurveyService {

    private static final Logger logger = LogManager.getLogger(SurveyServiceImpl.class);

    @Autowired
    private SurveyDAO surveyDAO;

    /** 설문조사 목록 조회 */
    @Override
    public List<SurveyVO> selectSurveyList(SurveyVO vo) throws Exception {
        if (vo.getPageSize()  == 0) vo.setPageSize(10);
        if (vo.getPageIndex() == 0) vo.setPageIndex(1);
        vo.setOffset((vo.getPageIndex() - 1) * vo.getPageSize());
        return surveyDAO.selectSurveyList(vo);
    }

    /** 설문조사 전체 건수 */
    @Override
    public int selectSurveyCount(SurveyVO vo) throws Exception {
        return surveyDAO.selectSurveyCount(vo);
    }

    /** 설문조사 상세 조회 (설문 + 문항 + 선택지) */
    @Override
    public SurveyVO selectSurveyDetail(int surveyId) throws Exception {
        SurveyVO survey = surveyDAO.selectSurvey(surveyId);
        if (survey == null) {
            throw new RuntimeException("존재하지 않는 설문입니다.");
        }
        // 문항 목록 조회
        List<SurveyQuestionVO> questions = surveyDAO.selectQuestionsBySurvey(surveyId);
        // 각 문항에 선택지 목록 설정
        for (SurveyQuestionVO q : questions) {
            List<SurveyOptionVO> options = surveyDAO.selectOptionsByQuestion(q.getQuestionId());
            q.setOptions(options);
        }
        survey.setQuestions(questions);
        return survey;
    }

    /** 설문조사 등록 (설문 + 문항 + 선택지 일괄 등록) */
    @Override
    public void insertSurvey(SurveyVO vo) throws Exception {
        // 1. 설문 등록 (useGeneratedKeys로 surveyId 자동 세팅)
        surveyDAO.insertSurvey(vo);
        logger.info("설문조사 등록 완료 - surveyId: {}, title: {}", vo.getSurveyId(), vo.getTitle());

        // 2. 문항 및 선택지 등록
        if (vo.getQuestions() != null) {
            int qOrder = 1;
            for (SurveyQuestionVO q : vo.getQuestions()) {
                q.setSurveyId(vo.getSurveyId());
                q.setOrderNum(qOrder++);
                surveyDAO.insertQuestion(q);
                logger.debug("문항 등록 - questionId: {}, text: {}", q.getQuestionId(), q.getQuestionText());

                // 선택형 문항인 경우 선택지 등록
                if (!"text".equals(q.getQuestionType()) && q.getOptions() != null) {
                    int oOrder = 1;
                    for (SurveyOptionVO o : q.getOptions()) {
                        o.setQuestionId(q.getQuestionId());
                        o.setOrderNum(oOrder++);
                        surveyDAO.insertOption(o);
                    }
                }
            }
        }
    }

    /** 설문 참여 처리 (중복 체크 포함) */
    @Override
    public void participate(int surveyId, int userId, List<SurveyAnswerVO> answers) throws Exception {
        // 1. 중복 참여 확인
        SurveyResponseVO checkVO = new SurveyResponseVO();
        checkVO.setSurveyId(surveyId);
        checkVO.setUserId(userId);
        int count = surveyDAO.checkParticipation(checkVO);
        if (count > 0) {
            throw new RuntimeException("이미 참여한 설문입니다.");
        }

        // 2. 참여 기록 등록
        SurveyResponseVO responseVO = new SurveyResponseVO();
        responseVO.setSurveyId(surveyId);
        responseVO.setUserId(userId);
        surveyDAO.insertResponse(responseVO);
        int responseId = responseVO.getResponseId();
        logger.info("설문 참여 등록 - surveyId: {}, userId: {}, responseId: {}", surveyId, userId, responseId);

        // 3. 답변 등록
        if (answers != null) {
            for (SurveyAnswerVO answer : answers) {
                answer.setResponseId(responseId);
                surveyDAO.insertAnswer(answer);
            }
        }
    }

    /** 설문 결과 집계 조회 */
    @Override
    public SurveyVO selectSurveyResult(int surveyId) throws Exception {
        SurveyVO survey = surveyDAO.selectSurvey(surveyId);
        if (survey == null) {
            throw new RuntimeException("존재하지 않는 설문입니다.");
        }

        // 문항 목록 조회
        List<SurveyQuestionVO> questions = surveyDAO.selectQuestionsBySurvey(surveyId);

        // 선택지별 응답 수 집계 (한 번에 조회)
        List<SurveyOptionVO> allOptionStats = surveyDAO.selectOptionStats(surveyId);

        // questionId -> optionStats 맵 구성
        Map<Integer, List<SurveyOptionVO>> optionStatsMap = new HashMap<Integer, List<SurveyOptionVO>>();
        for (SurveyOptionVO opt : allOptionStats) {
            int qId = opt.getQuestionId();
            if (!optionStatsMap.containsKey(qId)) {
                optionStatsMap.put(qId, new ArrayList<SurveyOptionVO>());
            }
            optionStatsMap.get(qId).add(opt);
        }

        // 문항에 선택지 통계 설정
        for (SurveyQuestionVO q : questions) {
            List<SurveyOptionVO> stats = optionStatsMap.get(q.getQuestionId());
            if (stats == null) stats = new ArrayList<SurveyOptionVO>();
            q.setOptions(stats);
        }

        survey.setQuestions(questions);
        return survey;
    }

    /** 설문조사 소프트삭제 */
    @Override
    public void deleteSurvey(int surveyId) throws Exception {
        surveyDAO.deleteSurvey(surveyId);
        logger.info("설문조사 삭제 완료 - surveyId: {}", surveyId);
    }

    /** 참여 여부 확인 */
    @Override
    public int checkParticipation(int surveyId, int userId) throws Exception {
        SurveyResponseVO vo = new SurveyResponseVO();
        vo.setSurveyId(surveyId);
        vo.setUserId(userId);
        return surveyDAO.checkParticipation(vo);
    }
}
