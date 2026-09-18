<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- 페이지 제목 -->
<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0">
        <i class="bi bi-calendar-check me-2"></i>출결 현황
        <c:if test="${not empty targetStudent}">
            <span class="fs-6 text-muted ms-2">— ${targetStudent.studentName} (${targetStudent.studentNum})</span>
        </c:if>
    </h4>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <a href="${pageContext.request.contextPath}/attend/insertForm.do${not empty targetStudent ? '?studentId='.concat(targetStudent.studentId) : ''}"
           class="btn btn-primary btn-sm">
            <i class="bi bi-plus-circle me-1"></i>출결 등록
        </a>
    </sec:authorize>
</div>

<!-- 알림 -->
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

<!-- 출석률 통계 카드 (특정 학생 조회 시) -->
<c:if test="${not empty attendStat}">
<div class="row g-2 mb-3">
    <div class="col">
        <div class="card border-0 shadow-sm text-center py-2">
            <div class="text-muted small">전체</div>
            <div class="fs-5 fw-bold">${attendStat.totalCount}일</div>
        </div>
    </div>
    <div class="col">
        <div class="card border-0 bg-success bg-opacity-10 text-center py-2">
            <div class="text-muted small">출석</div>
            <div class="fs-5 fw-bold text-success">${attendStat.presentCount}일</div>
        </div>
    </div>
    <div class="col">
        <div class="card border-0 bg-warning bg-opacity-10 text-center py-2">
            <div class="text-muted small">지각</div>
            <div class="fs-5 fw-bold text-warning">${attendStat.lateCount}일</div>
        </div>
    </div>
    <div class="col">
        <div class="card border-0 bg-danger bg-opacity-10 text-center py-2">
            <div class="text-muted small">결석</div>
            <div class="fs-5 fw-bold text-danger">${attendStat.absentCount}일</div>
        </div>
    </div>
    <div class="col">
        <div class="card border-0 bg-info bg-opacity-10 text-center py-2">
            <div class="text-muted small">공결</div>
            <div class="fs-5 fw-bold text-info">${attendStat.officialCount}일</div>
        </div>
    </div>
    <div class="col">
        <div class="card border-0 bg-primary bg-opacity-10 text-center py-2">
            <div class="text-muted small">출석률</div>
            <div class="fs-5 fw-bold text-primary">${attendStat.attendRate}%</div>
        </div>
    </div>
</div>
<!-- 출석률 프로그레스바 -->
<div class="mb-3">
    <div class="d-flex justify-content-between small mb-1">
        <span>출석률</span>
        <span class="fw-bold ${attendStat.attendRate >= 80 ? 'text-success' : attendStat.attendRate >= 60 ? 'text-warning' : 'text-danger'}">${attendStat.attendRate}%</span>
    </div>
    <div class="progress" style="height:12px;">
        <div class="progress-bar ${attendStat.attendRate >= 80 ? 'bg-success' : attendStat.attendRate >= 60 ? 'bg-warning' : 'bg-danger'}"
             style="width:${attendStat.attendRate}%"></div>
    </div>
</div>
</c:if>

<!-- 검색 폼 -->
<div class="search-box mb-3">
    <form action="${pageContext.request.contextPath}/attend/list.do" method="get" class="row g-2 align-items-end">
        <c:if test="${not empty targetStudent}">
            <input type="hidden" name="studentId" value="${targetStudent.studentId}">
        </c:if>
        <div class="col-sm-3">
            <label class="form-label small mb-1">학생명/학번</label>
            <input type="text" name="searchKeyword" value="${searchVO.searchKeyword}"
                   class="form-control form-control-sm" placeholder="학생명 또는 학번">
        </div>
        <div class="col-sm-2">
            <label class="form-label small mb-1">출결 상태</label>
            <select name="searchStatus" class="form-select form-select-sm">
                <option value="">전체</option>
                <option value="출석" ${searchVO.searchStatus == '출석' ? 'selected' : ''}>출석</option>
                <option value="지각" ${searchVO.searchStatus == '지각' ? 'selected' : ''}>지각</option>
                <option value="결석" ${searchVO.searchStatus == '결석' ? 'selected' : ''}>결석</option>
                <option value="공결" ${searchVO.searchStatus == '공결' ? 'selected' : ''}>공결</option>
            </select>
        </div>
        <div class="col-sm-2">
            <label class="form-label small mb-1">시작일</label>
            <input type="date" name="startDate" value="${searchVO.startDate}"
                   class="form-control form-control-sm">
        </div>
        <div class="col-sm-2">
            <label class="form-label small mb-1">종료일</label>
            <input type="date" name="endDate" value="${searchVO.endDate}"
                   class="form-control form-control-sm">
        </div>
        <input type="hidden" name="pageIndex" value="1">
        <div class="col-auto">
            <button type="submit" class="btn btn-secondary btn-sm">
                <i class="bi bi-search me-1"></i>검색
            </button>
            <a href="${pageContext.request.contextPath}/attend/list.do${not empty targetStudent ? '?studentId='.concat(targetStudent.studentId) : ''}"
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
                    <th>번호</th>
                    <th>학번</th>
                    <th>학생명</th>
                    <th>학과</th>
                    <th>날짜</th>
                    <th>출결 상태</th>
                    <th>비고</th>
                    <th>등록일</th>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <th>관리</th>
                    </sec:authorize>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty attendList}">
                        <c:forEach var="a" items="${attendList}" varStatus="st">
                        <tr>
                            <td class="text-center">${totalCount - (searchVO.pageIndex - 1) * searchVO.pageSize - st.index}</td>
                            <td class="text-center">${a.studentNum}</td>
                            <td>${a.studentName}</td>
                            <td>${a.deptName}</td>
                            <td class="text-center"><fmt:formatDate value="${a.attendDate}" pattern="yyyy-MM-dd"/></td>
                            <td class="text-center">
                                <span class="badge
                                    ${a.attendStatus == '출석' ? 'bg-success' :
                                      a.attendStatus == '지각' ? 'bg-warning text-dark' :
                                      a.attendStatus == '결석' ? 'bg-danger' : 'bg-info'}">
                                    ${a.attendStatus}
                                </span>
                            </td>
                            <td>${a.note}</td>
                            <td class="text-center"><fmt:formatDate value="${a.regDate}" pattern="yyyy-MM-dd"/></td>
                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/attend/updateForm.do?attendId=${a.attendId}"
                                   class="btn btn-outline-primary btn-sm">수정</a>
                                <button class="btn btn-outline-danger btn-sm"
                                        onclick="confirmDelete('${pageContext.request.contextPath}/attend/delete.do?attendId=${a.attendId}&studentId=${a.studentId}', '출결을 삭제하시겠습니까?')">삭제</button>
                            </td>
                            </sec:authorize>
                        </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="9" class="text-center text-muted py-4">
                            <i class="bi bi-inbox me-1"></i>조회된 출결 내역이 없습니다.
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
                <a class="page-link" href="?studentId=${searchVO.studentId}&searchKeyword=${searchVO.searchKeyword}&searchStatus=${searchVO.searchStatus}&startDate=${searchVO.startDate}&endDate=${searchVO.endDate}&pageIndex=${startPage - 1}">
                    <i class="bi bi-chevron-left"></i></a>
            </li>
        </c:if>
        <c:forEach var="p" begin="${startPage}" end="${endPage}">
            <li class="page-item ${searchVO.pageIndex == p ? 'active' : ''}">
                <a class="page-link" href="?studentId=${searchVO.studentId}&searchKeyword=${searchVO.searchKeyword}&searchStatus=${searchVO.searchStatus}&startDate=${searchVO.startDate}&endDate=${searchVO.endDate}&pageIndex=${p}">${p}</a>
            </li>
        </c:forEach>
        <c:if test="${endPage < totalPages}">
            <li class="page-item">
                <a class="page-link" href="?studentId=${searchVO.studentId}&searchKeyword=${searchVO.searchKeyword}&searchStatus=${searchVO.searchStatus}&startDate=${searchVO.startDate}&endDate=${searchVO.endDate}&pageIndex=${endPage + 1}">
                    <i class="bi bi-chevron-right"></i></a>
            </li>
        </c:if>
    </ul>
</nav>
</c:if>
