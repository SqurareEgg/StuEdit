<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-clipboard-check me-2"></i>설문 참여</h4>
    <a href="${pageContext.request.contextPath}/survey/list.do" class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-list me-1"></i>목록
    </a>
</div>

<c:if test="${not empty errorMsg}">
    <div class="alert alert-danger alert-dismissible fade show">
        <i class="bi bi-exclamation-triangle me-1"></i>${errorMsg}
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
        </div>
    </div>
</div>

<!-- 설문 참여 폼 -->
<form id="surveyParticipateForm"
      action="${pageContext.request.contextPath}/survey/participate.do"
      method="post"
      onsubmit="return validateSurveyForm()">
    <sec:csrfInput/>
    <input type="hidden" name="surveyId" value="${survey.surveyId}">

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
                        <%-- 단일 선택형 (radio) --%>
                        <c:when test="${q.questionType == 'single'}">
                            <c:forEach var="o" items="${q.options}" varStatus="ost">
                            <div class="form-check mb-2">
                                <input class="form-check-input survey-required"
                                       type="radio"
                                       name="answer_${q.questionId}"
                                       id="opt_${q.questionId}_${o.optionId}"
                                       value="${o.optionId}"
                                       data-question-id="${q.questionId}">
                                <label class="form-check-label" for="opt_${q.questionId}_${o.optionId}">
                                    ${o.optionText}
                                </label>
                            </div>
                            </c:forEach>
                            <c:if test="${empty q.options}">
                                <p class="text-muted small">선택지가 없습니다.</p>
                            </c:if>
                        </c:when>

                        <%-- 복수 선택형 (checkbox) --%>
                        <c:when test="${q.questionType == 'multi'}">
                            <c:forEach var="o" items="${q.options}" varStatus="ost">
                            <div class="form-check mb-2">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="answer_${q.questionId}"
                                       id="opt_${q.questionId}_${o.optionId}"
                                       value="${o.optionId}">
                                <label class="form-check-label" for="opt_${q.questionId}_${o.optionId}">
                                    ${o.optionText}
                                </label>
                            </div>
                            </c:forEach>
                            <c:if test="${empty q.options}">
                                <p class="text-muted small">선택지가 없습니다.</p>
                            </c:if>
                        </c:when>

                        <%-- 주관식 (textarea) --%>
                        <c:when test="${q.questionType == 'text'}">
                            <textarea class="form-control"
                                      name="answer_${q.questionId}"
                                      rows="3"
                                      placeholder="답변을 입력해주세요"></textarea>
                        </c:when>
                    </c:choose>
                </div>
            </div>
            </c:forEach>

            <div class="d-flex justify-content-center gap-2 mt-4">
                <button type="submit" class="btn btn-primary px-5">
                    <i class="bi bi-send me-1"></i>제출하기
                </button>
                <a href="${pageContext.request.contextPath}/survey/list.do"
                   class="btn btn-outline-secondary px-4">취소</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info text-center">
                <i class="bi bi-info-circle me-1"></i>등록된 문항이 없습니다.
            </div>
        </c:otherwise>
    </c:choose>
</form>

<script>
function validateSurveyForm() {
    // 단일선택(radio) 필수 응답 체크
    var radioGroups = {};
    document.querySelectorAll('input[type="radio"].survey-required').forEach(function(radio) {
        var qId = radio.dataset.questionId;
        if (!radioGroups[qId]) radioGroups[qId] = false;
        if (radio.checked) radioGroups[qId] = true;
    });
    for (var qId in radioGroups) {
        if (!radioGroups[qId]) {
            alert('모든 단일선택 문항에 답변해주세요.');
            return false;
        }
    }
    return true;
}
</script>
