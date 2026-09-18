<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-chat-dots me-2"></i>1:1 문의 상세</h4>
    <div class="d-flex gap-1">
        <%-- 본인 작성글 또는 관리자: 수정/삭제 버튼 표시 --%>
        <sec:authentication property="name" var="loginId"/>
        <c:if test="${inquiry.writerId == loginId}">
            <a href="${pageContext.request.contextPath}/inquiry/updateForm.do?inquiryId=${inquiry.inquiryId}"
               class="btn btn-outline-primary btn-sm">
                <i class="bi bi-pencil me-1"></i>수정
            </a>
            <button class="btn btn-outline-danger btn-sm"
                    onclick="confirmDelete('${pageContext.request.contextPath}/inquiry/delete.do?inquiryId=${inquiry.inquiryId}', '문의를 삭제하시겠습니까?')">
                <i class="bi bi-trash me-1"></i>삭제
            </button>
        </c:if>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <c:if test="${inquiry.writerId != loginId}">
                <button class="btn btn-outline-danger btn-sm"
                        onclick="confirmDelete('${pageContext.request.contextPath}/inquiry/delete.do?inquiryId=${inquiry.inquiryId}', '문의를 삭제하시겠습니까?')">
                    <i class="bi bi-trash me-1"></i>삭제
                </button>
            </c:if>
        </sec:authorize>
        <a href="${pageContext.request.contextPath}/inquiry/list.do?searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageIndex=${searchVO.pageIndex}"
           class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-list me-1"></i>목록
        </a>
    </div>
</div>

<c:if test="${not empty resultMsg}">
    <div class="alert alert-success alert-dismissible fade show">
        <i class="bi bi-check-circle me-1"></i>${resultMsg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>
<c:if test="${not empty errorMsg}">
    <div class="alert alert-danger alert-dismissible fade show">
        <i class="bi bi-exclamation-triangle me-1"></i>${errorMsg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<!-- 문의 본문 -->
<div class="card border-0 shadow-sm mb-3">
    <div class="card-header bg-white">
        <div class="d-flex align-items-center gap-2">
            <c:if test="${inquiry.isSecret == 'Y'}">
                <i class="bi bi-lock-fill text-secondary" title="비밀글"></i>
            </c:if>
            <h5 class="mb-0">${inquiry.title}</h5>
            <c:choose>
                <c:when test="${inquiry.status == '답변완료'}">
                    <span class="badge bg-success ms-auto">답변완료</span>
                </c:when>
                <c:otherwise>
                    <span class="badge bg-warning text-dark ms-auto">대기</span>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="card-body">
        <!-- 메타 정보 -->
        <div class="d-flex justify-content-between text-muted small border-bottom pb-2 mb-3">
            <span><i class="bi bi-person me-1"></i>${inquiry.writerName} (${inquiry.writerId})</span>
            <span>
                <i class="bi bi-calendar me-1"></i>
                <fmt:formatDate value="${inquiry.regDate}" pattern="yyyy-MM-dd HH:mm"/>
                <c:if test="${not empty inquiry.updDate}">
                    &nbsp;(수정: <fmt:formatDate value="${inquiry.updDate}" pattern="yyyy-MM-dd HH:mm"/>)
                </c:if>
            </span>
        </div>
        <!-- 문의 내용 -->
        <div class="inquiry-content lh-lg" style="min-height:150px; white-space: pre-wrap;">${fn:escapeXml(inquiry.content)}</div>
    </div>
</div>

<!-- 답변 영역 -->
<div class="card border-0 shadow-sm mb-3">
    <div class="card-header bg-light">
        <h6 class="mb-0"><i class="bi bi-reply-fill me-2 text-primary"></i>답변</h6>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty inquiry.answer}">
                <div class="d-flex justify-content-between text-muted small mb-2">
                    <span><i class="bi bi-person-badge me-1"></i>답변자: ${inquiry.answeredBy}</span>
                    <span><i class="bi bi-calendar me-1"></i>
                        <fmt:formatDate value="${inquiry.answerDate}" pattern="yyyy-MM-dd HH:mm"/>
                    </span>
                </div>
                <div class="answer-content lh-lg p-3 bg-light rounded" style="white-space: pre-wrap;">${fn:escapeXml(inquiry.answer)}</div>
            </c:when>
            <c:otherwise>
                <p class="text-muted mb-0"><i class="bi bi-hourglass-split me-1"></i>답변 대기 중입니다.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- 관리자 답변 폼 -->
<sec:authorize access="hasRole('ROLE_ADMIN')">
<div class="card border-0 shadow-sm mb-3">
    <div class="card-header bg-primary text-white">
        <h6 class="mb-0"><i class="bi bi-pencil-square me-2"></i>관리자 답변 등록</h6>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/inquiry/answer.do" method="post">
            <sec:csrfInput/>
            <input type="hidden" name="inquiryId" value="${inquiry.inquiryId}">
            <div class="mb-3">
                <label class="form-label fw-semibold">답변 내용 <span class="text-danger">*</span></label>
                <textarea name="answer" class="form-control" rows="6"
                          placeholder="답변 내용을 입력하세요" required>${inquiry.answer}</textarea>
            </div>
            <div class="d-flex justify-content-end">
                <button type="submit" class="btn btn-primary px-4">
                    <i class="bi bi-save me-1"></i>답변 저장
                </button>
            </div>
        </form>
    </div>
</div>
</sec:authorize>

<!-- 목록 버튼 -->
<div class="text-center">
    <a href="${pageContext.request.contextPath}/inquiry/list.do"
       class="btn btn-outline-secondary">
        <i class="bi bi-list me-1"></i>목록으로
    </a>
</div>
