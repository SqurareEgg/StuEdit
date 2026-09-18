package com.stuedit.vo;

import java.util.Date;

/**
 * 학사 일정 Value Object
 * TB_ACADEMIC_SCHEDULE 테이블과 매핑
 */
public class ScheduleVO {

    /* ── 기본 컬럼 ── */
    private int    scheduleId;    // PK (AUTO_INCREMENT)
    private String title;         // 제목
    private String content;       // 내용
    private Date   startDate;     // 시작일
    private Date   endDate;       // 종료일
    private String scheduleType;  // 유형 (일반/시험/방학/휴일)
    private Date   regDate;       // 등록일
    private String delYn;         // 삭제여부 (Y/N)

    /* ── 검색·페이징용 ── */
    private String searchType;    // 검색 타입 (title/type)
    private String searchKeyword; // 검색어
    private int    pageIndex;
    private int    pageSize;
    private int    offset;

    /* ── Getter / Setter ── */

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getScheduleType() { return scheduleType; }
    public void setScheduleType(String scheduleType) { this.scheduleType = scheduleType; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }

    public String getDelYn() { return delYn; }
    public void setDelYn(String delYn) { this.delYn = delYn; }

    public String getSearchType() { return searchType; }
    public void setSearchType(String searchType) { this.searchType = searchType; }

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
}
