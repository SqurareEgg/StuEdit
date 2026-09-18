<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- 페이지 제목 -->
<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0">
        <i class="bi bi-bar-chart me-2"></i>성적 조회
        <c:if test="${not empty targetStudent}">
            <span class="fs-6 text-muted ms-2">— ${targetStudent.studentName} (${targetStudent.studentNum})</span>
        </c:if>
    </h4>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <a href="${pageContext.request.contextPath}/grade/insertForm.do${not empty targetStudent ? '?studentId='.concat(targetStudent.studentId) : ''}"
           class="btn btn-primary btn-sm">
            <i class="bi bi-plus-circle me-1"></i>성적 등록
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

<!-- 평균 학점 카드 (특정 학생 조회 시) -->
<c:if test="${not empty gradeAvg}">
<div class="row g-3 mb-3">
    <div class="col-sm-4">
        <div class="card border-0 shadow-sm text-center py-2">
            <div class="text-muted small">평균 점수</div>
            <div class="fs-4 fw-bold text-primary">${gradeAvg.avgScore}</div>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="card border-0 shadow-sm text-center py-2">
            <div class="text-muted small">평균 학점 (GPA)</div>
            <div class="fs-4 fw-bold text-success">${gradeAvg.avgGradePoint}</div>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="card border-0 shadow-sm text-center py-2">
            <div class="text-muted small">총 이수 학점</div>
            <div class="fs-4 fw-bold text-warning">${gradeAvg.totalCredit}학점</div>
        </div>
    </div>
</div>
</c:if>

<!-- 검색 폼 -->
<div class="search-box mb-3">
    <form action="${pageContext.request.contextPath}/grade/list.do" method="get" class="row g-2 align-items-end">
        <c:if test="${not empty targetStudent}">
            <input type="hidden" name="studentId" value="${targetStudent.studentId}">
        </c:if>
        <div class="col-sm-4">
            <label class="form-label small mb-1">학생명/과목명</label>
            <input type="text" name="searchKeyword" value="${searchVO.searchKeyword}"
                   class="form-control form-control-sm" placeholder="학생명 또는 과목명">
        </div>
        <div class="col-sm-3">
            <label class="form-label small mb-1">학기</label>
            <select name="searchSemester" class="form-select form-select-sm">
                <option value="">전체 학기</option>
                <c:forEach var="sem" items="${semesterList}">
                    <option value="${sem}" ${searchVO.searchSemester == sem ? 'selected' : ''}>${sem}</option>
                </c:forEach>
            </select>
        </div>
        <input type="hidden" name="pageIndex" value="1">
        <div class="col-auto">
            <button type="submit" class="btn btn-secondary btn-sm">
                <i class="bi bi-search me-1"></i>검색
            </button>
            <a href="${pageContext.request.contextPath}/grade/list.do${not empty targetStudent ? '?studentId='.concat(targetStudent.studentId) : ''}"
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
                    <th>과목명</th>
                    <th>학기</th>
                    <th>점수</th>
                    <th>학점</th>
                    <th>학점수</th>
                    <th>등록일</th>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <th>관리</th>
                    </sec:authorize>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty gradeList}">
                        <c:forEach var="g" items="${gradeList}" varStatus="st">
                        <tr>
                            <td class="text-center">${totalCount - (searchVO.pageIndex - 1) * searchVO.pageSize - st.index}</td>
                            <td class="text-center">${g.studentNum}</td>
                            <td>${g.studentName}</td>
                            <td>${g.deptName}</td>
                            <td class="fw-semibold">${g.subjectName}</td>
                            <td class="text-center">${g.semester}</td>
                            <td class="text-center">
                                <span class="badge ${g.score >= 90 ? 'bg-success' : g.score >= 70 ? 'bg-primary' : g.score >= 60 ? 'bg-warning text-dark' : 'bg-danger'}">
                                    ${g.score}
                                </span>
                            </td>
                            <td class="text-center fw-bold">${g.gradePoint}</td>
                            <td class="text-center">${g.credit}</td>
                            <td class="text-center"><fmt:formatDate value="${g.regDate}" pattern="yyyy-MM-dd"/></td>
                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/grade/updateForm.do?gradeId=${g.gradeId}"
                                   class="btn btn-outline-primary btn-sm">수정</a>
                                <button class="btn btn-outline-danger btn-sm"
                                        onclick="confirmDelete('${pageContext.request.contextPath}/grade/delete.do?gradeId=${g.gradeId}&studentId=${g.studentId}', '성적을 삭제하시겠습니까?')">삭제</button>
                            </td>
                            </sec:authorize>
                        </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="11" class="text-center text-muted py-4">
                            <i class="bi bi-inbox me-1"></i>조회된 성적이 없습니다.
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
                <a class="page-link" href="?studentId=${searchVO.studentId}&searchKeyword=${searchVO.searchKeyword}&searchSemester=${searchVO.searchSemester}&pageIndex=${startPage - 1}">
                    <i class="bi bi-chevron-left"></i></a>
            </li>
        </c:if>
        <c:forEach var="p" begin="${startPage}" end="${endPage}">
            <li class="page-item ${searchVO.pageIndex == p ? 'active' : ''}">
                <a class="page-link" href="?studentId=${searchVO.studentId}&searchKeyword=${searchVO.searchKeyword}&searchSemester=${searchVO.searchSemester}&pageIndex=${p}">${p}</a>
            </li>
        </c:forEach>
        <c:if test="${endPage < totalPages}">
            <li class="page-item">
                <a class="page-link" href="?studentId=${searchVO.studentId}&searchKeyword=${searchVO.searchKeyword}&searchSemester=${searchVO.searchSemester}&pageIndex=${endPage + 1}">
                    <i class="bi bi-chevron-right"></i></a>
            </li>
        </c:if>
    </ul>
</nav>
</c:if>
