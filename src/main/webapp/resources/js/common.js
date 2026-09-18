/* StuEdit 공통 JavaScript */

/* 삭제 확인 다이얼로그 */
function confirmDelete(url, msg) {
    var message = msg || "정말 삭제하시겠습니까?";
    if (confirm(message)) {
        location.href = url;
    }
}

/* 폼 제출 전 필수 항목 검증 */
function validateForm(formId) {
    var form = document.getElementById(formId);
    if (!form) return true;
    var required = form.querySelectorAll("[required]");
    for (var i = 0; i < required.length; i++) {
        if (!required[i].value.trim()) {
            required[i].focus();
            alert("필수 항목을 모두 입력해주세요.");
            return false;
        }
    }
    return true;
}

/* 페이지 로드 시 알림 메시지 자동 제거 (3초 후) */
document.addEventListener("DOMContentLoaded", function () {
    var alerts = document.querySelectorAll(".alert-dismissible");
    alerts.forEach(function (el) {
        setTimeout(function () {
            el.classList.remove("show");
            el.classList.add("fade");
        }, 3000);
    });
});
