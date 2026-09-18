package com.stuedit.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stuedit.dao.InquiryDAO;
import com.stuedit.service.InquiryService;
import com.stuedit.vo.InquiryVO;

/**
 * 1:1 문의 서비스 구현체
 */
@Service("inquiryService")
public class InquiryServiceImpl implements InquiryService {

    private static final Logger logger = LogManager.getLogger(InquiryServiceImpl.class);

    @Autowired
    private InquiryDAO inquiryDAO;

    /** 문의 목록 조회 */
    @Override
    public List<InquiryVO> selectInquiryList(InquiryVO vo) throws Exception {
        if (vo.getPageSize()  == 0) vo.setPageSize(10);
        if (vo.getPageIndex() == 0) vo.setPageIndex(1);
        vo.setOffset((vo.getPageIndex() - 1) * vo.getPageSize());
        return inquiryDAO.selectInquiryList(vo);
    }

    /** 문의 전체 건수 */
    @Override
    public int selectInquiryCount(InquiryVO vo) throws Exception {
        return inquiryDAO.selectInquiryCount(vo);
    }

    /** 문의 상세 조회 */
    @Override
    public InquiryVO selectInquiry(int inquiryId) throws Exception {
        InquiryVO vo = inquiryDAO.selectInquiry(inquiryId);
        if (vo == null) {
            throw new RuntimeException("존재하지 않는 문의입니다.");
        }
        return vo;
    }

    /** 문의 등록 */
    @Override
    public void insertInquiry(InquiryVO vo) throws Exception {
        if (vo.getIsSecret() == null) vo.setIsSecret("N");
        if (vo.getWriterName() == null || vo.getWriterName().trim().isEmpty()) {
            vo.setWriterName("익명");
        }
        inquiryDAO.insertInquiry(vo);
        logger.info("문의 등록 완료 - title: {}, writerId: {}", vo.getTitle(), vo.getWriterId());
    }

    /** 문의 수정 */
    @Override
    public void updateInquiry(InquiryVO vo) throws Exception {
        if (vo.getIsSecret() == null) vo.setIsSecret("N");
        inquiryDAO.updateInquiry(vo);
        logger.info("문의 수정 완료 - inquiryId: {}", vo.getInquiryId());
    }

    /** 문의 소프트 삭제 */
    @Override
    public void deleteInquiry(int inquiryId) throws Exception {
        inquiryDAO.deleteInquiry(inquiryId);
        logger.info("문의 삭제 완료 - inquiryId: {}", inquiryId);
    }

    /** 답변 등록 */
    @Override
    public void updateAnswer(InquiryVO vo) throws Exception {
        inquiryDAO.updateAnswer(vo);
        logger.info("문의 답변 등록 완료 - inquiryId: {}, answeredBy: {}", vo.getInquiryId(), vo.getAnsweredBy());
    }
}
