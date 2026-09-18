<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-megaphone me-2"></i>공지사항 상세</h4>
    <div class="d-flex gap-1">
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <a href="${pageContext.request.contextPath}/notice/updateForm.do?noticeId=${notice.noticeId}"
               class="btn btn-outline-primary btn-sm">
                <i class="bi bi-pencil me-1"></i>수정
            </a>
            <button class="btn btn-outline-danger btn-sm"
                    onclick="confirmDelete('${pageContext.request.contextPath}/notice/delete.do?noticeId=${notice.noticeId}', '공지사항을 삭제하시겠습니까?')">
                <i class="bi bi-trash me-1"></i>삭제
            </button>
        </sec:authorize>
        <a href="${pageContext.request.contextPath}/notice/list.do?searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageIndex=${searchVO.pageIndex}"
           class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-list me-1"></i>목록
        </a>
    </div>
</div>

<!-- 공지사항 본문 -->
<div class="card border-0 shadow-sm mb-3">
    <div class="card-header bg-${notice.importantYn == 'Y' ? 'danger' : 'white'} ${notice.importantYn == 'Y' ? 'text-white' : ''}">
        <div class="d-flex align-items-center gap-2">
            <c:if test="${notice.importantYn == 'Y'}">
                <span class="badge bg-light text-danger">중요</span>
            </c:if>
            <h5 class="mb-0">${notice.title}</h5>
        </div>
    </div>
    <div class="card-body">
        <!-- 메타 정보 -->
        <div class="d-flex justify-content-between text-muted small border-bottom pb-2 mb-3">
            <span><i class="bi bi-person me-1"></i>${notice.writer}</span>
            <span>
                <i class="bi bi-calendar me-1"></i>
                <fmt:formatDate value="${notice.regDate}" pattern="yyyy-MM-dd HH:mm"/>
                <c:if test="${not empty notice.modDate}">
                    &nbsp;(수정: <fmt:formatDate value="${notice.modDate}" pattern="yyyy-MM-dd HH:mm"/>)
                </c:if>
                &nbsp;&nbsp;<i class="bi bi-eye me-1"></i>조회 ${notice.viewCount}
            </span>
        </div>
        <!-- 본문 내용: 줄바꿈 처리 -->
        <div class="notice-content lh-lg" style="min-height:150px; white-space: pre-wrap;">${fn:escapeXml(notice.content)}</div>
    </div>
</div>

<!-- 이전글 / 다음글 -->
<div class="card border-0 shadow-sm mb-3">
    <div class="card-body p-0">
        <table class="table table-sm table-bordered mb-0">
            <tr>
                <th class="table-light text-center" style="width:80px">다음글</th>
                <td>
                    <c:choose>
                        <c:when test="${not empty nextNotice}">
                            <a href="${pageContext.request.contextPath}/notice/detail.do?noticeId=${nextNotice.noticeId}"
                               class="text-decoration-none">
                                <i class="bi bi-chevron-up me-1"></i>${nextNotice.title}
                            </a>
                        </c:when>
                        <c:otherwise><span class="text-muted">다음 글이 없습니다.</span></c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <th class="table-light text-center">이전글</th>
                <td>
                    <c:choose>
                        <c:when test="${not empty prevNotice}">
                            <a href="${pageContext.request.contextPath}/notice/detail.do?noticeId=${prevNotice.noticeId}"
                               class="text-decoration-none">
                                <i class="bi bi-chevron-down me-1"></i>${prevNotice.title}
                            </a>
                        </c:when>
                        <c:otherwise><span class="text-muted">이전 글이 없습니다.</span></c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </div>
</div>

<!-- 목록 버튼 -->
<div class="text-center">
    <a href="${pageContext.request.contextPath}/notice/list.do"
       class="btn btn-outline-secondary">
        <i class="bi bi-list me-1"></i>목록으로
    </a>
</div>
