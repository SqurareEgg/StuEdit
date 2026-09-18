package com.stuedit.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.stuedit.vo.NoticeVO;

/**
 * 공지사항 DAO
 */
@Repository("noticeDAO")
public class NoticeDAO extends AbstractBaseDAO {

    private static final Logger logger = LogManager.getLogger(NoticeDAO.class);

    /** 공지사항 목록 조회 */
    public List<NoticeVO> selectNoticeList(NoticeVO vo) {
        logger.debug("selectNoticeList - keyword: {}, page: {}", vo.getSearchKeyword(), vo.getPageIndex());
        return getSqlSession().selectList("noticeSQL.selectNoticeList", vo);
    }

    /** 공지사항 전체 건수 */
    public int selectNoticeCount(NoticeVO vo) {
        return getSqlSession().selectOne("noticeSQL.selectNoticeCount", vo);
    }

    /** 공지사항 상세 조회 */
    public NoticeVO selectNotice(int noticeId) {
        return getSqlSession().selectOne("noticeSQL.selectNotice", noticeId);
    }

    /** 조회수 증가 */
    public void updateViewCount(int noticeId) {
        getSqlSession().update("noticeSQL.updateViewCount", noticeId);
    }

    /** 공지사항 등록 */
    public void insertNotice(NoticeVO vo) {
        getSqlSession().insert("noticeSQL.insertNotice", vo);
    }

    /** 공지사항 수정 */
    public void updateNotice(NoticeVO vo) {
        getSqlSession().update("noticeSQL.updateNotice", vo);
    }

    /** 공지사항 소프트삭제 */
    public void deleteNotice(int noticeId) {
        getSqlSession().update("noticeSQL.deleteNotice", noticeId);
    }

    /** 대시보드: 전체 공지사항 수 */
    public int selectTotalNoticeCount() {
        return getSqlSession().selectOne("noticeSQL.selectTotalNoticeCount");
    }

    /** 이전 글 */
    public NoticeVO selectPrevNotice(int noticeId) {
        return getSqlSession().selectOne("noticeSQL.selectPrevNotice", noticeId);
    }

    /** 다음 글 */
    public NoticeVO selectNextNotice(int noticeId) {
        return getSqlSession().selectOne("noticeSQL.selectNextNotice", noticeId);
    }
}
