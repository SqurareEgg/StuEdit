package com.stuedit.vo;

import java.util.List;

/**
 * 설문 문항 Value Object
 * TB_SURVEY_QUESTION 테이블과 매핑
 */
public class SurveyQuestionVO {

    /* ── 기본 컬럼 ── */
    private int    questionId;    // PK (AUTO_INCREMENT)
    private int    surveyId;      // FK (TB_SURVEY)
    private String questionText;  // 문항 텍스트
    private String questionType;  // 문항 유형 (single/multi/text)
    private int    orderNum;      // 순서

    /* ── 연관 데이터 ── */
    private List<SurveyOptionVO> options; // 선택지 목록

    /* ── Getter / Setter ── */

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public int getSurveyId() { return surveyId; }
    public void setSurveyId(int surveyId) { this.surveyId = surveyId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public int getOrderNum() { return orderNum; }
    public void setOrderNum(int orderNum) { this.orderNum = orderNum; }

    public List<SurveyOptionVO> getOptions() { return options; }
    public void setOptions(List<SurveyOptionVO> options) { this.options = options; }
}
