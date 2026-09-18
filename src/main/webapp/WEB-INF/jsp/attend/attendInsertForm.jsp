<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- 수정 모드 여부: attendId가 있으면 수정 -->
<c:set var="isUpdate" value="${attendVO.attendId != 0}"/>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0">
        <i class="bi bi-${isUpdate ? 'pencil' : 'plus-circle'} me-2"></i>
        출결 ${isUpdate ? '수정' : '등록'}
    </h4>
    <a href="${pageContext.request.contextPath}/attend/list.do${attendVO.studentId != 0 ? '?studentId='.concat(attendVO.studentId) : ''}"
       class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-list me-1"></i>목록
    </a>
</div>

<c:if test="${not empty errorMsg}">
    <div class="alert alert-danger"><i class="bi bi-exclamation-triangle me-1"></i>${errorMsg}</div>
</c:if>

<div class="row justify-content-center">
<div class="col-md-7">
<div class="card border-0 shadow-sm">
    <div class="card-body">
        <form id="attendForm"
              action="${pageContext.request.contextPath}/attend/${isUpdate ? 'update' : 'insert'}.do"
              method="post" onsubmit="return validateForm('attendForm')">
            <sec:csrfInput/>

            <c:if test="${isUpdate}">
                <input type="hidden" name="attendId" value="${attendVO.attendId}">
            </c:if>

            <!-- 학생 선택 -->
            <div class="mb-3">
                <label class="form-label fw-semibold">학생 <span class="text-danger">*</span></label>
                <c:choose>
                    <c:when test="${not empty targetStudent}">
                        <input type="hidden" name="studentId" value="${targetStudent.studentId}">
                        <input type="text" class="form-control bg-light"
                               value="${targetStudent.studentNum} - ${targetStudent.studentName} (${targetStudent.deptName})" readonly>
                    </c:when>
                    <c:when test="${isUpdate}">
                        <input type="hidden" name="studentId" value="${attendVO.studentId}">
                        <input type="text" class="form-control bg-light"
                               value="${attendVO.studentNum} - ${attendVO.studentName} (${attendVO.deptName})" readonly>
                    </c:when>
                    <c:otherwise>
                        <input type="number" name="studentId" class="form-control"
                               placeholder="학생 ID 입력" required min="1"
                               value="${attendVO.studentId != 0 ? attendVO.studentId : ''}">
                        <div class="form-text text-muted">학생 상세 페이지에서 등록하면 자동 입력됩니다.</div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- 날짜 -->
            <div class="mb-3">
                <label class="form-label fw-semibold">출결 날짜 <span class="text-danger">*</span></label>
                <input type="date" name="attendDate" class="form-control" required
                       value="<fmt:formatDate value='${attendVO.attendDate}' pattern='yyyy-MM-dd'/>">
            </div>

            <!-- 출결 상태 -->
            <div class="mb-3">
                <label class="form-label fw-semibold">출결 상태 <span class="text-danger">*</span></label>
                <div class="d-flex gap-3 flex-wrap">
                    <c:forTokens var="status" items="출석,지각,결석,공결" delims=",">
                    <c:set var="isChecked" value="${attendVO.attendStatus eq status or (empty attendVO.attendStatus and status eq '출석')}"/>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="attendStatus"
                               id="status_${status}" value="${status}" required
                               ${isChecked ? 'checked' : ''}>
                        <label class="form-check-label
                            ${status eq '출석' ? 'text-success' :
                              status eq '지각' ? 'text-warning' :
                              status eq '결석' ? 'text-danger' : 'text-info'} fw-semibold"
                               for="status_${status}">
                            ${status}
                        </label>
                    </div>
                    </c:forTokens>
                </div>
            </div>

            <!-- 비고 -->
            <div class="mb-4">
                <label class="form-label fw-semibold">비고</label>
                <input type="text" name="note" value="${attendVO.note}"
                       class="form-control" placeholder="사유 또는 메모 (선택)" maxlength="200">
            </div>

            <div class="d-flex justify-content-center gap-2">
                <button type="submit" class="btn btn-primary px-4">
                    <i class="bi bi-save me-1"></i>${isUpdate ? '수정 저장' : '등록'}
                </button>
                <a href="${pageContext.request.contextPath}/attend/list.do${attendVO.studentId != 0 ? '?studentId='.concat(attendVO.studentId) : ''}"
                   class="btn btn-outline-secondary px-4">취소</a>
            </div>
        </form>
    </div>
</div>
</div>
</div>
