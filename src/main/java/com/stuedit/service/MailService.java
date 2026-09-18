package com.stuedit.service;

import com.stuedit.vo.StudentVO;

/**
 * 이메일 발송 서비스 인터페이스
 */
public interface MailService {

    /**
     * 학생 등록 완료 안내 메일 발송
     * @param studentVO 등록된 학생 정보 (이름, 학번, 학과, 이메일 등)
     */
    void sendStudentRegistrationMail(StudentVO studentVO) throws Exception;

    /**
     * 공지사항 등록 알림 메일 발송 (선택 구현)
     * @param to      수신자 이메일
     * @param title   공지 제목
     * @param content 공지 내용 요약
     */
    void sendNoticeMail(String to, String title, String content) throws Exception;

    /**
     * 범용 메일 발송
     * @param to      수신자 이메일
     * @param subject 제목
     * @param html    HTML 본문
     */
    void sendHtmlMail(String to, String subject, String html) throws Exception;
}
