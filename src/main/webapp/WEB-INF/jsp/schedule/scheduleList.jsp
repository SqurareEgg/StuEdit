<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-calendar3 me-2"></i>학사 일정</h4>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <a href="${pageContext.request.contextPath}/schedule/insertForm.do" class="btn btn-primary btn-sm">
            <i class="bi bi-plus-circle me-1"></i>일정 등록
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
    <form action="${pageContext.request.contextPath}/schedule/list.do" method="get" class="row g-2 align-items-end">
        <div class="col-auto">
            <select name="searchType" class="form-select form-select-sm">
                <option value=""     ${searchVO.searchType == null || searchVO.searchType == '' ? 'selected' : ''}>제목</option>
                <option value="type" ${searchVO.searchType == 'type' ? 'selected' : ''}>유형</option>
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
            <a href="${pageContext.request.contextPath}/schedule/list.do"
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
                    <th style="width:90px">유형</th>
                    <th>제목</th>
                    <th style="width:200px">기간</th>
                    <th style="width:110px">등록일</th>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <th style="width:120px">관리</th>
                    </sec:authorize>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty scheduleList}">
                        <c:forEach var="s" items="${scheduleList}" varStatus="st">
                        <tr>
                            <td class="text-center">
                                ${totalCount - (searchVO.pageIndex - 1) * searchVO.pageSize - st.index}
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${s.scheduleType == '시험'}">
                                        <span class="badge bg-danger">${s.scheduleType}</span>
                                    </c:when>
                                    <c:when test="${s.scheduleType == '방학'}">
                                        <span class="badge bg-success">${s.scheduleType}</span>
                                    </c:when>
                                    <c:when test="${s.scheduleType == '휴일'}">
                                        <span class="badge bg-warning text-dark">${s.scheduleType}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-info text-dark">${s.scheduleType}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="#" class="text-decoration-none schedule-detail-link"
                                   data-schedule-id="${s.scheduleId}"
                                   data-bs-toggle="modal" data-bs-target="#scheduleDetailModal">
                                    ${s.title}
                                </a>
                            </td>
                            <td class="text-center">
                                <fmt:formatDate value="${s.startDate}" pattern="yyyy-MM-dd"/>
                                ~
                                <fmt:formatDate value="${s.endDate}" pattern="yyyy-MM-dd"/>
                            </td>
                            <td class="text-center">
                                <fmt:formatDate value="${s.regDate}" pattern="yyyy-MM-dd"/>
                            </td>
                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/schedule/updateForm.do?scheduleId=${s.scheduleId}"
                                   class="btn btn-outline-primary btn-sm">수정</a>
                                <button class="btn btn-outline-danger btn-sm"
                                        onclick="confirmDelete('${pageContext.request.contextPath}/schedule/delete.do?scheduleId=${s.scheduleId}', '일정을 삭제하시겠습니까?')">삭제</button>
                            </td>
                            </sec:authorize>
                        </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="6" class="text-center text-muted py-4">
                            <i class="bi bi-calendar-x me-1"></i>등록된 학사 일정이 없습니다.
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

<!-- 상세 보기 모달 -->
<div class="modal fade" id="scheduleDetailModal" tabindex="-1" aria-labelledby="scheduleDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="scheduleDetailModalLabel">
                    <i class="bi bi-calendar-event me-2"></i>학사 일정 상세
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" id="scheduleDetailBody">
                <div class="text-center py-3">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <a href="#" id="scheduleUpdateBtn" class="btn btn-primary btn-sm">
                        <i class="bi bi-pencil me-1"></i>수정
                    </a>
                </sec:authorize>
                <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<script>
(function() {
    var ctx = '${pageContext.request.contextPath}';

    document.querySelectorAll('.schedule-detail-link').forEach(function(link) {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            var scheduleId = this.getAttribute('data-schedule-id');
            loadScheduleDetail(scheduleId);
        });
    });

    function loadScheduleDetail(scheduleId) {
        var body = document.getElementById('scheduleDetailBody');
        var updateBtn = document.getElementById('scheduleUpdateBtn');

        body.innerHTML = '<div class="text-center py-3"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div></div>';

        if (updateBtn) {
            updateBtn.href = ctx + '/schedule/updateForm.do?scheduleId=' + scheduleId;
        }

        fetch(ctx + '/schedule/detail.do?scheduleId=' + scheduleId, {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
        .then(function(res) { return res.json(); })
        .then(function(data) {
            if (data.success) {
                var typeBadge = getTypeBadge(data.scheduleType);
                var contentHtml = data.content
                    ? data.content.replace(/\n/g, '<br>')
                    : '<span class="text-muted">내용 없음</span>';

                body.innerHTML =
                    '<dl class="row mb-0">' +
                    '  <dt class="col-sm-3">제목</dt>' +
                    '  <dd class="col-sm-9">' + escapeHtml(data.title) + '</dd>' +
                    '  <dt class="col-sm-3">유형</dt>' +
                    '  <dd class="col-sm-9">' + typeBadge + '</dd>' +
                    '  <dt class="col-sm-3">시작일</dt>' +
                    '  <dd class="col-sm-9">' + data.startDate + '</dd>' +
                    '  <dt class="col-sm-3">종료일</dt>' +
                    '  <dd class="col-sm-9">' + data.endDate + '</dd>' +
                    '  <dt class="col-sm-3">등록일</dt>' +
                    '  <dd class="col-sm-9">' + data.regDate + '</dd>' +
                    '  <dt class="col-sm-3">내용</dt>' +
                    '  <dd class="col-sm-9">' + contentHtml + '</dd>' +
                    '</dl>';
            } else {
                body.innerHTML = '<div class="alert alert-danger"><i class="bi bi-exclamation-triangle me-1"></i>' + escapeHtml(data.message) + '</div>';
            }
        })
        .catch(function(err) {
            body.innerHTML = '<div class="alert alert-danger"><i class="bi bi-exclamation-triangle me-1"></i>데이터를 불러오는 중 오류가 발생했습니다.</div>';
        });
    }

    function getTypeBadge(type) {
        var cls;
        switch(type) {
            case '시험': cls = 'bg-danger'; break;
            case '방학': cls = 'bg-success'; break;
            case '휴일': cls = 'bg-warning text-dark'; break;
            default:    cls = 'bg-info text-dark'; break;
        }
        return '<span class="badge ' + cls + '">' + escapeHtml(type) + '</span>';
    }

    function escapeHtml(text) {
        var div = document.createElement('div');
        div.appendChild(document.createTextNode(text || ''));
        return div.innerHTML;
    }
})();
</script>
