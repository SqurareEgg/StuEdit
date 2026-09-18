package com.stuedit.vo;

/**
 * 설문 선택지 Value Object
 * TB_SURVEY_OPTION 테이블과 매핑
 */
public class SurveyOptionVO {

    /* ── 기본 컬럼 ── */
    private int    optionId;      // PK (AUTO_INCREMENT)
    private int    questionId;    // FK (TB_SURVEY_QUESTION)
    private String optionText;    // 선택지 텍스트
    private int    orderNum;      // 순서

    /* ── 통계용 ── */
    private int    responseCount; // 해당 선택지를 선택한 응답 수

    /* ── Getter / Setter ── */

    public int getOptionId() { return optionId; }
    public void setOptionId(int optionId) { this.optionId = optionId; }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }

    public int getOrderNum() { return orderNum; }
    public void setOrderNum(int orderNum) { this.orderNum = orderNum; }

    public int getResponseCount() { return responseCount; }
    public void setResponseCount(int responseCount) { this.responseCount = responseCount; }
}
