<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- 페이지 제목 -->
<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-key me-2"></i>비밀번호 변경</h4>
    <a href="${pageContext.request.contextPath}/user/mypage.do"
       class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-person-badge me-1"></i>마이페이지
    </a>
</div>

<!-- 오류 메시지 -->
<c:if test="${not empty errorMsg}">
    <div class="alert alert-danger alert-dismissible fade show">
        <i class="bi bi-exclamation-triangle me-1"></i>${errorMsg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card border-0 shadow-sm">
            <div class="card-header bg-white fw-bold">
                <i class="bi bi-shield-lock me-1 text-warning"></i>비밀번호 변경
            </div>
            <div class="card-body">
                <form id="pwChangeForm"
                      action="${pageContext.request.contextPath}/user/pwChange.do"
                      method="post"
                      onsubmit="return validatePwForm()">
                    <sec:csrfInput/>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">현재 비밀번호 <span class="text-danger">*</span></label>
                        <input type="password" name="currentPw" id="currentPw"
                               class="form-control" required placeholder="현재 비밀번호">
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-semibold">새 비밀번호 <span class="text-danger">*</span></label>
                        <input type="password" name="newPw" id="newPw"
                               class="form-control" required placeholder="8자 이상 입력">
                        <div class="form-text text-muted">영문, 숫자, 특수문자 조합 8자 이상 권장</div>
                    </div>
                    <div class="mb-4">
                        <label class="form-label fw-semibold">새 비밀번호 확인 <span class="text-danger">*</span></label>
                        <input type="password" name="newPwConfirm" id="newPwConfirm"
                               class="form-control" required placeholder="새 비밀번호 재입력">
                        <div id="pwMatchMsg" class="form-text"></div>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-warning fw-bold">
                            <i class="bi bi-check-lg me-1"></i>변경하기
                        </button>
                        <a href="${pageContext.request.contextPath}/user/mypage.do"
                           class="btn btn-outline-secondary">취소</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
/* 새 비밀번호 일치 실시간 확인 */
document.getElementById('newPwConfirm').addEventListener('input', function () {
    var newPw  = document.getElementById('newPw').value;
    var msg    = document.getElementById('pwMatchMsg');
    if (this.value === '') {
        msg.textContent = '';
    } else if (this.value === newPw) {
        msg.textContent = '비밀번호가 일치합니다.';
        msg.className   = 'form-text text-success';
    } else {
        msg.textContent = '비밀번호가 일치하지 않습니다.';
        msg.className   = 'form-text text-danger';
    }
});

function validatePwForm() {
    var newPw      = document.getElementById('newPw').value;
    var newPwConfirm = document.getElementById('newPwConfirm').value;
    if (newPw !== newPwConfirm) {
        alert('새 비밀번호와 확인 비밀번호가 일치하지 않습니다.');
        return false;
    }
    if (newPw.length < 8) {
        alert('새 비밀번호는 8자 이상이어야 합니다.');
        return false;
    }
    return true;
}
</script>
