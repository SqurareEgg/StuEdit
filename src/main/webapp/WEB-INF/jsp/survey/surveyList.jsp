<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-clipboard-check me-2"></i>설문조사</h4>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <a href="${pageContext.request.contextPath}/survey/insertForm.do" class="btn btn-primary btn-sm">
            <i class="bi bi-plus-circle me-1"></i>설문 등록
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
    <form action="${pageContext.request.contextPath}/survey/list.do" method="get" class="row g-2 align-items-end">
        <div class="col-sm-5">
            <input type="text" name="searchKeyword" value="${searchVO.searchKeyword}"
                   class="form-control form-control-sm" placeholder="설문 제목을 검색하세요">
        </div>
        <input type="hidden" name="pageIndex" value="1">
        <div class="col-auto">
            <button type="submit" class="btn btn-secondary btn-sm">
                <i class="bi bi-search me-1"></i>검색
            </button>
            <a href="${pageContext.request.contextPath}/survey/list.do"
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
                    <th style="width:200px">기간</th>
                    <th style="width:90px">상태</th>
                    <th style="width:90px">참여</th>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <th style="width:100px">관리</th>
                    </sec:authorize>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty surveyList}">
                        <c:forEach var="s" items="${surveyList}" varStatus="st">
                        <tr>
                            <td class="text-center">
                                ${totalCount - (searchVO.pageIndex - 1) * searchVO.pageSize - st.index}
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/survey/participate.do?surveyId=${s.surveyId}"
                                   class="text-decoration-none">
                                    <i class="bi bi-clipboard me-1 text-muted"></i>${s.title}
                                </a>
                            </td>
                            <td class="text-center small">
                                <fmt:formatDate value="${s.startDate}" pattern="yyyy-MM-dd"/>
                                ~
                                <fmt:formatDate value="${s.endDate}" pattern="yyyy-MM-dd"/>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${s.status == '진행중'}">
                                        <span class="badge bg-primary">${s.status}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">${s.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/survey/result.do?surveyId=${s.surveyId}"
                                   class="btn btn-outline-info btn-sm">
                                    <i class="bi bi-bar-chart me-1"></i>결과
                                </a>
                            </td>
                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <td class="text-center">
                                <button class="btn btn-outline-danger btn-sm"
                                        onclick="confirmDelete('${pageContext.request.contextPath}/survey/delete.do?surveyId=${s.surveyId}', '설문조사를 삭제하시겠습니까?')">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                            </sec:authorize>
                        </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" class="text-center text-muted py-4">
                                <i class="bi bi-inbox me-1"></i>등록된 설문조사가 없습니다.
                            </td>
                        </tr>
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
                <a class="page-link" href="?searchKeyword=${searchVO.searchKeyword}&pageIndex=${startPage - 1}">
                    <i class="bi bi-chevron-left"></i>
                </a>
            </li>
        </c:if>
        <c:forEach var="p" begin="${startPage}" end="${endPage}">
            <li class="page-item ${searchVO.pageIndex == p ? 'active' : ''}">
                <a class="page-link" href="?searchKeyword=${searchVO.searchKeyword}&pageIndex=${p}">${p}</a>
            </li>
        </c:forEach>
        <c:if test="${endPage < totalPages}">
            <li class="page-item">
                <a class="page-link" href="?searchKeyword=${searchVO.searchKeyword}&pageIndex=${endPage + 1}">
                    <i class="bi bi-chevron-right"></i>
                </a>
            </li>
        </c:if>
    </ul>
</nav>
</c:if>
