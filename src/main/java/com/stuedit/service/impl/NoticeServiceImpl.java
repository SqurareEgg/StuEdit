package com.stuedit.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stuedit.dao.NoticeDAO;
import com.stuedit.service.NoticeService;
import com.stuedit.vo.NoticeVO;

/**
 * 공지사항 서비스 구현체
 */
@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {

    private static final Logger logger = LogManager.getLogger(NoticeServiceImpl.class);

    @Autowired
    private NoticeDAO noticeDAO;

    /** 공지사항 목록 조회 */
    @Override
    public List<NoticeVO> selectNoticeList(NoticeVO vo) throws Exception {
        if (vo.getPageSize()  == 0) vo.setPageSize(10);
        if (vo.getPageIndex() == 0) vo.setPageIndex(1);
        vo.setOffset((vo.getPageIndex() - 1) * vo.getPageSize());
        return noticeDAO.selectNoticeList(vo);
    }

    /** 공지사항 전체 건수 */
    @Override
    public int selectNoticeCount(NoticeVO vo) throws Exception {
        return noticeDAO.selectNoticeCount(vo);
    }

    /** 공지사항 상세 조회 */
    @Override
    public NoticeVO selectNotice(int noticeId) throws Exception {
        NoticeVO vo = noticeDAO.selectNotice(noticeId);
        if (vo == null) {
            throw new RuntimeException("존재하지 않는 공지사항입니다.");
        }
        return vo;
    }

    /** 조회수 증가 */
    @Override
    public void updateViewCount(int noticeId) throws Exception {
        noticeDAO.updateViewCount(noticeId);
    }

    /** 공지사항 등록 */
    @Override
    public void insertNotice(NoticeVO vo) throws Exception {
        if (vo.getImportantYn() == null) vo.setImportantYn("N");
        noticeDAO.insertNotice(vo);
        logger.info("공지사항 등록 완료 - title: {}, writer: {}", vo.getTitle(), vo.getWriter());
    }

    /** 공지사항 수정 */
    @Override
    public void updateNotice(NoticeVO vo) throws Exception {
        if (vo.getImportantYn() == null) vo.setImportantYn("N");
        noticeDAO.updateNotice(vo);
        logger.info("공지사항 수정 완료 - noticeId: {}", vo.getNoticeId());
    }

    /** 공지사항 소프트삭제 */
    @Override
    public void deleteNotice(int noticeId) throws Exception {
        noticeDAO.deleteNotice(noticeId);
        logger.info("공지사항 삭제 완료 - noticeId: {}", noticeId);
    }

    /** 대시보드: 전체 공지사항 수 */
    @Override
    public int selectTotalNoticeCount() throws Exception {
        return noticeDAO.selectTotalNoticeCount();
    }

    /** 이전 글 */
    @Override
    public NoticeVO selectPrevNotice(int noticeId) throws Exception {
        return noticeDAO.selectPrevNotice(noticeId);
    }

    /** 다음 글 */
    @Override
    public NoticeVO selectNextNotice(int noticeId) throws Exception {
        return noticeDAO.selectNextNotice(noticeId);
    }
}
