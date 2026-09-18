package com.stuedit.service;

import java.util.List;

import com.stuedit.vo.InquiryVO;

/**
 * 1:1 문의 서비스 인터페이스
 */
public interface InquiryService {

    /** 문의 목록 조회 (페이징 + 검색) */
    List<InquiryVO> selectInquiryList(InquiryVO vo) throws Exception;

    /** 문의 전체 건수 */
    int selectInquiryCount(InquiryVO vo) throws Exception;

    /** 문의 상세 조회 */
    InquiryVO selectInquiry(int inquiryId) throws Exception;

    /** 문의 등록 */
    void insertInquiry(InquiryVO vo) throws Exception;

    /** 문의 수정 */
    void updateInquiry(InquiryVO vo) throws Exception;

    /** 문의 삭제 (소프트 삭제) */
    void deleteInquiry(int inquiryId) throws Exception;

    /** 답변 등록 */
    void updateAnswer(InquiryVO vo) throws Exception;
}
