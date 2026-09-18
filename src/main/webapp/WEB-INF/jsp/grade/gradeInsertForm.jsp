<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-pencil-square me-2"></i>성적 등록</h4>
    <a href="${pageContext.request.contextPath}/grade/list.do" class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-list me-1"></i>목록
    </a>
</div>

<c:if test="${not empty errorMsg}">
    <div class="alert alert-danger"><i class="bi bi-exclamation-triangle me-1"></i>${errorMsg}</div>
</c:if>

<div class="card border-0 shadow-sm">
    <div class="card-body">
        <form id="gradeInsertForm" action="${pageContext.request.contextPath}/grade/insert.do"
              method="post" onsubmit="return validateForm('gradeInsertForm')">
            <sec:csrfInput/>

            <div class="row g-3">
                <!-- 학생 선택 -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">학번 / 학생명 <span class="text-danger">*</span></label>
                    <c:choose>
                        <c:when test="${not empty targetStudent}">
                            <input type="hidden" name="studentId" value="${targetStudent.studentId}">
                            <input type="text" class="form-control bg-light"
                                   value="${targetStudent.studentNum} - ${targetStudent.studentName}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="number" name="studentId" value="${gradeVO.studentId}"
                                   class="form-control" placeholder="학생 ID 입력" required min="1">
                            <div class="form-text text-muted">학생 상세 페이지에서 등록하면 자동 입력됩니다.</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- 학기 -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">학기 <span class="text-danger">*</span></label>
                    <input type="text" name="semester" class="form-control" required
                           placeholder="예: 2024-1" list="semesterDatalist">
                    <datalist id="semesterDatalist">
                        <c:forEach var="sem" items="${semesterList}">
                            <option value="${sem}">
                        </c:forEach>
                    </datalist>
                    <div class="form-text text-muted">형식: 연도-학기 (예: 2024-1, 2024-2)</div>
                </div>

                <!-- 과목명 -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">과목명 <span class="text-danger">*</span></label>
                    <input type="text" name="subjectName" value="${gradeVO.subjectName}"
                           class="form-control" placeholder="예: 자료구조" required maxlength="100">
                </div>

                <!-- 학점수 -->
                <div class="col-md-2">
                    <label class="form-label fw-semibold">학점수 <span class="text-danger">*</span></label>
                    <select name="credit" class="form-select" required>
                        <option value="">선택</option>
                        <option value="1">1학점</option>
                        <option value="2">2학점</option>
                        <option value="3">3학점</option>
                    </select>
                </div>

                <!-- 점수 -->
                <div class="col-md-2">
                    <label class="form-label fw-semibold">점수 <span class="text-danger">*</span></label>
                    <input type="number" name="score" value="${gradeVO.score}"
                           class="form-control" placeholder="0~100"
                           min="0" max="100" step="0.1" required
                           oninput="autoGradePoint(this.value)">
                </div>

                <!-- 학점 (점수에 따라 자동 계산) -->
                <div class="col-md-2">
                    <label class="form-label fw-semibold">학점</label>
                    <select name="gradePoint" id="gradePoint" class="form-select">
                        <option value="A+">A+</option>
                        <option value="A">A</option>
                        <option value="B+">B+</option>
                        <option value="B">B</option>
                        <option value="C+">C+</option>
                        <option value="C">C</option>
                        <option value="D+">D+</option>
                        <option value="D">D</option>
                        <option value="F">F</option>
                    </select>
                    <div class="form-text text-muted">점수 입력 시 자동 설정</div>
                </div>
            </div>

            <hr class="my-4">
            <div class="d-flex justify-content-center gap-2">
                <button type="submit" class="btn btn-primary px-4">
                    <i class="bi bi-save me-1"></i>등록
                </button>
                <a href="${pageContext.request.contextPath}/grade/list.do" class="btn btn-outline-secondary px-4">취소</a>
            </div>
        </form>
    </div>
</div>

<script>
/* 점수 → 학점 자동 변환 */
function autoGradePoint(score) {
    var s = parseFloat(score);
    var gp = 'F';
    if      (s >= 95) gp = 'A+';
    else if (s >= 90) gp = 'A';
    else if (s >= 85) gp = 'B+';
    else if (s >= 80) gp = 'B';
    else if (s >= 75) gp = 'C+';
    else if (s >= 70) gp = 'C';
    else if (s >= 65) gp = 'D+';
    else if (s >= 60) gp = 'D';
    document.getElementById('gradePoint').value = gp;
}
</script>
