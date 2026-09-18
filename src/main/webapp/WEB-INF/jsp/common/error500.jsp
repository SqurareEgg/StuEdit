<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>500 - 서버 오류</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
<div class="container text-center py-5 mt-5">
    <i class="bi bi-exclamation-octagon text-danger" style="font-size: 5rem;"></i>
    <h1 class="display-4 fw-bold mt-3">500</h1>
    <h4 class="text-muted">서버 내부 오류가 발생했습니다.</h4>
    <p class="text-muted mt-2">잠시 후 다시 시도하거나 관리자에게 문의하세요.</p>
    <a href="${pageContext.request.contextPath}/main.do" class="btn btn-danger mt-3">
        <i class="bi bi-house me-1"></i>메인으로 돌아가기
    </a>
</div>
</body>
</html>
