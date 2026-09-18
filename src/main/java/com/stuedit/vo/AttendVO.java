package com.stuedit.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 출결 정보 Value Object
 * TB_ATTEND 테이블과 매핑
 */
public class AttendVO {

    /* ── 기본 컬럼 ── */
    private int    attendId;     // PK (AUTO_INCREMENT)
    private int    studentId;    // FK → TB_STUDENT
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date   attendDate;   // 출결 날짜
    private String attendStatus; // 출결 상태: 출석/지각/결석/공결
    private String note;         // 비고
    private Date   regDate;      // 등록일

    /* ── 조인 컬럼 (TB_STUDENT) ── */
    private String studentNum;   // 학번
    private String studentName;  // 학생명
    private String deptName;     // 학과명

    /* ── 출석률 집계 컬럼 ── */
    private int    totalCount;   // 전체 출결 건수
    private int    presentCount; // 출석 건수
    private int    lateCount;    // 지각 건수
    private int    absentCount;  // 결석 건수
    private int    officialCount;// 공결 건수
    private double attendRate;   // 출석률 (%) = (출석 + 공결) / 전체 * 100

    /* ── 검색·페이징용 ── */
    private String searchKeyword; // 학생명/학번 검색
    private String startDate;     // 기간 검색 시작일 (yyyy-MM-dd)
    private String endDate;       // 기간 검색 종료일 (yyyy-MM-dd)
    private String searchStatus;  // 출결 상태 검색
    private int    pageIndex;
    private int    pageSize;
    private int    offset;

    /* ── Getter / Setter ── */

    public int getAttendId() { return attendId; }
    public void setAttendId(int attendId) { this.attendId = attendId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public Date getAttendDate() { return attendDate; }
    public void setAttendDate(Date attendDate) { this.attendDate = attendDate; }

    public String getAttendStatus() { return attendStatus; }
    public void setAttendStatus(String attendStatus) { this.attendStatus = attendStatus; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }

    public String getStudentNum() { return studentNum; }
    public void setStudentNum(String studentNum) { this.studentNum = studentNum; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public int getPresentCount() { return presentCount; }
    public void setPresentCount(int presentCount) { this.presentCount = presentCount; }

    public int getLateCount() { return lateCount; }
    public void setLateCount(int lateCount) { this.lateCount = lateCount; }

    public int getAbsentCount() { return absentCount; }
    public void setAbsentCount(int absentCount) { this.absentCount = absentCount; }

    public int getOfficialCount() { return officialCount; }
    public void setOfficialCount(int officialCount) { this.officialCount = officialCount; }

    public double getAttendRate() { return attendRate; }
    public void setAttendRate(double attendRate) { this.attendRate = attendRate; }

    public String getSearchKeyword() { return searchKeyword; }
    public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getSearchStatus() { return searchStatus; }
    public void setSearchStatus(String searchStatus) { this.searchStatus = searchStatus; }

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
