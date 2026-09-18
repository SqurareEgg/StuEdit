package com.stuedit.vo;

import java.util.Date;

/**
 * 설문 참여 기록 Value Object
 * TB_SURVEY_RESPONSE 테이블과 매핑
 */
public class SurveyResponseVO {

    /* ── 기본 컬럼 ── */
    private int  responseId; // PK (AUTO_INCREMENT)
    private int  surveyId;   // FK (TB_SURVEY)
    private int  userId;     // FK (TB_USER)
    private Date regDate;    // 참여일시

    /* ── Getter / Setter ── */

    public int getResponseId() { return responseId; }
    public void setResponseId(int responseId) { this.responseId = responseId; }

    public int getSurveyId() { return surveyId; }
    public void setSurveyId(int surveyId) { this.surveyId = surveyId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }
}
