<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-plus-circle me-2"></i>공지사항 등록</h4>
    <a href="${pageContext.request.contextPath}/notice/list.do" class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-list me-1"></i>목록
    </a>
</div>

<c:if test="${not empty errorMsg}">
    <div class="alert alert-danger"><i class="bi bi-exclamation-triangle me-1"></i>${errorMsg}</div>
</c:if>

<div class="card border-0 shadow-sm">
    <div class="card-body">
        <form id="noticeInsertForm" action="${pageContext.request.contextPath}/notice/insert.do"
              method="post" onsubmit="return validateForm('noticeInsertForm')">
            <sec:csrfInput/>

            <!-- 중요 공지 여부 -->
            <div class="mb-3 form-check">
                <input type="checkbox" name="importantYn" id="importantYn"
                       class="form-check-input" value="Y"
                       ${notice.importantYn == 'Y' ? 'checked' : ''}>
                <label class="form-check-label fw-semibold text-danger" for="importantYn">
                    <i class="bi bi-exclamation-circle me-1"></i>중요 공지로 등록 (상단 고정)
                </label>
            </div>

            <!-- 제목 -->
            <div class="mb-3">
                <label class="form-label fw-semibold">제목 <span class="text-danger">*</span></label>
                <input type="text" name="title" value="${notice.title}"
                       class="form-control" placeholder="제목을 입력하세요" required maxlength="200">
            </div>

            <!-- 내용 -->
            <div class="mb-4">
                <label class="form-label fw-semibold">내용 <span class="text-danger">*</span></label>
                <textarea name="content" class="form-control" rows="15"
                          placeholder="내용을 입력하세요" required>${notice.content}</textarea>
            </div>

            <div class="d-flex justify-content-center gap-2">
                <button type="submit" class="btn btn-primary px-4">
                    <i class="bi bi-save me-1"></i>등록
                </button>
                <a href="${pageContext.request.contextPath}/notice/list.do"
                   class="btn btn-outline-secondary px-4">취소</a>
            </div>
        </form>
    </div>
</div>
