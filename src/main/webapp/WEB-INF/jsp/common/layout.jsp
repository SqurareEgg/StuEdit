<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"   uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>StuEdit - 학생정보 관리시스템</title>
    <!-- Spring Security CSRF 토큰 (Ajax 요청용) -->
    <sec:csrfMetaTags/>

    <!-- Bootstrap 5 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <!-- 공통 CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>

<!-- ── 헤더 ── -->
<tiles:insertAttribute name="header"/>

<div class="container-fluid">
    <div class="row">

        <!-- ── 사이드바 ── -->
        <tiles:insertAttribute name="sidebar"/>

        <!-- ── 본문 영역 ── -->
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <tiles:insertAttribute name="body"/>
        </main>

    </div>
</div>

<!-- ── 푸터 ── -->
<tiles:insertAttribute name="footer"/>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- 공통 JS -->
<script src="${pageContext.request.contextPath}/resources/js/common.js"></script>
</body>
</html>
