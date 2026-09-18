<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<nav id="sidebar" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
    <div class="position-sticky pt-3">

        <!-- ── 공통 메뉴 (로그인 사용자 전체) ── -->
        <ul class="nav flex-column mb-3">
            <li class="nav-item">
                <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/main') ? 'active fw-bold' : 'text-dark'}"
                   href="${pageContext.request.contextPath}/main.do">
                    <i class="bi bi-house-door me-2"></i>대시보드
                </a>
            </li>
        </ul>

        <!-- ── 관리자 전용 메뉴 ── -->
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <h6 class="sidebar-heading px-3 mt-2 mb-1 text-muted text-uppercase small">
                <i class="bi bi-people me-1"></i>학생 관리
            </h6>
            <ul class="nav flex-column mb-3">
                <li class="nav-item">
                    <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/student/') ? 'active fw-bold' : 'text-dark'}"
                       href="${pageContext.request.contextPath}/student/list.do">
                        <i class="bi bi-list-ul me-2"></i>학생 목록
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-dark"
                       href="${pageContext.request.contextPath}/student/insertForm.do">
                        <i class="bi bi-person-plus me-2"></i>학생 등록
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-dark"
                       href="${pageContext.request.contextPath}/student/excelDownload.do">
                        <i class="bi bi-file-earmark-excel me-2"></i>엑셀 다운로드
                    </a>
                </li>
            </ul>
        </sec:authorize>

        <!-- ── 성적 관리 (공통) ── -->
        <h6 class="sidebar-heading px-3 mt-2 mb-1 text-muted text-uppercase small">
            <i class="bi bi-bar-chart me-1"></i>성적 관리
        </h6>
        <ul class="nav flex-column mb-3">
            <li class="nav-item">
                <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/grade/') ? 'active fw-bold' : 'text-dark'}"
                   href="${pageContext.request.contextPath}/grade/list.do">
                    <i class="bi bi-journal-text me-2"></i>성적 조회
                </a>
            </li>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <li class="nav-item">
                    <a class="nav-link text-dark"
                       href="${pageContext.request.contextPath}/grade/insertForm.do">
                        <i class="bi bi-pencil-square me-2"></i>성적 등록
                    </a>
                </li>
            </sec:authorize>
        </ul>

        <!-- ── 출결 관리 (공통) ── -->
        <h6 class="sidebar-heading px-3 mt-2 mb-1 text-muted text-uppercase small">
            <i class="bi bi-calendar-check me-1"></i>출결 관리
        </h6>
        <ul class="nav flex-column mb-3">
            <li class="nav-item">
                <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/attend/') ? 'active fw-bold' : 'text-dark'}"
                   href="${pageContext.request.contextPath}/attend/list.do">
                    <i class="bi bi-clipboard-check me-2"></i>출결 현황
                </a>
            </li>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <li class="nav-item">
                    <a class="nav-link text-dark"
                       href="${pageContext.request.contextPath}/attend/insertForm.do">
                        <i class="bi bi-plus-circle me-2"></i>출결 등록
                    </a>
                </li>
            </sec:authorize>
        </ul>

        <!-- ── 공지사항 (공통) ── -->
        <h6 class="sidebar-heading px-3 mt-2 mb-1 text-muted text-uppercase small">
            <i class="bi bi-megaphone me-1"></i>공지사항
        </h6>
        <ul class="nav flex-column mb-3">
            <li class="nav-item">
                <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/notice/') ? 'active fw-bold' : 'text-dark'}"
                   href="${pageContext.request.contextPath}/notice/list.do">
                    <i class="bi bi-bell me-2"></i>공지 목록
                </a>
            </li>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <li class="nav-item">
                    <a class="nav-link text-dark"
                       href="${pageContext.request.contextPath}/notice/insertForm.do">
                        <i class="bi bi-plus-circle me-2"></i>공지 등록
                    </a>
                </li>
            </sec:authorize>
        </ul>

        <!-- ── 1:1 문의 (공통) ── -->
        <h6 class="sidebar-heading px-3 mt-2 mb-1 text-muted text-uppercase small">
            <i class="bi bi-chat-dots me-1"></i>1:1 문의
        </h6>
        <ul class="nav flex-column mb-3">
            <li class="nav-item">
                <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/inquiry/') ? 'active fw-bold' : 'text-dark'}"
                   href="${pageContext.request.contextPath}/inquiry/list.do">
                    <i class="bi bi-list-ul me-2"></i>문의 목록
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-dark"
                   href="${pageContext.request.contextPath}/inquiry/insertForm.do">
                    <i class="bi bi-plus-circle me-2"></i>문의 등록
                </a>
            </li>
        </ul>

        <!-- ── 설문조사 (공통) ── -->
        <h6 class="sidebar-heading px-3 mt-2 mb-1 text-muted text-uppercase small">
            <i class="bi bi-clipboard-data me-1"></i>설문조사
        </h6>
        <ul class="nav flex-column mb-3">
            <li class="nav-item">
                <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/survey/') ? 'active fw-bold' : 'text-dark'}"
                   href="${pageContext.request.contextPath}/survey/list.do">
                    <i class="bi bi-bar-chart-line me-2"></i>설문 목록
                </a>
            </li>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <li class="nav-item">
                    <a class="nav-link text-dark"
                       href="${pageContext.request.contextPath}/survey/insertForm.do">
                        <i class="bi bi-plus-circle me-2"></i>설문 등록
                    </a>
                </li>
            </sec:authorize>
        </ul>

        <!-- ── 학사 일정 (공통) ── -->
        <h6 class="sidebar-heading px-3 mt-2 mb-1 text-muted text-uppercase small">
            <i class="bi bi-calendar3 me-1"></i>학사 일정
        </h6>
        <ul class="nav flex-column mb-3">
            <li class="nav-item">
                <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/schedule/') ? 'active fw-bold' : 'text-dark'}"
                   href="${pageContext.request.contextPath}/schedule/list.do">
                    <i class="bi bi-calendar-week me-2"></i>일정 조회
                </a>
            </li>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <li class="nav-item">
                    <a class="nav-link text-dark"
                       href="${pageContext.request.contextPath}/schedule/insertForm.do">
                        <i class="bi bi-plus-circle me-2"></i>일정 등록
                    </a>
                </li>
            </sec:authorize>
        </ul>

        <!-- ── AI 챗봇 (공통) ── -->
        <h6 class="sidebar-heading px-3 mt-2 mb-1 text-muted text-uppercase small">
            <i class="bi bi-robot me-1"></i>AI 챗봇
        </h6>
        <ul class="nav flex-column mb-3">
            <li class="nav-item">
                <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/chatbot/') ? 'active fw-bold' : 'text-dark'}"
                   href="${pageContext.request.contextPath}/chatbot/chat.do">
                    <i class="bi bi-chat-square-text me-2"></i>챗봇 상담
                </a>
            </li>
        </ul>

        <!-- ── 마이페이지 ── -->
        <h6 class="sidebar-heading px-3 mt-2 mb-1 text-muted text-uppercase small">
            <i class="bi bi-person me-1"></i>내 정보
        </h6>
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/user/mypage') ? 'active fw-bold' : 'text-dark'}"
                   href="${pageContext.request.contextPath}/user/mypage.do">
                    <i class="bi bi-person-badge me-2"></i>마이페이지
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-dark"
                   href="${pageContext.request.contextPath}/user/pwChange.do">
                    <i class="bi bi-key me-2"></i>비밀번호 변경
                </a>
            </li>
        </ul>

    </div>
</nav>
