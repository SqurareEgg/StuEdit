<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인 - StuEdit</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        body { background: linear-gradient(135deg, #1a237e 0%, #0d47a1 100%); min-height: 100vh; }
        .login-card { border-radius: 1rem; box-shadow: 0 1rem 3rem rgba(0,0,0,.3); }
        .login-brand { font-size: 2rem; font-weight: 800; color: #1a237e; letter-spacing: -0.05em; }
    </style>
</head>
<body class="d-flex align-items-center justify-content-center">

<div class="card login-card" style="width: 100%; max-width: 420px;">
    <div class="card-body p-5">

        <!-- 브랜드 -->
        <div class="text-center mb-4">
            <i class="bi bi-mortarboard-fill text-primary" style="font-size: 3rem;"></i>
            <div class="login-brand mt-1">StuEdit</div>
            <div class="text-muted small">학생정보 관리시스템</div>
        </div>

        <!-- 오류 메시지 -->
        <c:if test="${not empty errorMsg}">
            <div class="alert alert-danger d-flex align-items-center py-2 mb-3">
                <i class="bi bi-exclamation-circle me-2"></i>
                <span class="small">${errorMsg}</span>
            </div>
        </c:if>

        <!-- 성공 메시지 (비밀번호 변경 후 리다이렉트) -->
        <c:if test="${not empty resultMsg}">
            <div class="alert alert-success d-flex align-items-center py-2 mb-3">
                <i class="bi bi-check-circle me-2"></i>
                <span class="small">${resultMsg}</span>
            </div>
        </c:if>

        <!-- 로그인 폼: Spring Security가 /user/loginProc.do POST 처리 -->
        <form action="${pageContext.request.contextPath}/user/loginProc.do" method="post">
            <sec:csrfInput/>

            <div class="mb-3">
                <label class="form-label fw-semibold">아이디</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-person"></i></span>
                    <input type="text" name="userId" class="form-control"
                           placeholder="아이디를 입력하세요" autofocus required>
                </div>
            </div>

            <div class="mb-4">
                <label class="form-label fw-semibold">비밀번호</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-lock"></i></span>
                    <input type="password" name="userPw" class="form-control"
                           placeholder="비밀번호를 입력하세요" required>
                </div>
            </div>

            <button type="submit" class="btn btn-primary w-100 py-2 fw-bold">
                <i class="bi bi-box-arrow-in-right me-1"></i>로그인
            </button>
        </form>

    </div>
    <div class="card-footer text-center text-muted small py-3 bg-light rounded-bottom">
        &copy; 2025 StuEdit &nbsp;|&nbsp; eGovFrame 4.3.1
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
