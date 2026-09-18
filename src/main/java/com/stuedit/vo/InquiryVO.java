package com.stuedit.vo;

import java.util.Date;

/**
 * 1:1 문의 Value Object
 * TB_INQUIRY 테이블과 매핑
 */
public class InquiryVO {

    /* ── 기본 컬럼 ── */
    private int    inquiryId;     // PK (AUTO_INCREMENT)
    private String title;         // 제목
    private String content;       // 내용
    private String writerId;      // 작성자 로그인 ID
    private String writerName;    // 작성자 이름
    private String isSecret;      // 비밀글 여부 (Y/N)
    private String status;        // 상태 (대기/답변완료)
    private String answer;        // 답변 내용
    private String answeredBy;    // 답변자
    private Date   answerDate;    // 답변일
    private Date   regDate;       // 등록일
    private Date   updDate;       // 수정일
    private String delYn;         // 삭제여부 (Y/N)

    /* ── 검색·페이징용 ── */
    private String searchType;    // 검색 타입 (title/writer)
    private String searchKeyword; // 검색어
    private int    pageIndex;
    private int    pageSize;
    private int    offset;

    /* ── Getter / Setter ── */

    public int getInquiryId() { return inquiryId; }
    public void setInquiryId(int inquiryId) { this.inquiryId = inquiryId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getWriterId() { return writerId; }
    public void setWriterId(String writerId) { this.writerId = writerId; }

    public String getWriterName() { return writerName; }
    public void setWriterName(String writerName) { this.writerName = writerName; }

    public String getIsSecret() { return isSecret; }
    public void setIsSecret(String isSecret) { this.isSecret = isSecret; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getAnsweredBy() { return answeredBy; }
    public void setAnsweredBy(String answeredBy) { this.answeredBy = answeredBy; }

    public Date getAnswerDate() { return answerDate; }
    public void setAnswerDate(Date answerDate) { this.answerDate = answerDate; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }

    public Date getUpdDate() { return updDate; }
    public void setUpdDate(Date updDate) { this.updDate = updDate; }

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
