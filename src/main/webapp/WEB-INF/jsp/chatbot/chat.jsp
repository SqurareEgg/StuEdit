<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- CSRF 토큰 메타 태그 -->
<sec:csrfMetaTags/>

<style>
    /* ── 채팅 컨테이너 ── */
    #chatWindow {
        height: 500px;
        overflow-y: auto;
        background-color: #f8f9fa;
        border: 1px solid #dee2e6;
        border-radius: 0.5rem;
        padding: 1rem;
        display: flex;
        flex-direction: column;
        gap: 0.75rem;
    }

    /* ── 메시지 공통 ── */
    .chat-message {
        display: flex;
        flex-direction: column;
        max-width: 75%;
    }
    .chat-message.user-msg {
        align-self: flex-end;
        align-items: flex-end;
    }
    .chat-message.bot-msg {
        align-self: flex-start;
        align-items: flex-start;
    }

    /* ── 말풍선 ── */
    .bubble {
        padding: 0.65rem 1rem;
        border-radius: 1rem;
        word-break: break-word;
        white-space: pre-wrap;
        line-height: 1.5;
        font-size: 0.92rem;
        box-shadow: 0 1px 2px rgba(0,0,0,0.08);
    }
    .user-msg .bubble {
        background-color: #0d6efd;
        color: #ffffff;
        border-bottom-right-radius: 0.25rem;
    }
    .bot-msg .bubble {
        background-color: #ffffff;
        color: #212529;
        border-bottom-left-radius: 0.25rem;
        border: 1px solid #e0e0e0;
    }

    /* ── 타임스탬프 ── */
    .chat-time {
        font-size: 0.72rem;
        color: #6c757d;
        margin-top: 0.2rem;
    }

    /* ── 입력 영역 ── */
    #messageInput {
        resize: none;
        overflow-y: auto;
    }

    /* ── 로딩 인디케이터 (점 3개 바운스) ── */
    .loading-dots {
        display: flex;
        gap: 4px;
        padding: 0.65rem 1rem;
        background-color: #ffffff;
        border: 1px solid #e0e0e0;
        border-radius: 1rem;
        border-bottom-left-radius: 0.25rem;
        box-shadow: 0 1px 2px rgba(0,0,0,0.08);
    }
    .loading-dots span {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background-color: #adb5bd;
        animation: bounce 1.2s infinite ease-in-out;
        display: inline-block;
    }
    .loading-dots span:nth-child(1) { animation-delay: 0s; }
    .loading-dots span:nth-child(2) { animation-delay: 0.2s; }
    .loading-dots span:nth-child(3) { animation-delay: 0.4s; }
    @keyframes bounce {
        0%, 80%, 100% { transform: scale(0.6); opacity: 0.5; }
        40%            { transform: scale(1.0); opacity: 1.0; }
    }

    /* ── 봇 아이콘 ── */
    .bot-avatar {
        width: 28px;
        height: 28px;
        border-radius: 50%;
        background: linear-gradient(135deg, #6f42c1, #0d6efd);
        display: inline-flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 0.8rem;
        flex-shrink: 0;
        margin-bottom: 0.2rem;
    }
</style>

<!-- 페이지 헤더 -->
<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0">
        <i class="bi bi-robot me-2 text-primary"></i>AI 챗봇 상담
    </h4>
    <small class="text-muted">Gemini AI 기반 학습 도우미</small>
</div>

<!-- 학생 정보 배너 (학생 계정인 경우) -->
<c:if test="${not empty studentInfo}">
<div class="alert alert-info py-2 mb-3 d-flex align-items-center gap-2">
    <i class="bi bi-person-circle fs-5"></i>
    <span>
        <strong>${studentInfo.studentName}</strong> 학생
        (학번: ${studentInfo.studentNum} | ${studentInfo.deptName} ${studentInfo.grade}학년)
        의 성적 데이터를 기반으로 상담합니다.
    </span>
</div>
</c:if>

<!-- 채팅 카드 -->
<div class="card shadow-sm">
    <div class="card-body p-3">

        <!-- 채팅 창 -->
        <div id="chatWindow">

            <!-- 환영 메시지 -->
            <div class="chat-message bot-msg" id="welcomeMessage">
                <div class="d-flex align-items-start gap-2">
                    <span class="bot-avatar"><i class="bi bi-robot"></i></span>
                    <div>
                        <div class="bubble">
                            <c:choose>
                                <c:when test="${not empty studentInfo}">
                                    안녕하세요, <strong>${studentInfo.studentName}</strong> 학생! 저는 StuEdit AI 학습 도우미입니다.<br>
                                    학생님의 성적 데이터를 바탕으로 맞춤 상담을 제공합니다.<br>
                                    성적 분석, 학점 향상 전략, 졸업 요건 등 무엇이든 물어보세요.
                                </c:when>
                                <c:otherwise>
                                    안녕하세요! 저는 StuEdit AI 학습 도우미입니다.<br>
                                    학습, 성적, 출결 등 궁금한 사항을 자유롭게 물어보세요.
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <span class="chat-time">시스템 메시지</span>
                    </div>
                </div>
            </div>

            <!-- 대화 이력 출력 -->
            <c:forEach var="log" items="${chatHistory}">
                <!-- 사용자 메시지 -->
                <div class="chat-message user-msg">
                    <div class="bubble">${log.userMessage}</div>
                    <span class="chat-time">
                        <fmt:formatDate value="${log.regDate}" pattern="MM/dd HH:mm"/>
                    </span>
                </div>
                <!-- 봇 응답 -->
                <div class="chat-message bot-msg">
                    <div class="d-flex align-items-start gap-2">
                        <span class="bot-avatar"><i class="bi bi-robot"></i></span>
                        <div>
                            <div class="bubble">${log.botResponse}</div>
                            <span class="chat-time">
                                <fmt:formatDate value="${log.regDate}" pattern="MM/dd HH:mm"/>
                            </span>
                        </div>
                    </div>
                </div>
            </c:forEach>

        </div><!-- /#chatWindow -->

        <!-- 빠른 질문 버튼 (학생 계정) -->
        <c:if test="${not empty studentInfo}">
        <div class="mt-3 d-flex flex-wrap gap-2">
            <button class="btn btn-outline-secondary btn-sm quick-btn" type="button"
                    onclick="setQuickMsg('내 성적을 분석해줘')">
                <i class="bi bi-bar-chart-line me-1"></i>성적 분석
            </button>
            <button class="btn btn-outline-secondary btn-sm quick-btn" type="button"
                    onclick="setQuickMsg('성적이 낮은 과목을 어떻게 향상시킬 수 있을까?')">
                <i class="bi bi-arrow-up-circle me-1"></i>성적 향상 전략
            </button>
            <button class="btn btn-outline-secondary btn-sm quick-btn" type="button"
                    onclick="setQuickMsg('내 GPA로 졸업 요건을 충족할 수 있을까?')">
                <i class="bi bi-mortarboard me-1"></i>졸업 요건 확인
            </button>
            <button class="btn btn-outline-secondary btn-sm quick-btn" type="button"
                    onclick="setQuickMsg('수강신청할 때 어떤 과목을 추천해?')">
                <i class="bi bi-calendar2-plus me-1"></i>수강 추천
            </button>
            <button class="btn btn-outline-secondary btn-sm quick-btn" type="button"
                    onclick="setQuickMsg('내 출결 상태가 위험한지 분석해줘')">
                <i class="bi bi-calendar-check me-1"></i>출결 위험 분석
            </button>
        </div>
        </c:if>

        <!-- 입력 폼 -->
        <div class="mt-3">
            <div class="input-group">
                <textarea
                    id="messageInput"
                    class="form-control"
                    rows="3"
                    placeholder="메시지를 입력하세요... (Enter: 전송, Shift+Enter: 줄바꿈)"
                    style="border-radius: 0.5rem 0 0 0.5rem;"
                ></textarea>
                <button
                    id="sendBtn"
                    class="btn btn-primary"
                    type="button"
                    onclick="sendMessage()"
                    style="border-radius: 0 0.5rem 0.5rem 0;"
                >
                    <i class="bi bi-send-fill me-1"></i>전송
                </button>
            </div>
            <small class="text-muted mt-1 d-block">
                <i class="bi bi-info-circle me-1"></i>
                Enter 키로 전송, Shift+Enter로 줄바꿈합니다.
            </small>
        </div>

    </div>
</div>

<script>
    var contextPath = '${pageContext.request.contextPath}';

    /* ── CSRF 토큰 ── */
    function getCsrfToken() {
        var tokenMeta  = document.querySelector('meta[name="_csrf"]');
        var headerMeta = document.querySelector('meta[name="_csrf_header"]');
        return {
            token:  tokenMeta  ? tokenMeta.getAttribute('content')  : '',
            header: headerMeta ? headerMeta.getAttribute('content') : 'X-CSRF-TOKEN'
        };
    }

    /* ── 현재 시각 포맷 ── */
    function nowStr() {
        var d = new Date();
        var mm = String(d.getMonth() + 1).padStart(2, '0');
        var dd = String(d.getDate()).padStart(2, '0');
        var hh = String(d.getHours()).padStart(2, '0');
        var mi = String(d.getMinutes()).padStart(2, '0');
        return mm + '/' + dd + ' ' + hh + ':' + mi;
    }

    /* ── 메시지 추가 ── */
    function appendMessage(type, text) {
        var chatWindow = document.getElementById('chatWindow');
        var div = document.createElement('div');

        if (type === 'user') {
            div.className = 'chat-message user-msg';
            div.innerHTML =
                '<div class="bubble">' + escapeHtml(text) + '</div>' +
                '<span class="chat-time">' + nowStr() + '</span>';
        } else {
            div.className = 'chat-message bot-msg';
            div.innerHTML =
                '<div class="d-flex align-items-start gap-2">' +
                    '<span class="bot-avatar"><i class="bi bi-robot"></i></span>' +
                    '<div>' +
                        '<div class="bubble">' + escapeHtml(text) + '</div>' +
                        '<span class="chat-time">' + nowStr() + '</span>' +
                    '</div>' +
                '</div>';
        }

        chatWindow.appendChild(div);
        scrollToBottom();
    }

    /* ── 로딩 인디케이터 추가 ── */
    function appendLoadingMessage(loadingId) {
        var chatWindow = document.getElementById('chatWindow');
        var div = document.createElement('div');
        div.className = 'chat-message bot-msg';
        div.id = loadingId;
        div.innerHTML =
            '<div class="d-flex align-items-start gap-2">' +
                '<span class="bot-avatar"><i class="bi bi-robot"></i></span>' +
                '<div class="loading-dots">' +
                    '<span></span><span></span><span></span>' +
                '</div>' +
            '</div>';
        chatWindow.appendChild(div);
        scrollToBottom();
    }

    /* ── 로딩 인디케이터 제거 ── */
    function removeLoadingMessage(loadingId) {
        var el = document.getElementById(loadingId);
        if (el) { el.parentNode.removeChild(el); }
    }

    /* ── 맨 아래로 스크롤 ── */
    function scrollToBottom() {
        var chatWindow = document.getElementById('chatWindow');
        chatWindow.scrollTop = chatWindow.scrollHeight;
    }

    /* ── HTML 이스케이프 (XSS 방지) ── */
    function escapeHtml(text) {
        return text
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;')
            .replace(/\n/g, '<br>');
    }

    /* ── 메시지 전송 ── */
    function sendMessage() {
        var input   = document.getElementById('messageInput');
        var sendBtn = document.getElementById('sendBtn');
        var message = input.value.trim();

        if (!message) { return; }

        /* 사용자 메시지 즉시 표시 */
        appendMessage('user', message);
        input.value = '';
        input.style.height = 'auto';

        /* 버튼 비활성화 (중복 전송 방지) */
        sendBtn.disabled = true;
        sendBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>전송 중';

        /* 로딩 인디케이터 */
        var loadingId = 'loading-' + Date.now();
        appendLoadingMessage(loadingId);

        /* CSRF */
        var csrf = getCsrfToken();

        /* API 호출 */
        var headers = { 'Content-Type': 'application/x-www-form-urlencoded' };
        headers[csrf.header] = csrf.token;

        fetch(contextPath + '/chatbot/send.do', {
            method: 'POST',
            headers: headers,
            body: 'message=' + encodeURIComponent(message)
        })
        .then(function(r) {
            if (!r.ok) { throw new Error('HTTP ' + r.status); }
            return r.json();
        })
        .then(function(data) {
            removeLoadingMessage(loadingId);
            appendMessage('bot', data.response || '응답을 받을 수 없습니다.');
            scrollToBottom();
        })
        .catch(function(err) {
            removeLoadingMessage(loadingId);
            appendMessage('bot', '오류가 발생했습니다: ' + err.message);
        })
        .finally(function() {
            sendBtn.disabled = false;
            sendBtn.innerHTML = '<i class="bi bi-send-fill me-1"></i>전송';
            input.focus();
        });
    }

    /* ── 빠른 질문 버튼 ── */
    function setQuickMsg(text) {
        var input = document.getElementById('messageInput');
        input.value = text;
        input.focus();
        sendMessage();
    }

    /* ── Enter 키 전송 (Shift+Enter = 줄바꿈) ── */
    document.getElementById('messageInput').addEventListener('keydown', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });

    /* ── 페이지 로드 시 맨 아래로 스크롤 ── */
    window.addEventListener('DOMContentLoaded', function() {
        scrollToBottom();
        document.getElementById('messageInput').focus();
    });
</script>
