<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-megaphone me-2"></i>공지사항</h4>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <a href="${pageContext.request.contextPath}/notice/insertForm.do" class="btn btn-primary btn-sm">
            <i class="bi bi-plus-circle me-1"></i>공지 등록
        </a>
    </sec:authorize>
</div>

<c:if test="${not empty resultMsg}">
    <div class="alert alert-success alert-dismissible fade show">
        <i class="bi bi-check-circle me-1"></i>${resultMsg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<!-- 검색 폼 -->
<div class="search-box mb-3">
    <form action="${pageContext.request.contextPath}/notice/list.do" method="get" class="row g-2 align-items-end">
        <div class="col-auto">
            <select name="searchType" class="form-select form-select-sm">
                <option value=""      ${searchVO.searchType == null || searchVO.searchType == '' ? 'selected' : ''}>전체</option>
                <option value="title"   ${searchVO.searchType == 'title'   ? 'selected' : ''}>제목</option>
                <option value="content" ${searchVO.searchType == 'content' ? 'selected' : ''}>내용</option>
                <option value="writer"  ${searchVO.searchType == 'writer'  ? 'selected' : ''}>작성자</option>
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
            <a href="${pageContext.request.contextPath}/notice/list.do"
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
                    <th style="width:80px">조회수</th>
                    <th style="width:110px">등록일</th>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <th style="width:100px">관리</th>
                    </sec:authorize>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty noticeList}">
                        <c:forEach var="n" items="${noticeList}" varStatus="st">
                        <tr class="${n.importantYn == 'Y' ? 'table-warning' : ''}">
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${n.importantYn == 'Y'}">
                                        <span class="badge bg-danger">공지</span>
                                    </c:when>
                                    <c:otherwise>
                                        ${totalCount - (searchVO.pageIndex - 1) * searchVO.pageSize - st.index}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/notice/detail.do?noticeId=${n.noticeId}&searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageIndex=${searchVO.pageIndex}"
                                   class="text-decoration-none ${n.importantYn == 'Y' ? 'fw-bold text-danger' : ''}">
                                    ${n.title}
                                </a>
                                <c:if test="${n.fileCount > 0}">
                                    <i class="bi bi-paperclip text-muted ms-1" title="${n.fileCount}개 첨부파일"></i>
                                </c:if>
                            </td>
                            <td class="text-center">${n.writer}</td>
                            <td class="text-center">${n.viewCount}</td>
                            <td class="text-center">
                                <fmt:formatDate value="${n.regDate}" pattern="yyyy-MM-dd"/>
                            </td>
                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/notice/updateForm.do?noticeId=${n.noticeId}"
                                   class="btn btn-outline-primary btn-sm">수정</a>
                                <button class="btn btn-outline-danger btn-sm"
                                        onclick="confirmDelete('${pageContext.request.contextPath}/notice/delete.do?noticeId=${n.noticeId}', '공지사항을 삭제하시겠습니까?')">삭제</button>
                            </td>
                            </sec:authorize>
                        </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="6" class="text-center text-muted py-4">
                            <i class="bi bi-inbox me-1"></i>등록된 공지사항이 없습니다.
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
