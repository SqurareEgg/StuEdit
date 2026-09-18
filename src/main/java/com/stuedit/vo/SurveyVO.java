package com.stuedit.vo;

import java.util.Date;
import java.util.List;

/**
 * 설문조사 Value Object
 * TB_SURVEY 테이블과 매핑
 */
public class SurveyVO {

    /* ── 기본 컬럼 ── */
    private int    surveyId;      // PK (AUTO_INCREMENT)
    private String title;         // 설문 제목
    private String description;   // 설문 설명
    private Date   startDate;     // 시작일
    private Date   endDate;       // 종료일
    private String status;        // 상태 (진행중/종료)
    private Date   regDate;       // 등록일
    private String delYn;         // 삭제여부 (Y/N)

    /* ── 검색·페이징용 ── */
    private String searchKeyword; // 제목 검색어
    private int    pageIndex;
    private int    pageSize;
    private int    offset;

    /* ── 연관 데이터 ── */
    private List<SurveyQuestionVO> questions; // 설문 문항 목록

    /* ── Getter / Setter ── */

    public int getSurveyId() { return surveyId; }
    public void setSurveyId(int surveyId) { this.surveyId = surveyId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }

    public String getDelYn() { return delYn; }
    public void setDelYn(String delYn) { this.delYn = delYn; }

    public String getSearchKeyword() { return searchKeyword; }
    public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }

    public int getPageIndex() { return pageIndex; }
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex < 1 ? 1 : pageIndex;
        if (this.pageSize > 0) this.offset = (this.pageIndex - 1) * this.pageSize;
    }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? 10 : pageSize;
        if (this.pageIndex > 0) this.offset = (this.pageIndex - 1) * this.pageSize;
    }

    public int getOffset() { return offset; }
    public void setOffset(int offset) { this.offset = offset; }

    public List<SurveyQuestionVO> getQuestions() { return questions; }
    public void setQuestions(List<SurveyQuestionVO> questions) { this.questions = questions; }
}
