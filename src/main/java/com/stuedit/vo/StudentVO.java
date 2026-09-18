package com.stuedit.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 학생 정보 Value Object
 * TB_STUDENT 테이블과 매핑
 */
public class StudentVO {

    /* ── 기본 컬럼 ── */
    private int    studentId;    // PK (AUTO_INCREMENT)
    private String studentNum;   // 학번
    private String studentName;  // 이름
    private String deptName;     // 학과명
    private int    grade;        // 학년 (1~4)
    private String phone;        // 연락처
    private String email;        // 이메일
    private String address;      // 주소
    private String gender;       // 성별 (M/F)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date   birthDate;    // 생년월일
    private Date   regDate;      // 등록일
    private String delYn;        // 삭제여부 (Y/N) - 소프트삭제

    /* ── 검색·페이징용 (DB 컬럼 아님) ── */
    private String searchType;    // 검색 타입 (name/num/dept)
    private String searchKeyword; // 검색어
    private int    pageIndex;     // 현재 페이지 번호 (1부터 시작)
    private int    pageSize;      // 페이지당 건수 (기본 10)
    private int    offset;        // OFFSET 계산값 = (pageIndex-1) * pageSize

    /* ── 통계용 (대시보드) ── */
    private int    studentCount;  // 학과별 학생 수
    private double ratio;         // 학과별 비율(%)

    /* ── Getter / Setter ── */

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentNum() { return studentNum; }
    public void setStudentNum(String studentNum) { this.studentNum = studentNum; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

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
        // pageIndex 변경 시 offset 자동 재계산
        if (this.pageSize > 0) {
            this.offset = (this.pageIndex - 1) * this.pageSize;
        }
    }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? 10 : pageSize;
        // pageSize 변경 시 offset 자동 재계산
        if (this.pageIndex > 0) {
            this.offset = (this.pageIndex - 1) * this.pageSize;
        }
    }

    public int getOffset() { return offset; }
    public void setOffset(int offset) { this.offset = offset; }

    public int getStudentCount() { return studentCount; }
    public void setStudentCount(int studentCount) { this.studentCount = studentCount; }

    public double getRatio() { return ratio; }
    public void setRatio(double ratio) { this.ratio = ratio; }
}
