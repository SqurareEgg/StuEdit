package com.stuedit.service;

import java.util.List;

import com.stuedit.vo.AttendVO;
import com.stuedit.vo.ChatLogVO;
import com.stuedit.vo.GradeVO;
import com.stuedit.vo.StudentVO;

/**
 * AI 챗봇 서비스 인터페이스
 * Gemini API 호출 및 대화 이력 관리
 */
public interface ChatService {

    /**
     * 사용자 메시지를 Gemini API로 전송하고 응답을 반환
     * student/gradeList/attendStat이 있으면 학생 데이터를 system prompt에 주입
     *
     * @param userMessage 사용자 입력 메시지
     * @param userId      현재 로그인 사용자 ID
     * @param student     로그인 학생 정보 (관리자면 null)
     * @param gradeList   학생 성적 목록 (관리자면 null)
     * @param attendStat  출결 집계 통계 (관리자면 null)
     * @return Gemini AI 응답 텍스트
     * @throws Exception 처리 중 오류
     */
    String sendMessage(String userMessage, int userId, StudentVO student, List<GradeVO> gradeList, AttendVO attendStat) throws Exception;

    /**
     * 사용자의 최근 대화 이력 조회 (최대 20건)
     *
     * @param userId 조회할 사용자 ID
     * @return 대화 로그 목록 (오래된 순)
     * @throws Exception 처리 중 오류
     */
    List<ChatLogVO> getChatHistory(int userId) throws Exception;
}
