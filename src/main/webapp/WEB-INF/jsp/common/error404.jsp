<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>404 - 페이지를 찾을 수 없습니다</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
<div class="container text-center py-5 mt-5">
    <i class="bi bi-file-earmark-x text-secondary" style="font-size: 5rem;"></i>
    <h1 class="display-4 fw-bold mt-3">404</h1>
    <h4 class="text-muted">요청하신 페이지를 찾을 수 없습니다.</h4>
    <p class="text-muted mt-2">URL을 다시 확인하거나 관리자에게 문의하세요.</p>
    <a href="${pageContext.request.contextPath}/main.do" class="btn btn-primary mt-3">
        <i class="bi bi-house me-1"></i>메인으로 돌아가기
    </a>
</div>
</body>
</html>
