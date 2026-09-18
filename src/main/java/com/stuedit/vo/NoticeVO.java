package com.stuedit.vo;

import java.util.Date;

/**
 * 공지사항 Value Object
 * TB_NOTICE 테이블과 매핑
 */
public class NoticeVO {

    /* ── 기본 컬럼 ── */
    private int    noticeId;     // PK (AUTO_INCREMENT)
    private String title;        // 제목
    private String content;      // 내용
    private String writer;       // 작성자 (로그인 ID)
    private int    viewCount;    // 조회수
    private String importantYn;  // 중요 공지 여부 (Y/N)
    private String delYn;        // 삭제여부 (Y/N)
    private Date   regDate;      // 등록일
    private Date   modDate;      // 수정일

    /* ── 파일 첨부 여부 (TB_FILE 조인) ── */
    private int    fileCount;    // 첨부파일 수

    /* ── 검색·페이징용 ── */
    private String searchType;    // 검색 타입 (title/content/writer)
    private String searchKeyword; // 검색어
    private int    pageIndex;
    private int    pageSize;
    private int    offset;

    /* ── Getter / Setter ── */

    public int getNoticeId() { return noticeId; }
    public void setNoticeId(int noticeId) { this.noticeId = noticeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getWriter() { return writer; }
    public void setWriter(String writer) { this.writer = writer; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public String getImportantYn() { return importantYn; }
    public void setImportantYn(String importantYn) { this.importantYn = importantYn; }

    public String getDelYn() { return delYn; }
    public void setDelYn(String delYn) { this.delYn = delYn; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }

    public Date getModDate() { return modDate; }
    public void setModDate(Date modDate) { this.modDate = modDate; }

    public int getFileCount() { return fileCount; }
    public void setFileCount(int fileCount) { this.fileCount = fileCount; }

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
