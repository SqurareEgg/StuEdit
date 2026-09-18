<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <div class="container-fluid">
        <!-- 브랜드 로고 -->
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/main.do">
            <i class="bi bi-mortarboard-fill me-1"></i>StuEdit
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarTop">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarTop">
            <ul class="navbar-nav ms-auto align-items-center">

                <!-- 로그인 상태에 따라 메뉴 분기 -->
                <sec:authorize access="isAuthenticated()">
                    <!-- 로그인 사용자 이름 표시 -->
                    <li class="nav-item me-2">
                        <span class="navbar-text text-white">
                            <i class="bi bi-person-circle me-1"></i>
                            <sec:authentication property="principal.username"/> 님
                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                                <span class="badge bg-danger ms-1">관리자</span>
                            </sec:authorize>
                            <sec:authorize access="hasRole('ROLE_STUDENT')">
                                <span class="badge bg-primary ms-1">학생</span>
                            </sec:authorize>
                        </span>
                    </li>
                    <!-- 마이페이지 -->
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/user/mypage.do">
                            <i class="bi bi-gear me-1"></i>마이페이지
                        </a>
                    </li>
                    <!-- 로그아웃 -->
                    <li class="nav-item">
                        <form action="${pageContext.request.contextPath}/user/logout.do"
                              method="post" class="d-inline">
                            <sec:csrfInput/>
                            <button type="submit" class="btn btn-outline-light btn-sm ms-2">
                                <i class="bi bi-box-arrow-right me-1"></i>로그아웃
                            </button>
                        </form>
                    </li>
                </sec:authorize>

                <!-- 비로그인 상태 -->
                <sec:authorize access="!isAuthenticated()">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/user/login.do">
                            <i class="bi bi-box-arrow-in-right me-1"></i>로그인
                        </a>
                    </li>
                </sec:authorize>

            </ul>
        </div>
    </div>
</nav>
<!-- 헤더 높이만큼 여백 -->
<div style="height: 56px;"></div>
