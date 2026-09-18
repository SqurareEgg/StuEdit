<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- 페이지 제목 -->
<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-people me-2"></i>학생 목록</h4>
    <a href="${pageContext.request.contextPath}/student/insertForm.do"
       class="btn btn-primary btn-sm">
        <i class="bi bi-person-plus me-1"></i>학생 등록
    </a>
</div>

<!-- 알림 메시지 -->
<c:if test="${not empty resultMsg}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle me-1"></i>${resultMsg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<!-- ── 검색 폼 ── -->
<div class="search-box mb-3">
    <form action="${pageContext.request.contextPath}/student/list.do" method="get" class="row g-2 align-items-end">
        <div class="col-auto">
            <label class="form-label small mb-1">검색 조건</label>
            <select name="searchType" class="form-select form-select-sm">
                <option value=""   ${searchVO.searchType == null || searchVO.searchType == '' ? 'selected' : ''}>전체</option>
                <option value="name" ${searchVO.searchType == 'name' ? 'selected' : ''}>이름</option>
                <option value="num"  ${searchVO.searchType == 'num'  ? 'selected' : ''}>학번</option>
                <option value="dept" ${searchVO.searchType == 'dept' ? 'selected' : ''}>학과</option>
            </select>
        </div>
        <div class="col-sm-4">
            <label class="form-label small mb-1">검색어</label>
            <input type="text" name="searchKeyword" value="${searchVO.searchKeyword}"
                   class="form-control form-control-sm" placeholder="검색어를 입력하세요">
        </div>
        <div class="col-auto">
            <label class="form-label small mb-1">표시 건수</label>
            <select name="pageSize" class="form-select form-select-sm">
                <option value="10"  ${searchVO.pageSize == 10  ? 'selected' : ''}>10건</option>
                <option value="20"  ${searchVO.pageSize == 20  ? 'selected' : ''}>20건</option>
                <option value="50"  ${searchVO.pageSize == 50  ? 'selected' : ''}>50건</option>
            </select>
        </div>
        <input type="hidden" name="pageIndex" value="1">
        <div class="col-auto">
            <button type="submit" class="btn btn-secondary btn-sm">
                <i class="bi bi-search me-1"></i>검색
            </button>
            <a href="${pageContext.request.contextPath}/student/list.do"
               class="btn btn-outline-secondary btn-sm ms-1">초기화</a>
        </div>
        <!-- 엑셀 다운로드 -->
        <div class="col-auto ms-auto">
            <a href="${pageContext.request.contextPath}/student/excelDownload.do?searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}"
               class="btn btn-success btn-sm">
                <i class="bi bi-file-earmark-excel me-1"></i>엑셀 다운로드
            </a>
        </div>
    </form>
</div>

<!-- ── 목록 테이블 ── -->
<div class="card border-0 shadow-sm">
    <div class="card-header bg-white d-flex justify-content-between align-items-center">
        <span class="small text-muted">
            총 <strong class="text-primary">${totalCount}</strong>명
        </span>
    </div>
    <div class="card-body p-0">
        <table class="table table-hover table-bordered mb-0">
            <thead class="table-light text-center">
                <tr>
                    <th style="width:60px">번호</th>
                    <th style="width:110px">학번</th>
                    <th>이름</th>
                    <th>학과</th>
                    <th style="width:60px">학년</th>
                    <th>연락처</th>
                    <th>이메일</th>
                    <th style="width:100px">등록일</th>
                    <th style="width:100px">관리</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty studentList}">
                        <c:forEach var="stu" items="${studentList}" varStatus="status">
                        <tr>
                            <td class="text-center">${totalCount - (searchVO.pageIndex - 1) * searchVO.pageSize - status.index}</td>
                            <td class="text-center">${stu.studentNum}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/student/detail.do?studentId=${stu.studentId}&searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageIndex=${searchVO.pageIndex}&pageSize=${searchVO.pageSize}"
                                   class="text-decoration-none fw-semibold">${stu.studentName}</a>
                            </td>
                            <td>${stu.deptName}</td>
                            <td class="text-center">${stu.grade}학년</td>
                            <td>${stu.phone}</td>
                            <td>${stu.email}</td>
                            <td class="text-center">
                                <fmt:formatDate value="${stu.regDate}" pattern="yyyy-MM-dd"/>
                            </td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/student/updateForm.do?studentId=${stu.studentId}"
                                   class="btn btn-outline-primary btn-sm">수정</a>
                                <button type="button" class="btn btn-outline-danger btn-sm"
                                        onclick="confirmDelete('${pageContext.request.contextPath}/student/delete.do?studentId=${stu.studentId}', '${stu.studentName} 학생을 삭제하시겠습니까?')">삭제</button>
                            </td>
                        </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="9" class="text-center text-muted py-4">
                                <i class="bi bi-inbox me-1"></i>조회된 학생이 없습니다.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<!-- ── 페이징 ── -->
<c:if test="${totalPages > 0}">
<nav class="mt-3 d-flex justify-content-center">
    <ul class="pagination pagination-sm mb-0">
        <!-- 이전 블록 -->
        <c:if test="${startPage > 1}">
            <li class="page-item">
                <a class="page-link" href="?searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageSize=${searchVO.pageSize}&pageIndex=${startPage - 1}">
                    <i class="bi bi-chevron-left"></i>
                </a>
            </li>
        </c:if>
        <!-- 페이지 번호 -->
        <c:forEach var="p" begin="${startPage}" end="${endPage}">
            <li class="page-item ${searchVO.pageIndex == p ? 'active' : ''}">
                <a class="page-link" href="?searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageSize=${searchVO.pageSize}&pageIndex=${p}">${p}</a>
            </li>
        </c:forEach>
        <!-- 다음 블록 -->
        <c:if test="${endPage < totalPages}">
            <li class="page-item">
                <a class="page-link" href="?searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageSize=${searchVO.pageSize}&pageIndex=${endPage + 1}">
                    <i class="bi bi-chevron-right"></i>
                </a>
            </li>
        </c:if>
    </ul>
</nav>
</c:if>
