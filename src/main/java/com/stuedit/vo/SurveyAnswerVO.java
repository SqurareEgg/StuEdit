package com.stuedit.vo;

/**
 * 설문 답변 Value Object
 * TB_SURVEY_ANSWER 테이블과 매핑
 */
public class SurveyAnswerVO {

    /* ── 기본 컬럼 ── */
    private int    answerId;    // PK (AUTO_INCREMENT)
    private int    responseId;  // FK (TB_SURVEY_RESPONSE)
    private int    questionId;  // FK (TB_SURVEY_QUESTION)
    private int    optionId;    // FK (TB_SURVEY_OPTION), nullable (선택형일 때만 사용)
    private String answerText;  // 주관식 답변 텍스트, nullable

    /* ── Getter / Setter ── */

    public int getAnswerId() { return answerId; }
    public void setAnswerId(int answerId) { this.answerId = answerId; }

    public int getResponseId() { return responseId; }
    public void setResponseId(int responseId) { this.responseId = responseId; }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public int getOptionId() { return optionId; }
    public void setOptionId(int optionId) { this.optionId = optionId; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
}
