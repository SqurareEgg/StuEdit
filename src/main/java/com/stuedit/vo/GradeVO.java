package com.stuedit.vo;

import java.util.Date;

/**
 * 성적 정보 Value Object
 * TB_GRADE 테이블과 매핑
 */
public class GradeVO {

    /* ── 기본 컬럼 ── */
    private int    gradeId;      // PK (AUTO_INCREMENT)
    private int    studentId;    // FK → TB_STUDENT
    private String subjectName;  // 과목명
    private String semester;     // 학기 (예: 2024-1, 2024-2)
    private double score;        // 점수 (0.0 ~ 100.0)
    private String gradePoint;   // 학점 (A+, A, B+, B, C+, C, D+, D, F)
    private int    credit;       // 학점수 (1~3)
    private Date   regDate;      // 등록일

    /* ── 조인 컬럼 (TB_STUDENT) ── */
    private String studentNum;   // 학번
    private String studentName;  // 학생명
    private String deptName;     // 학과명

    /* ── 집계 컬럼 ── */
    private double avgScore;     // 평균 점수
    private double avgGradePoint;// 평균 학점 (GPA)
    private int    totalCredit;  // 총 이수 학점

    /* ── 검색·페이징용 ── */
    private String searchKeyword;// 검색어 (학생명, 과목명)
    private String searchSemester;// 학기 검색
    private int    pageIndex;
    private int    pageSize;
    private int    offset;

    /* ── Getter / Setter ── */

    public int getGradeId() { return gradeId; }
    public void setGradeId(int gradeId) { this.gradeId = gradeId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getGradePoint() { return gradePoint; }
    public void setGradePoint(String gradePoint) { this.gradePoint = gradePoint; }

    public int getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }

    public String getStudentNum() { return studentNum; }
    public void setStudentNum(String studentNum) { this.studentNum = studentNum; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public double getAvgScore() { return avgScore; }
    public void setAvgScore(double avgScore) { this.avgScore = avgScore; }

    public double getAvgGradePoint() { return avgGradePoint; }
    public void setAvgGradePoint(double avgGradePoint) { this.avgGradePoint = avgGradePoint; }

    public int getTotalCredit() { return totalCredit; }
    public void setTotalCredit(int totalCredit) { this.totalCredit = totalCredit; }

    public String getSearchKeyword() { return searchKeyword; }
    public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }

    public String getSearchSemester() { return searchSemester; }
    public void setSearchSemester(String searchSemester) { this.searchSemester = searchSemester; }

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
