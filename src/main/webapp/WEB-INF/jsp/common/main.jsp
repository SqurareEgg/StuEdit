<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- 페이지 제목 -->
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-house-door me-2"></i>대시보드</h4>
    <small class="text-muted">
        <fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy년 MM월 dd일"/>
    </small>
</div>

<!-- ── 통계 카드 (관리자) ── -->
<sec:authorize access="hasRole('ROLE_ADMIN')">
<div class="row g-3 mb-4">

    <!-- 전체 학생 수 -->
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm h-100">
            <div class="card-body d-flex align-items-center">
                <div class="rounded-circle bg-primary bg-opacity-10 p-3 me-3">
                    <i class="bi bi-people fs-3 text-primary"></i>
                </div>
                <div>
                    <div class="text-muted small">전체 학생 수</div>
                    <div class="fs-4 fw-bold">${totalStudentCount}<span class="fs-6 text-muted ms-1">명</span></div>
                </div>
            </div>
            <div class="card-footer bg-transparent border-0">
                <a href="${pageContext.request.contextPath}/student/list.do"
                   class="btn btn-sm btn-outline-primary w-100">학생 목록 보기</a>
            </div>
        </div>
    </div>

    <!-- 이번 달 등록 학생 -->
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm h-100">
            <div class="card-body d-flex align-items-center">
                <div class="rounded-circle bg-success bg-opacity-10 p-3 me-3">
                    <i class="bi bi-person-plus fs-3 text-success"></i>
                </div>
                <div>
                    <div class="text-muted small">이번 달 신규 등록</div>
                    <div class="fs-4 fw-bold">${monthlyNewCount}<span class="fs-6 text-muted ms-1">명</span></div>
                </div>
            </div>
            <div class="card-footer bg-transparent border-0">
                <a href="${pageContext.request.contextPath}/student/insertForm.do"
                   class="btn btn-sm btn-outline-success w-100">학생 등록</a>
            </div>
        </div>
    </div>

    <!-- 공지사항 수 -->
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm h-100">
            <div class="card-body d-flex align-items-center">
                <div class="rounded-circle bg-warning bg-opacity-10 p-3 me-3">
                    <i class="bi bi-megaphone fs-3 text-warning"></i>
                </div>
                <div>
                    <div class="text-muted small">공지사항</div>
                    <div class="fs-4 fw-bold">${totalNoticeCount}<span class="fs-6 text-muted ms-1">건</span></div>
                </div>
            </div>
            <div class="card-footer bg-transparent border-0">
                <a href="${pageContext.request.contextPath}/notice/list.do"
                   class="btn btn-sm btn-outline-warning w-100">공지 목록 보기</a>
            </div>
        </div>
    </div>

    <!-- 오늘 출석률 -->
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm h-100">
            <div class="card-body d-flex align-items-center">
                <div class="rounded-circle bg-info bg-opacity-10 p-3 me-3">
                    <i class="bi bi-calendar-check fs-3 text-info"></i>
                </div>
                <div>
                    <div class="text-muted small">오늘 출석률</div>
                    <div class="fs-4 fw-bold">${todayAttendRate}<span class="fs-6 text-muted ms-1">%</span></div>
                </div>
            </div>
            <div class="card-footer bg-transparent border-0">
                <a href="${pageContext.request.contextPath}/attend/list.do"
                   class="btn btn-sm btn-outline-info w-100">출결 현황 보기</a>
            </div>
        </div>
    </div>

</div>

<!-- ── 학과별 학생 현황 + 최근 등록 학생 ── -->
<div class="row g-3 mb-4">

    <!-- 학과별 인원 -->
    <div class="col-lg-5">
        <div class="card border-0 shadow-sm">
            <div class="card-header bg-white fw-bold">
                <i class="bi bi-bar-chart me-2 text-primary"></i>학과별 학생 현황
            </div>
            <div class="card-body p-0">
                <table class="table table-sm table-hover mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="ps-3">학과</th>
                            <th class="text-center">인원</th>
                            <th>비율</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty deptStatList}">
                                <c:forEach var="dept" items="${deptStatList}">
                                <tr>
                                    <td class="ps-3">${dept.deptName}</td>
                                    <td class="text-center fw-bold">${dept.studentCount}명</td>
                                    <td style="width:40%">
                                        <div class="progress" style="height:14px;">
                                            <div class="progress-bar bg-primary"
                                                 style="width:${dept.ratio}%">${dept.ratio}%</div>
                                        </div>
                                    </td>
                                </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="3" class="text-center text-muted py-3">데이터 없음</td></tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- 최근 등록 학생 -->
    <div class="col-lg-7">
        <div class="card border-0 shadow-sm">
            <div class="card-header bg-white fw-bold">
                <i class="bi bi-clock-history me-2 text-success"></i>최근 등록 학생 (5명)
            </div>
            <div class="card-body p-0">
                <table class="table table-sm table-hover mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="ps-3">학번</th>
                            <th>이름</th>
                            <th>학과</th>
                            <th>등록일</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty recentStudentList}">
                                <c:forEach var="stu" items="${recentStudentList}">
                                <tr>
                                    <td class="ps-3">
                                        <a href="${pageContext.request.contextPath}/student/detail.do?studentId=${stu.studentId}"
                                           class="text-decoration-none">${stu.studentNum}</a>
                                    </td>
                                    <td>${stu.studentName}</td>
                                    <td>${stu.deptName}</td>
                                    <td>
                                        <fmt:formatDate value="${stu.regDate}" pattern="yyyy-MM-dd"/>
                                    </td>
                                </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="4" class="text-center text-muted py-3">데이터 없음</td></tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
            <div class="card-footer bg-transparent text-end">
                <a href="${pageContext.request.contextPath}/student/list.do"
                   class="btn btn-sm btn-outline-secondary">전체 보기</a>
            </div>
        </div>
    </div>

</div>
</sec:authorize>

<!-- ── 학생 전용 대시보드 ── -->
<sec:authorize access="hasRole('ROLE_STUDENT')">
<div class="row g-3">
    <div class="col-12">
        <div class="alert alert-primary d-flex align-items-center">
            <i class="bi bi-info-circle-fill me-2 fs-5"></i>
            <span>
                <sec:authentication property="principal.username"/> 님, 환영합니다!
                좌측 메뉴에서 성적 조회 및 출결 현황을 확인하세요.
            </span>
        </div>
    </div>
    <div class="col-sm-6">
        <div class="card border-0 shadow-sm">
            <div class="card-body text-center py-4">
                <i class="bi bi-bar-chart fs-1 text-primary mb-2 d-block"></i>
                <h6>내 성적 조회</h6>
                <a href="${pageContext.request.contextPath}/grade/list.do"
                   class="btn btn-primary btn-sm mt-2">바로가기</a>
            </div>
        </div>
    </div>
    <div class="col-sm-6">
        <div class="card border-0 shadow-sm">
            <div class="card-body text-center py-4">
                <i class="bi bi-calendar-check fs-1 text-success mb-2 d-block"></i>
                <h6>출결 현황 조회</h6>
                <a href="${pageContext.request.contextPath}/attend/list.do"
                   class="btn btn-success btn-sm mt-2">바로가기</a>
            </div>
        </div>
    </div>
</div>
</sec:authorize>
