<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-chat-dots me-2"></i>1:1 문의</h4>
    <sec:authorize access="isAuthenticated()">
        <a href="${pageContext.request.contextPath}/inquiry/insertForm.do" class="btn btn-primary btn-sm">
            <i class="bi bi-plus-circle me-1"></i>문의 등록
        </a>
    </sec:authorize>
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

<!-- 검색 폼 -->
<div class="search-box mb-3">
    <form action="${pageContext.request.contextPath}/inquiry/list.do" method="get" class="row g-2 align-items-end">
        <div class="col-auto">
            <select name="searchType" class="form-select form-select-sm">
                <option value=""       ${searchVO.searchType == null || searchVO.searchType == '' ? 'selected' : ''}>전체</option>
                <option value="title"  ${searchVO.searchType == 'title'  ? 'selected' : ''}>제목</option>
                <option value="writer" ${searchVO.searchType == 'writer' ? 'selected' : ''}>작성자</option>
            </select>
        </div>
        <div class="col-sm-4">
            <input type="text" name="searchKeyword" value="${searchVO.searchKeyword}"
                   class="form-control form-control-sm" placeholder="검색어를 입력하세요">
        </div>
        <input type="hidden" name="pageIndex" value="1">
        <div class="col-auto">
            <button type="submit" class="btn btn-secondary btn-sm">
                <i class="bi bi-search me-1"></i>검색
            </button>
            <a href="${pageContext.request.contextPath}/inquiry/list.do"
               class="btn btn-outline-secondary btn-sm ms-1">초기화</a>
        </div>
    </form>
</div>

<!-- 목록 테이블 -->
<div class="card border-0 shadow-sm">
    <div class="card-header bg-white">
        <span class="small text-muted">총 <strong class="text-primary">${totalCount}</strong>건</span>
    </div>
    <div class="card-body p-0">
        <table class="table table-hover table-bordered mb-0">
            <thead class="table-light text-center">
                <tr>
                    <th style="width:60px">번호</th>
                    <th>제목</th>
                    <th style="width:100px">작성자</th>
                    <th style="width:100px">상태</th>
                    <th style="width:110px">등록일</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty inquiryList}">
                        <c:forEach var="inq" items="${inquiryList}" varStatus="st">
                        <tr>
                            <td class="text-center">
                                ${totalCount - (searchVO.pageIndex - 1) * searchVO.pageSize - st.index}
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/inquiry/detail.do?inquiryId=${inq.inquiryId}&searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageIndex=${searchVO.pageIndex}"
                                   class="text-decoration-none">
                                    <c:if test="${inq.isSecret == 'Y'}">
                                        <i class="bi bi-lock-fill text-secondary me-1" title="비밀글"></i>
                                    </c:if>
                                    ${inq.title}
                                </a>
                            </td>
                            <td class="text-center">${inq.writerName}</td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${inq.status == '답변완료'}">
                                        <span class="badge bg-success">답변완료</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark">대기</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <fmt:formatDate value="${inq.regDate}" pattern="yyyy-MM-dd"/>
                            </td>
                        </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="5" class="text-center text-muted py-4">
                            <i class="bi bi-inbox me-1"></i>등록된 문의가 없습니다.
                        </td></tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<!-- 페이징 -->
<c:if test="${totalPages > 0}">
<nav class="mt-3 d-flex justify-content-center">
    <ul class="pagination pagination-sm mb-0">
        <c:if test="${startPage > 1}">
            <li class="page-item">
                <a class="page-link" href="?searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageIndex=${startPage - 1}">
                    <i class="bi bi-chevron-left"></i></a>
            </li>
        </c:if>
        <c:forEach var="p" begin="${startPage}" end="${endPage}">
            <li class="page-item ${searchVO.pageIndex == p ? 'active' : ''}">
                <a class="page-link" href="?searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageIndex=${p}">${p}</a>
            </li>
        </c:forEach>
        <c:if test="${endPage < totalPages}">
            <li class="page-item">
                <a class="page-link" href="?searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageIndex=${endPage + 1}">
                    <i class="bi bi-chevron-right"></i></a>
            </li>
        </c:if>
    </ul>
</nav>
</c:if>
