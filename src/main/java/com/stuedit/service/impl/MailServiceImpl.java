package com.stuedit.service.impl;

import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.stuedit.service.MailService;
import com.stuedit.vo.StudentVO;

/**
 * 이메일 발송 서비스 구현체
 */
@Service("mailService")
public class MailServiceImpl implements MailService {

    private static final Logger logger = LogManager.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    /** 발신자 이메일 (context-mail.xml mail.username 과 동일) */
    @Value("${mail.username:your-email@gmail.com}")
    private String fromEmail;

    /** 발신자 표시 이름 */
    @Value("${mail.fromName:StuEdit 학생관리시스템}")
    private String fromName;

    /* ===================================================================
     * 학생 등록 완료 안내 메일
     * =================================================================== */
    @Async
    @Override
    public void sendStudentRegistrationMail(StudentVO studentVO) throws Exception {
        if (studentVO.getEmail() == null || studentVO.getEmail().trim().isEmpty()) {
            logger.warn("학생 등록 메일 발송 스킵 - 이메일 없음, studentNum: {}", studentVO.getStudentNum());
            return;
        }

        String subject = "[StuEdit] 학생 등록 완료 안내";
        String html = buildRegistrationHtml(studentVO);

        try {
            sendHtmlMail(studentVO.getEmail(), subject, html);
            logger.info("학생 등록 메일 발송 완료 - to: {}, studentNum: {}",
                    studentVO.getEmail(), studentVO.getStudentNum());
        } catch (Exception e) {
            // 메일 발송 실패가 학생 등록 트랜잭션에 영향을 주지 않도록 예외를 로그만 기록
            logger.error("학생 등록 메일 발송 실패 - to: {}", studentVO.getEmail(), e);
        }
    }

    /* ===================================================================
     * 공지사항 알림 메일
     * =================================================================== */
    @Async
    @Override
    public void sendNoticeMail(String to, String title, String content) throws Exception {
        if (to == null || to.trim().isEmpty()) {
            logger.warn("공지 메일 발송 스킵 - 이메일 없음");
            return;
        }

        String subject = "[StuEdit 공지] " + title;
        String html = buildNoticeHtml(title, content);

        try {
            sendHtmlMail(to, subject, html);
            logger.info("공지 메일 발송 완료 - to: {}, title: {}", to, title);
        } catch (Exception e) {
            logger.error("공지 메일 발송 실패 - to: {}", to, e);
        }
    }

    /* ===================================================================
     * 범용 HTML 메일 발송
     * =================================================================== */
    @Override
    public void sendHtmlMail(String to, String subject, String html) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail, fromName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true); // true = HTML 모드

        mailSender.send(message);
        logger.debug("메일 발송 - to: {}, subject: {}", to, subject);
    }

    /* ===================================================================
     * HTML 템플릿 빌더
     * =================================================================== */

    /** 학생 등록 완료 안내 HTML */
    private String buildRegistrationHtml(StudentVO vo) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>")
          .append("<html lang='ko'><head><meta charset='UTF-8'></head><body>")
          .append("<div style='max-width:600px;margin:0 auto;font-family:\"Noto Sans KR\",Arial,sans-serif;")
          .append("border:1px solid #dee2e6;border-radius:8px;overflow:hidden;'>")

          // 헤더
          .append("<div style='background:#1a3a6b;padding:28px 32px;'>")
          .append("<h1 style='margin:0;color:#fff;font-size:22px;'>StuEdit 학생관리시스템</h1>")
          .append("<p style='margin:6px 0 0;color:#aec6f0;font-size:14px;'>학생 등록 완료 안내</p>")
          .append("</div>")

          // 본문
          .append("<div style='padding:32px;background:#fff;'>")
          .append("<p style='font-size:16px;color:#333;'>안녕하세요, <strong>").append(escapeHtml(vo.getStudentName())).append("</strong> 학생.</p>")
          .append("<p style='color:#555;line-height:1.7;'>")
          .append("StuEdit 학생관리시스템에 성공적으로 등록되었습니다.<br>")
          .append("아래 등록 정보를 확인해 주세요.</p>")

          // 정보 테이블
          .append("<table style='width:100%;border-collapse:collapse;margin:20px 0;font-size:14px;'>")
          .append(infoRow("학번",  escapeHtml(vo.getStudentNum())))
          .append(infoRow("이름",  escapeHtml(vo.getStudentName())))
          .append(infoRow("학과",  escapeHtml(vo.getDeptName())))
          .append(infoRow("학년",  vo.getGrade() + "학년"))
          .append(infoRow("성별",  "M".equals(vo.getGender()) ? "남" : "여"))
          .append(infoRow("연락처", escapeHtml(vo.getPhone())))
          .append(infoRow("이메일", escapeHtml(vo.getEmail())))
          .append("</table>")

          .append("<p style='color:#888;font-size:13px;margin-top:24px;'>")
          .append("본 메일은 시스템에서 자동 발송된 메일입니다. 문의사항은 관리자에게 연락해 주세요.</p>")
          .append("</div>")

          // 푸터
          .append("<div style='background:#f8f9fa;padding:16px 32px;text-align:center;'>")
          .append("<p style='margin:0;color:#aaa;font-size:12px;'>")
          .append("&copy; 2025 StuEdit. All rights reserved.</p>")
          .append("</div>")
          .append("</div></body></html>");

        return sb.toString();
    }

    /** 공지사항 알림 HTML */
    private String buildNoticeHtml(String title, String content) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>")
          .append("<html lang='ko'><head><meta charset='UTF-8'></head><body>")
          .append("<div style='max-width:600px;margin:0 auto;font-family:\"Noto Sans KR\",Arial,sans-serif;")
          .append("border:1px solid #dee2e6;border-radius:8px;overflow:hidden;'>")

          .append("<div style='background:#1a3a6b;padding:28px 32px;'>")
          .append("<h1 style='margin:0;color:#fff;font-size:22px;'>StuEdit 공지사항</h1>")
          .append("</div>")

          .append("<div style='padding:32px;background:#fff;'>")
          .append("<h2 style='font-size:18px;color:#1a3a6b;border-bottom:2px solid #1a3a6b;padding-bottom:8px;'>")
          .append(escapeHtml(title)).append("</h2>")
          .append("<div style='color:#555;line-height:1.8;white-space:pre-wrap;'>")
          .append(escapeHtml(content)).append("</div>")
          .append("</div>")

          .append("<div style='background:#f8f9fa;padding:16px 32px;text-align:center;'>")
          .append("<p style='margin:0;color:#aaa;font-size:12px;'>")
          .append("&copy; 2025 StuEdit. All rights reserved.</p>")
          .append("</div>")
          .append("</div></body></html>");

        return sb.toString();
    }

    /** 테이블 행 생성 헬퍼 */
    private String infoRow(String label, String value) {
        return "<tr>"
             + "<td style='padding:10px 14px;background:#f0f4ff;color:#1a3a6b;font-weight:bold;"
             + "width:30%;border:1px solid #dee2e6;'>" + label + "</td>"
             + "<td style='padding:10px 14px;border:1px solid #dee2e6;color:#333;'>"
             + (value != null ? value : "-") + "</td>"
             + "</tr>";
    }

    /** 기본 HTML 이스케이프 */
    private String escapeHtml(String str) {
        if (str == null) return "";
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
}
