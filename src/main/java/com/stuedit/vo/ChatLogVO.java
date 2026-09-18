package com.stuedit.vo;

import java.util.Date;

/**
 * 챗봇 대화 기록 Value Object
 * TB_CHAT_LOG 테이블과 매핑
 */
public class ChatLogVO {

    private int    chatId;       // PK (AUTO_INCREMENT)
    private int    userId;       // 사용자 ID (TB_USER.user_id FK)
    private String userMessage;  // 사용자 입력 메시지
    private String botResponse;  // AI 봇 응답 메시지
    private Date   regDate;      // 대화 등록일시

    /* ── Getter / Setter ── */

    public int getChatId() { return chatId; }
    public void setChatId(int chatId) { this.chatId = chatId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserMessage() { return userMessage; }
    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }

    public String getBotResponse() { return botResponse; }
    public void setBotResponse(String botResponse) { this.botResponse = botResponse; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }
}
