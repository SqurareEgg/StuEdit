package com.stuedit.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.stuedit.vo.InquiryVO;

/**
 * 1:1 문의 DAO
 */
@Repository("inquiryDAO")
public class InquiryDAO extends AbstractBaseDAO {

    private static final Logger logger = LogManager.getLogger(InquiryDAO.class);

    /** 문의 목록 조회 */
    public List<InquiryVO> selectInquiryList(InquiryVO vo) {
        logger.debug("selectInquiryList - keyword: {}, page: {}", vo.getSearchKeyword(), vo.getPageIndex());
        return getSqlSession().selectList("inquirySQL.selectInquiryList", vo);
    }

    /** 문의 전체 건수 */
    public int selectInquiryCount(InquiryVO vo) {
        return getSqlSession().selectOne("inquirySQL.selectInquiryCount", vo);
    }

    /** 문의 상세 조회 */
    public InquiryVO selectInquiry(int inquiryId) {
        return getSqlSession().selectOne("inquirySQL.selectInquiry", inquiryId);
    }

    /** 문의 등록 */
    public void insertInquiry(InquiryVO vo) {
        getSqlSession().insert("inquirySQL.insertInquiry", vo);
    }

    /** 문의 수정 */
    public void updateInquiry(InquiryVO vo) {
        getSqlSession().update("inquirySQL.updateInquiry", vo);
    }

    /** 문의 소프트 삭제 */
    public void deleteInquiry(int inquiryId) {
        getSqlSession().update("inquirySQL.deleteInquiry", inquiryId);
    }

    /** 답변 등록 */
    public void updateAnswer(InquiryVO vo) {
        getSqlSession().update("inquirySQL.updateAnswer", vo);
    }
}
