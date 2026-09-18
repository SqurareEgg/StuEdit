package com.stuedit.service;

import java.util.List;

import com.stuedit.vo.NoticeVO;

/**
 * 공지사항 서비스 인터페이스
 */
public interface NoticeService {

    /** 공지사항 목록 조회 (페이징 + 검색) */
    List<NoticeVO> selectNoticeList(NoticeVO vo) throws Exception;

    /** 공지사항 전체 건수 */
    int selectNoticeCount(NoticeVO vo) throws Exception;

    /** 공지사항 상세 조회 */
    NoticeVO selectNotice(int noticeId) throws Exception;

    /** 조회수 증가 */
    void updateViewCount(int noticeId) throws Exception;

    /** 공지사항 등록 */
    void insertNotice(NoticeVO vo) throws Exception;

    /** 공지사항 수정 */
    void updateNotice(NoticeVO vo) throws Exception;

    /** 공지사항 삭제 (소프트삭제) */
    void deleteNotice(int noticeId) throws Exception;

    /** 대시보드: 전체 공지사항 수 */
    int selectTotalNoticeCount() throws Exception;

    /** 이전 글 */
    NoticeVO selectPrevNotice(int noticeId) throws Exception;

    /** 다음 글 */
    NoticeVO selectNextNotice(int noticeId) throws Exception;
}
