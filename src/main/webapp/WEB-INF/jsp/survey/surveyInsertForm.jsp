<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-plus-circle me-2"></i>설문조사 등록</h4>
    <a href="${pageContext.request.contextPath}/survey/list.do" class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-list me-1"></i>목록
    </a>
</div>

<c:if test="${not empty errorMsg}">
    <div class="alert alert-danger alert-dismissible fade show">
        <i class="bi bi-exclamation-triangle me-1"></i>${errorMsg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<form id="surveyInsertForm"
      action="${pageContext.request.contextPath}/survey/insert.do"
      method="post"
      onsubmit="return validateAndPrepare()">
    <sec:csrfInput/>

    <!-- 설문 기본 정보 -->
    <div class="card border-0 shadow-sm mb-3">
        <div class="card-header bg-white fw-semibold">
            <i class="bi bi-info-circle me-1 text-primary"></i>설문 기본 정보
        </div>
        <div class="card-body">
            <div class="mb-3">
                <label class="form-label fw-semibold">설문 제목 <span class="text-danger">*</span></label>
                <input type="text" name="title" class="form-control"
                       placeholder="설문 제목을 입력하세요" required maxlength="200">
            </div>
            <div class="mb-3">
                <label class="form-label fw-semibold">설문 설명</label>
                <textarea name="description" class="form-control" rows="3"
                          placeholder="설문에 대한 설명을 입력하세요 (선택)"></textarea>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label fw-semibold">시작일 <span class="text-danger">*</span></label>
                    <input type="date" name="startDate" class="form-control" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label fw-semibold">종료일 <span class="text-danger">*</span></label>
                    <input type="date" name="endDate" class="form-control" required>
                </div>
            </div>
        </div>
    </div>

    <!-- 문항 영역 -->
    <div class="card border-0 shadow-sm mb-3">
        <div class="card-header bg-white d-flex justify-content-between align-items-center">
            <span class="fw-semibold"><i class="bi bi-list-check me-1 text-primary"></i>설문 문항</span>
            <button type="button" class="btn btn-success btn-sm" onclick="addQuestion()">
                <i class="bi bi-plus-circle me-1"></i>문항 추가
            </button>
        </div>
        <div class="card-body">
            <div id="questionContainer">
                <!-- 문항이 동적으로 추가됩니다 -->
                <div class="text-center text-muted py-3" id="noQuestionMsg">
                    <i class="bi bi-arrow-up me-1"></i>문항 추가 버튼을 클릭하여 문항을 추가하세요.
                </div>
            </div>
        </div>
    </div>

    <div class="d-flex justify-content-center gap-2">
        <button type="submit" class="btn btn-primary px-5">
            <i class="bi bi-save me-1"></i>등록
        </button>
        <a href="${pageContext.request.contextPath}/survey/list.do"
           class="btn btn-outline-secondary px-4">취소</a>
    </div>
</form>

<script>
var questionCount = 0;

function addQuestion() {
    var container = document.getElementById('questionContainer');
    var noMsg = document.getElementById('noQuestionMsg');
    if (noMsg) noMsg.style.display = 'none';

    var qIdx = questionCount;
    var div = document.createElement('div');
    div.className = 'border rounded p-3 mb-3 bg-light';
    div.id = 'question_' + qIdx;
    div.innerHTML =
        '<div class="d-flex justify-content-between align-items-center mb-2">' +
            '<strong class="text-primary"><i class="bi bi-question-circle me-1"></i>문항 ' + (qIdx + 1) + '</strong>' +
            '<button type="button" class="btn btn-outline-danger btn-sm" onclick="removeQuestion(' + qIdx + ')">' +
                '<i class="bi bi-trash"></i>' +
            '</button>' +
        '</div>' +
        '<div class="mb-2">' +
            '<label class="form-label small fw-semibold">문항 내용 <span class="text-danger">*</span></label>' +
            '<input type="text" name="questions[' + qIdx + '].questionText"' +
                   ' class="form-control form-control-sm" placeholder="문항을 입력하세요" required>' +
        '</div>' +
        '<div class="mb-2">' +
            '<label class="form-label small fw-semibold">문항 유형</label>' +
            '<select name="questions[' + qIdx + '].questionType" class="form-select form-select-sm"' +
                    ' onchange="toggleOptions(this, ' + qIdx + ')">' +
                '<option value="single">단일선택 (radio)</option>' +
                '<option value="multi">복수선택 (checkbox)</option>' +
                '<option value="text">주관식 (textarea)</option>' +
            '</select>' +
        '</div>' +
        '<div id="optionArea_' + qIdx + '">' +
            '<div class="d-flex justify-content-between align-items-center mb-1">' +
                '<label class="form-label small fw-semibold mb-0">선택지</label>' +
                '<button type="button" class="btn btn-outline-secondary btn-sm" onclick="addOption(' + qIdx + ')">' +
                    '<i class="bi bi-plus me-1"></i>선택지 추가' +
                '</button>' +
            '</div>' +
            '<div id="options_' + qIdx + '">' +
                '<div class="text-muted small">선택지를 추가하세요.</div>' +
            '</div>' +
        '</div>';

    container.appendChild(div);
    questionCount++;
    // 첫 번째 선택지 자동 추가
    addOption(qIdx);
}

function removeQuestion(qIdx) {
    var el = document.getElementById('question_' + qIdx);
    if (el) el.remove();
    // 번호 갱신
    var items = document.querySelectorAll('[id^="question_"]');
    if (items.length === 0) {
        var noMsg = document.getElementById('noQuestionMsg');
        if (noMsg) noMsg.style.display = '';
    }
}

function toggleOptions(sel, qIdx) {
    var optionArea = document.getElementById('optionArea_' + qIdx);
    if (sel.value === 'text') {
        optionArea.style.display = 'none';
    } else {
        optionArea.style.display = '';
    }
}

var optionCounts = {};

function addOption(qIdx) {
    if (!optionCounts[qIdx]) optionCounts[qIdx] = 0;
    var oIdx = optionCounts[qIdx];
    var container = document.getElementById('options_' + qIdx);

    // 안내 텍스트 제거
    var firstChild = container.firstChild;
    if (firstChild && firstChild.className && firstChild.className.indexOf('text-muted') >= 0) {
        container.innerHTML = '';
    }

    var div = document.createElement('div');
    div.className = 'input-group input-group-sm mb-1';
    div.id = 'option_' + qIdx + '_' + oIdx;
    div.innerHTML =
        '<span class="input-group-text">' + (oIdx + 1) + '</span>' +
        '<input type="text" name="questions[' + qIdx + '].options[' + oIdx + '].optionText"' +
               ' class="form-control" placeholder="선택지 내용을 입력하세요">' +
        '<button type="button" class="btn btn-outline-danger"' +
                ' onclick="removeOption(\'' + qIdx + '_' + oIdx + '\')">' +
            '<i class="bi bi-x"></i>' +
        '</button>';
    container.appendChild(div);
    optionCounts[qIdx]++;
}

function removeOption(key) {
    var el = document.getElementById('option_' + key);
    if (el) el.remove();
}

function validateAndPrepare() {
    var questions = document.querySelectorAll('[id^="question_"]');
    if (questions.length === 0) {
        alert('최소 하나 이상의 문항을 추가해주세요.');
        return false;
    }
    // 날짜 유효성 검사
    var startDate = document.querySelector('[name="startDate"]').value;
    var endDate   = document.querySelector('[name="endDate"]').value;
    if (startDate && endDate && startDate > endDate) {
        alert('종료일은 시작일보다 이후여야 합니다.');
        return false;
    }
    return true;
}
</script>
