<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-calendar-check me-2"></i>학사 일정 수정</h4>
    <a href="${pageContext.request.contextPath}/schedule/list.do" class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-list me-1"></i>목록
    </a>
</div>

<c:if test="${not empty errorMsg}">
    <div class="alert alert-danger alert-dismissible fade show">
        <i class="bi bi-exclamation-triangle me-1"></i>${errorMsg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<div class="card border-0 shadow-sm">
    <div class="card-body">
        <form id="scheduleUpdateForm" action="${pageContext.request.contextPath}/schedule/update.do"
              method="post" onsubmit="return validateScheduleForm()">
            <sec:csrfInput/>
            <input type="hidden" name="scheduleId" value="${schedule.scheduleId}">

            <!-- 제목 -->
            <div class="mb-3">
                <label for="title" class="form-label fw-semibold">제목 <span class="text-danger">*</span></label>
                <input type="text" name="title" id="title" value="${schedule.title}"
                       class="form-control" required maxlength="200">
            </div>

            <!-- 유형 -->
            <div class="mb-3">
                <label for="scheduleType" class="form-label fw-semibold">유형 <span class="text-danger">*</span></label>
                <select name="scheduleType" id="scheduleType" class="form-select" required>
                    <option value="일반" ${schedule.scheduleType == '일반' ? 'selected' : ''}>일반</option>
                    <option value="시험" ${schedule.scheduleType == '시험' ? 'selected' : ''}>시험</option>
                    <option value="방학" ${schedule.scheduleType == '방학' ? 'selected' : ''}>방학</option>
                    <option value="휴일" ${schedule.scheduleType == '휴일' ? 'selected' : ''}>휴일</option>
                </select>
            </div>

            <!-- 시작일 / 종료일 -->
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="startDate" class="form-label fw-semibold">시작일 <span class="text-danger">*</span></label>
                    <input type="date" name="startDate" id="startDate"
                           class="form-control" required
                           value="<fmt:formatDate value='${schedule.startDate}' pattern='yyyy-MM-dd'/>">
                </div>
                <div class="col-md-6">
                    <label for="endDate" class="form-label fw-semibold">종료일 <span class="text-danger">*</span></label>
                    <input type="date" name="endDate" id="endDate"
                           class="form-control" required
                           value="<fmt:formatDate value='${schedule.endDate}' pattern='yyyy-MM-dd'/>">
                </div>
            </div>

            <!-- 내용 -->
            <div class="mb-4">
                <label for="content" class="form-label fw-semibold">내용</label>
                <textarea name="content" id="content" class="form-control" rows="8">${schedule.content}</textarea>
            </div>

            <div class="d-flex justify-content-center gap-2">
                <button type="submit" class="btn btn-primary px-4">
                    <i class="bi bi-save me-1"></i>수정 저장
                </button>
                <a href="${pageContext.request.contextPath}/schedule/list.do"
                   class="btn btn-outline-secondary px-4">취소</a>
            </div>
        </form>
    </div>
</div>

<script>
function validateScheduleForm() {
    var title     = document.getElementById('title').value.trim();
    var startDate = document.getElementById('startDate').value;
    var endDate   = document.getElementById('endDate').value;

    if (!title) {
        alert('제목을 입력하세요.');
        document.getElementById('title').focus();
        return false;
    }
    if (!startDate) {
        alert('시작일을 선택하세요.');
        document.getElementById('startDate').focus();
        return false;
    }
    if (!endDate) {
        alert('종료일을 선택하세요.');
        document.getElementById('endDate').focus();
        return false;
    }
    if (startDate > endDate) {
        alert('종료일은 시작일보다 같거나 늦어야 합니다.');
        document.getElementById('endDate').focus();
        return false;
    }
    return true;
}
</script>
