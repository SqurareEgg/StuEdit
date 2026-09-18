<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-bar-chart me-2"></i>설문 결과</h4>
    <a href="${pageContext.request.contextPath}/survey/list.do" class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-list me-1"></i>목록
    </a>
</div>

<c:if test="${not empty resultMsg}">
    <div class="alert alert-success alert-dismissible fade show">
        <i class="bi bi-check-circle me-1"></i>${resultMsg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<!-- 설문 헤더 -->
<div class="card border-0 shadow-sm mb-4">
    <div class="card-body">
        <h5 class="card-title fw-bold text-primary">
            <i class="bi bi-clipboard me-2"></i>${survey.title}
        </h5>
        <c:if test="${not empty survey.description}">
            <p class="card-text text-muted">${survey.description}</p>
        </c:if>
        <div class="d-flex gap-3 small text-muted">
            <span>
                <i class="bi bi-calendar-range me-1"></i>
                <fmt:formatDate value="${survey.startDate}" pattern="yyyy-MM-dd"/>
                ~
                <fmt:formatDate value="${survey.endDate}" pattern="yyyy-MM-dd"/>
            </span>
            <span>
                <c:choose>
                    <c:when test="${survey.status == '진행중'}">
                        <span class="badge bg-primary">${survey.status}</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge bg-secondary">${survey.status}</span>
                    </c:otherwise>
                </c:choose>
            </span>
            <span>
                <i class="bi bi-people me-1"></i>
                총 <strong class="text-primary">${totalResponseCount}</strong>명 참여
            </span>
        </div>
    </div>
</div>

<!-- 문항별 결과 -->
<c:choose>
    <c:when test="${not empty survey.questions}">
        <c:forEach var="q" items="${survey.questions}" varStatus="qst">
        <div class="card border-0 shadow-sm mb-3">
            <div class="card-header bg-light">
                <strong class="text-dark">
                    Q${qst.index + 1}. ${q.questionText}
                </strong>
                <c:choose>
                    <c:when test="${q.questionType == 'single'}">
                        <span class="badge bg-info ms-2">단일선택</span>
                    </c:when>
                    <c:when test="${q.questionType == 'multi'}">
                        <span class="badge bg-warning text-dark ms-2">복수선택</span>
                    </c:when>
                    <c:when test="${q.questionType == 'text'}">
                        <span class="badge bg-success ms-2">주관식</span>
                    </c:when>
                </c:choose>
            </div>
            <div class="card-body">
                <c:choose>
                    <%-- 선택형: 막대 그래프 --%>
                    <c:when test="${q.questionType == 'single' or q.questionType == 'multi'}">
                        <%-- 해당 문항의 총 응답 수 계산 --%>
                        <c:set var="questionTotal" value="0"/>
                        <c:forEach var="o" items="${q.options}">
                            <c:set var="questionTotal" value="${questionTotal + o.responseCount}"/>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${not empty q.options}">
                                <c:forEach var="o" items="${q.options}">
                                    <c:set var="percent" value="0"/>
                                    <c:if test="${questionTotal > 0}">
                                        <c:set var="percent" value="${o.responseCount * 100 / questionTotal}"/>
                                    </c:if>
                                    <div class="mb-3">
                                        <div class="d-flex justify-content-between mb-1">
                                            <span class="small fw-semibold">${o.optionText}</span>
                                            <span class="small text-muted">
                                                ${o.responseCount}명
                                                (<fmt:formatNumber value="${percent}" maxFractionDigits="1"/>%)
                                            </span>
                                        </div>
                                        <div class="progress" style="height: 22px;">
                                            <div class="progress-bar bg-primary progress-bar-striped"
                                                 role="progressbar"
                                                 style="width: ${percent}%"
                                                 aria-valuenow="${percent}"
                                                 aria-valuemin="0"
                                                 aria-valuemax="100">
                                                <c:if test="${percent >= 10}">
                                                    <fmt:formatNumber value="${percent}" maxFractionDigits="1"/>%
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                                <div class="text-end small text-muted mt-1">
                                    총 응답 수: <strong>${questionTotal}</strong>개
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted small mb-0">선택지 데이터가 없습니다.</p>
                            </c:otherwise>
                        </c:choose>
                    </c:when>

                    <%-- 주관식: 텍스트 안내 --%>
                    <c:when test="${q.questionType == 'text'}">
                        <div class="alert alert-light border mb-0">
                            <i class="bi bi-pencil me-1 text-muted"></i>
                            <span class="text-muted">주관식 응답은 개별 통계 집계에서 제외됩니다.</span>
                        </div>
                    </c:when>
                </c:choose>
            </div>
        </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info text-center">
            <i class="bi bi-info-circle me-1"></i>등록된 문항이 없습니다.
        </div>
    </c:otherwise>
</c:choose>

<div class="d-flex justify-content-center mt-3">
    <a href="${pageContext.request.contextPath}/survey/list.do"
       class="btn btn-outline-secondary px-4">
        <i class="bi bi-list me-1"></i>목록으로
    </a>
</div>
