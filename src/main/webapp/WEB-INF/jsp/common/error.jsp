<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="text-center py-5">
    <i class="bi bi-exclamation-triangle-fill text-danger" style="font-size: 4rem;"></i>
    <h3 class="mt-3">오류가 발생했습니다</h3>
    <p class="text-muted">
        <c:choose>
            <c:when test="${not empty errorMessage}">${errorMessage}</c:when>
            <c:otherwise>처리 중 오류가 발생하였습니다. 관리자에게 문의하세요.</c:otherwise>
        </c:choose>
    </p>
    <a href="${pageContext.request.contextPath}/main.do" class="btn btn-primary mt-2">
        <i class="bi bi-house me-1"></i>메인으로 돌아가기
    </a>
</div>
