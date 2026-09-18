<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- 페이지 제목 -->
<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-person-badge me-2"></i>마이페이지</h4>
    <a href="${pageContext.request.contextPath}/user/pwChange.do"
       class="btn btn-outline-warning btn-sm">
        <i class="bi bi-key me-1"></i>비밀번호 변경
    </a>
</div>

<!-- 알림 메시지 -->
<c:if test="${not empty resultMsg}">
    <div class="alert alert-success alert-dismissible fade show">
        <i class="bi bi-check-circle me-1"></i>${resultMsg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>
<c:if test="${not empty errorMsg}">
    <div class="alert alert-danger alert-dismissible fade show">
        <i class="bi bi-exclamation-triangle me-1"></i>${errorMsg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<div class="row g-4">

    <!-- ── 기본 정보 (읽기 전용) ── -->
    <div class="col-lg-4">
        <div class="card border-0 shadow-sm h-100">
            <div class="card-header bg-primary text-white fw-bold">
                <i class="bi bi-person-circle me-1"></i>기본 정보
            </div>
            <div class="card-body">
                <div class="text-center mb-3">
                    <div class="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center"
                         style="width:80px; height:80px;">
                        <i class="bi bi-person-fill text-primary" style="font-size:2.5rem;"></i>
                    </div>
                    <h5 class="mt-2 mb-0">${user.userName}</h5>
                    <span class="badge ${user.role == 'ROLE_ADMIN' ? 'bg-danger' : 'bg-primary'} mt-1">
                        ${user.role == 'ROLE_ADMIN' ? '관리자' : '학생'}
                    </span>
                </div>
                <table class="table table-sm table-borderless">
                    <tr>
                        <th class="text-muted small" style="width:40%">아이디</th>
                        <td class="fw-semibold">${user.loginId}</td>
                    </tr>
                    <tr>
                        <th class="text-muted small">권한</th>
                        <td>${user.role}</td>
                    </tr>
                    <tr>
                        <th class="text-muted small">등록일</th>
                        <td><fmt:formatDate value="${user.regDate}" pattern="yyyy-MM-dd"/></td>
                    </tr>
                    <tr>
                        <th class="text-muted small">마지막 로그인</th>
                        <td><fmt:formatDate value="${user.lastLoginDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <!-- ── 연락처·이메일 수정 폼 ── -->
    <div class="col-lg-8">
        <div class="card border-0 shadow-sm">
            <div class="card-header bg-white fw-bold">
                <i class="bi bi-pencil-square me-1 text-primary"></i>연락처·이메일 수정
            </div>
            <div class="card-body">
                <form id="mypageForm"
                      action="${pageContext.request.contextPath}/user/mypageUpdate.do"
                      method="post">
                    <sec:csrfInput/>
                    <input type="hidden" name="userId" value="${user.userId}">

                    <div class="mb-3">
                        <label class="form-label fw-semibold">연락처</label>
                        <input type="tel" name="phone" value="${user.phone}"
                               class="form-control" placeholder="010-0000-0000" maxlength="20">
                    </div>
                    <div class="mb-4">
                        <label class="form-label fw-semibold">이메일</label>
                        <input type="email" name="email" value="${user.email}"
                               class="form-control" placeholder="example@email.com" maxlength="100">
                    </div>
                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-save me-1"></i>저장
                        </button>
                        <a href="${pageContext.request.contextPath}/main.do"
                           class="btn btn-outline-secondary">취소</a>
                    </div>
                </form>
            </div>
        </div>

        <!-- 학생 연결 정보 (학생 권한인 경우) -->
        <c:if test="${user.role == 'ROLE_STUDENT' and user.studentId > 0}">
        <div class="card border-0 shadow-sm mt-3">
            <div class="card-header bg-white fw-bold">
                <i class="bi bi-mortarboard me-1 text-success"></i>학생 정보 바로가기
            </div>
            <div class="card-body d-flex gap-3">
                <a href="${pageContext.request.contextPath}/grade/list.do?studentId=${user.studentId}"
                   class="btn btn-outline-primary btn-sm">
                    <i class="bi bi-bar-chart me-1"></i>내 성적 조회
                </a>
                <a href="${pageContext.request.contextPath}/attend/list.do?studentId=${user.studentId}"
                   class="btn btn-outline-success btn-sm">
                    <i class="bi bi-calendar-check me-1"></i>출결 현황
                </a>
            </div>
        </div>
        </c:if>
    </div>

</div>
