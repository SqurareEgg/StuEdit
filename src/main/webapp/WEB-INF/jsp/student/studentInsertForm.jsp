<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- 페이지 제목 -->
<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-person-plus me-2"></i>학생 등록</h4>
    <a href="${pageContext.request.contextPath}/student/list.do"
       class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-list me-1"></i>목록
    </a>
</div>

<!-- 오류 메시지 -->
<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">
        <i class="bi bi-exclamation-triangle me-1"></i>${errorMessage}
    </div>
</c:if>

<div class="card border-0 shadow-sm">
    <div class="card-body">
        <form id="insertForm" action="${pageContext.request.contextPath}/student/insert.do"
              method="post" onsubmit="return validateForm('insertForm')">
            <sec:csrfInput/>

            <div class="row g-3">
                <!-- 학번 -->
                <div class="col-md-4">
                    <label class="form-label">학번 <span class="text-danger">*</span></label>
                    <input type="text" name="studentNum" value="${student.studentNum}"
                           class="form-control" placeholder="예: 2024001" required maxlength="20">
                </div>
                <!-- 이름 -->
                <div class="col-md-4">
                    <label class="form-label">이름 <span class="text-danger">*</span></label>
                    <input type="text" name="studentName" value="${student.studentName}"
                           class="form-control" placeholder="홍길동" required maxlength="50">
                </div>
                <!-- 성별 -->
                <div class="col-md-4">
                    <label class="form-label">성별 <span class="text-danger">*</span></label>
                    <select name="gender" class="form-select" required>
                        <option value="">선택</option>
                        <option value="M" ${student.gender == 'M' ? 'selected' : ''}>남성</option>
                        <option value="F" ${student.gender == 'F' ? 'selected' : ''}>여성</option>
                    </select>
                </div>
                <!-- 학과 -->
                <div class="col-md-4">
                    <label class="form-label">학과 <span class="text-danger">*</span></label>
                    <input type="text" name="deptName" value="${student.deptName}"
                           class="form-control" placeholder="인공지능학과" required maxlength="100">
                </div>
                <!-- 학년 -->
                <div class="col-md-4">
                    <label class="form-label">학년 <span class="text-danger">*</span></label>
                    <select name="grade" class="form-select" required>
                        <option value="">선택</option>
                        <option value="1" ${student.grade == 1 ? 'selected' : ''}>1학년</option>
                        <option value="2" ${student.grade == 2 ? 'selected' : ''}>2학년</option>
                        <option value="3" ${student.grade == 3 ? 'selected' : ''}>3학년</option>
                        <option value="4" ${student.grade == 4 ? 'selected' : ''}>4학년</option>
                    </select>
                </div>
                <!-- 생년월일 -->
                <div class="col-md-4">
                    <label class="form-label">생년월일</label>
                    <input type="date" name="birthDate" value="${student.birthDate}"
                           class="form-control">
                </div>
                <!-- 연락처 -->
                <div class="col-md-4">
                    <label class="form-label">연락처 <span class="text-danger">*</span></label>
                    <input type="tel" name="phone" value="${student.phone}"
                           class="form-control" placeholder="010-0000-0000" required maxlength="20">
                </div>
                <!-- 이메일 -->
                <div class="col-md-4">
                    <label class="form-label">이메일 <span class="text-danger">*</span></label>
                    <input type="email" name="email" value="${student.email}"
                           class="form-control" placeholder="example@email.com" required maxlength="100">
                </div>
                <!-- 주소 -->
                <div class="col-md-8">
                    <label class="form-label">주소</label>
                    <input type="text" name="address" value="${student.address}"
                           class="form-control" placeholder="서울특별시 ..." maxlength="255">
                </div>
            </div>

            <hr class="my-4">
            <div class="d-flex justify-content-center gap-2">
                <button type="submit" class="btn btn-primary px-4">
                    <i class="bi bi-save me-1"></i>등록
                </button>
                <a href="${pageContext.request.contextPath}/student/list.do"
                   class="btn btn-outline-secondary px-4">취소</a>
            </div>
        </form>
    </div>
</div>
