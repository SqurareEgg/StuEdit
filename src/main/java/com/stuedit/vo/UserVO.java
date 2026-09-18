package com.stuedit.vo;

import java.util.Date;

/**
 * 사용자 정보 Value Object
 * TB_USER 테이블과 매핑
 */
public class UserVO {

    /* ── 기본 컬럼 ── */
    private int    userId;       // PK (AUTO_INCREMENT)
    private String loginId;      // 로그인 아이디 (학번 또는 관리자ID)
    private String userPw;       // 비밀번호 (BCrypt 해시)
    private String userName;     // 사용자명
    private String role;         // 권한 (ROLE_ADMIN / ROLE_STUDENT)
    private String phone;        // 연락처
    private String email;        // 이메일
    private Date   regDate;      // 등록일
    private Date   lastLoginDate;// 마지막 로그인 일시
    private String useYn;        // 사용여부 (Y/N)
    private int    studentId;    // 연결된 학생 ID (ROLE_STUDENT인 경우)

    /* ── 비밀번호 변경용 (DB 컬럼 아님) ── */
    private String currentPw;    // 현재 비밀번호 (검증용)
    private String newPw;        // 새 비밀번호
    private String newPwConfirm; // 새 비밀번호 확인

    /* ── Getter / Setter ── */

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }

    public String getUserPw() { return userPw; }
    public void setUserPw(String userPw) { this.userPw = userPw; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }

    public Date getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(Date lastLoginDate) { this.lastLoginDate = lastLoginDate; }

    public String getUseYn() { return useYn; }
    public void setUseYn(String useYn) { this.useYn = useYn; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getCurrentPw() { return currentPw; }
    public void setCurrentPw(String currentPw) { this.currentPw = currentPw; }

    public String getNewPw() { return newPw; }
    public void setNewPw(String newPw) { this.newPw = newPw; }

    public String getNewPwConfirm() { return newPwConfirm; }
    public void setNewPwConfirm(String newPwConfirm) { this.newPwConfirm = newPwConfirm; }
}
