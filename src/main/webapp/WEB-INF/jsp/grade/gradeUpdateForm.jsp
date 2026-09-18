<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-pencil me-2"></i>성적 수정</h4>
    <a href="${pageContext.request.contextPath}/grade/list.do?studentId=${gradeVO.studentId}"
       class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-list me-1"></i>목록
    </a>
</div>

<c:if test="${not empty errorMsg}">
    <div class="alert alert-danger"><i class="bi bi-exclamation-triangle me-1"></i>${errorMsg}</div>
</c:if>

<div class="card border-0 shadow-sm">
    <div class="card-body">
        <form id="gradeUpdateForm" action="${pageContext.request.contextPath}/grade/update.do"
              method="post" onsubmit="return validateForm('gradeUpdateForm')">
            <sec:csrfInput/>
            <input type="hidden" name="gradeId"   value="${gradeVO.gradeId}">
            <input type="hidden" name="studentId"  value="${gradeVO.studentId}">

            <div class="row g-3">
                <!-- 학생 정보 (수정 불가) -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">학생</label>
                    <input type="text" class="form-control bg-light"
                           value="${gradeVO.studentNum} - ${gradeVO.studentName} (${gradeVO.deptName})" readonly>
                </div>

                <!-- 학기 -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">학기 <span class="text-danger">*</span></label>
                    <input type="text" name="semester" class="form-control" required
                           value="${gradeVO.semester}" list="semesterDatalist">
                    <datalist id="semesterDatalist">
                        <c:forEach var="sem" items="${semesterList}">
                            <option value="${sem}">
                        </c:forEach>
                    </datalist>
                </div>

                <!-- 과목명 -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">과목명 <span class="text-danger">*</span></label>
                    <input type="text" name="subjectName" value="${gradeVO.subjectName}"
                           class="form-control" required maxlength="100">
                </div>

                <!-- 학점수 -->
                <div class="col-md-2">
                    <label class="form-label fw-semibold">학점수 <span class="text-danger">*</span></label>
                    <select name="credit" class="form-select" required>
                        <option value="1" ${gradeVO.credit == 1 ? 'selected' : ''}>1학점</option>
                        <option value="2" ${gradeVO.credit == 2 ? 'selected' : ''}>2학점</option>
                        <option value="3" ${gradeVO.credit == 3 ? 'selected' : ''}>3학점</option>
                    </select>
                </div>

                <!-- 점수 -->
                <div class="col-md-2">
                    <label class="form-label fw-semibold">점수 <span class="text-danger">*</span></label>
                    <input type="number" name="score" value="${gradeVO.score}"
                           class="form-control" min="0" max="100" step="0.1" required
                           oninput="autoGradePoint(this.value)">
                </div>

                <!-- 학점 -->
                <div class="col-md-2">
                    <label class="form-label fw-semibold">학점</label>
                    <select name="gradePoint" id="gradePoint" class="form-select">
                        <c:forTokens var="gp" items="A+,A,B+,B,C+,C,D+,D,F" delims=",">
                            <option value="${gp}" ${gradeVO.gradePoint eq gp ? 'selected' : ''}>${gp}</option>
                        </c:forTokens>
                    </select>
                </div>
            </div>

            <hr class="my-4">
            <div class="d-flex justify-content-center gap-2">
                <button type="submit" class="btn btn-primary px-4">
                    <i class="bi bi-save me-1"></i>수정 저장
                </button>
                <a href="${pageContext.request.contextPath}/grade/list.do?studentId=${gradeVO.studentId}"
                   class="btn btn-outline-secondary px-4">취소</a>
            </div>
        </form>
    </div>
</div>

<script>
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
