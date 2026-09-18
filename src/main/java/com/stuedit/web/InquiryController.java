package com.stuedit.web;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stuedit.service.InquiryService;
import com.stuedit.vo.InquiryVO;

/**
 * 1:1 문의 컨트롤러
 * 목록: 로그인 사용자 전체
 * 등록: 로그인 사용자 전체
 * 수정/삭제: 본인 또는 관리자
 * 답변: ROLE_ADMIN 전용
 * 비밀글: 작성자 본인 또는 ROLE_ADMIN만 열람
 */
@Controller
@RequestMapping("/inquiry")
public class InquiryController {

    private static final Logger logger = LogManager.getLogger(InquiryController.class);

    @Autowired
    private InquiryService inquiryService;

    /* ===================================================================
     * 문의 목록
     * GET /inquiry/list.do
     * =================================================================== */
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String selectInquiryList(
            @ModelAttribute("searchVO") InquiryVO searchVO,
            ModelMap model) throws Exception {

        if (searchVO.getPageSize()  == 0) searchVO.setPageSize(10);
        if (searchVO.getPageIndex() == 0) searchVO.setPageIndex(1);

        int totalCount = inquiryService.selectInquiryCount(searchVO);
        List<InquiryVO> inquiryList = inquiryService.selectInquiryList(searchVO);

        int pageSize   = searchVO.getPageSize();
        int pageIndex  = searchVO.getPageIndex();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int blockSize  = 10;
        int startPage  = ((pageIndex - 1) / blockSize) * blockSize + 1;
        int endPage    = Math.min(startPage + blockSize - 1, totalPages);

        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("totalCount",  totalCount);
        model.addAttribute("totalPages",  totalPages);
        model.addAttribute("startPage",   startPage);
        model.addAttribute("endPage",     endPage);

        return "inquiry/list";
    }

    /* ===================================================================
     * 문의 상세 조회
     * GET /inquiry/detail.do?inquiryId=
     * 비밀글이면 작성자 본인 또는 ROLE_ADMIN만 열람 가능
     * =================================================================== */
    @RequestMapping(value = "/detail.do", method = RequestMethod.GET)
    public String selectInquiry(
            @RequestParam("inquiryId") int inquiryId,
            @ModelAttribute("searchVO") InquiryVO searchVO,
            ModelMap model,
            RedirectAttributes redirectAttr) throws Exception {

        InquiryVO inquiry = inquiryService.selectInquiry(inquiryId);

        // 비밀글 열람 권한 체크
        if ("Y".equals(inquiry.getIsSecret())) {
            String loginId = getLoginId();
            if (!inquiry.getWriterId().equals(loginId) && !isAdmin()) {
                redirectAttr.addFlashAttribute("errorMsg", "비밀글입니다.");
                return "redirect:/inquiry/list.do";
            }
        }

        model.addAttribute("inquiry", inquiry);
        return "inquiry/detail";
    }

    /* ===================================================================
     * 문의 등록 폼
     * GET /inquiry/insertForm.do
     * =================================================================== */
    @RequestMapping(value = "/insertForm.do", method = RequestMethod.GET)
    public String insertForm(ModelMap model) {
        model.addAttribute("inquiry", new InquiryVO());
        return "inquiry/insertForm";
    }

    /* ===================================================================
     * 문의 등록 처리
     * POST /inquiry/insert.do
     * =================================================================== */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    public String insertInquiry(
            @ModelAttribute InquiryVO inquiryVO,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            inquiryVO.setWriterId(auth != null ? auth.getName() : "anonymous");

            // writerName이 비어 있으면 loginId로 채움 (서비스에서 최종 처리)
            if (inquiryVO.getWriterName() == null || inquiryVO.getWriterName().trim().isEmpty()) {
                inquiryVO.setWriterName(inquiryVO.getWriterId());
            }

            // 비밀글 체크박스 미선택 시 null → "N"
            if (inquiryVO.getIsSecret() == null) inquiryVO.setIsSecret("N");

            inquiryService.insertInquiry(inquiryVO);
            redirectAttr.addFlashAttribute("resultMsg", "문의가 등록되었습니다.");
            return "redirect:/inquiry/detail.do?inquiryId=" + inquiryVO.getInquiryId();
        } catch (Exception e) {
            logger.error("문의 등록 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/inquiry/insertForm.do";
        }
    }

    /* ===================================================================
     * 문의 수정 폼 (본인 작성글만)
     * GET /inquiry/updateForm.do?inquiryId=
     * =================================================================== */
    @RequestMapping(value = "/updateForm.do", method = RequestMethod.GET)
    public String updateForm(
            @RequestParam("inquiryId") int inquiryId,
            ModelMap model,
            RedirectAttributes redirectAttr) throws Exception {

        InquiryVO inquiry = inquiryService.selectInquiry(inquiryId);

        // 본인 작성글 확인
        if (!inquiry.getWriterId().equals(getLoginId())) {
            redirectAttr.addFlashAttribute("errorMsg", "본인이 작성한 문의만 수정할 수 있습니다.");
            return "redirect:/inquiry/list.do";
        }

        model.addAttribute("inquiry", inquiry);
        return "inquiry/updateForm";
    }

    /* ===================================================================
     * 문의 수정 처리 (본인만)
     * POST /inquiry/update.do
     * =================================================================== */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    public String updateInquiry(
            @ModelAttribute InquiryVO inquiryVO,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            InquiryVO existing = inquiryService.selectInquiry(inquiryVO.getInquiryId());
            if (!existing.getWriterId().equals(getLoginId())) {
                redirectAttr.addFlashAttribute("errorMsg", "본인이 작성한 문의만 수정할 수 있습니다.");
                return "redirect:/inquiry/list.do";
            }

            if (inquiryVO.getIsSecret() == null) inquiryVO.setIsSecret("N");

            inquiryService.updateInquiry(inquiryVO);
            redirectAttr.addFlashAttribute("resultMsg", "문의가 수정되었습니다.");
        } catch (Exception e) {
            logger.error("문의 수정 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/inquiry/detail.do?inquiryId=" + inquiryVO.getInquiryId();
    }

    /* ===================================================================
     * 문의 삭제 (본인 또는 관리자)
     * GET /inquiry/delete.do?inquiryId=
     * =================================================================== */
    @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
    public String deleteInquiry(
            @RequestParam("inquiryId") int inquiryId,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            InquiryVO existing = inquiryService.selectInquiry(inquiryId);
            String loginId = getLoginId();
            if (!existing.getWriterId().equals(loginId) && !isAdmin()) {
                redirectAttr.addFlashAttribute("errorMsg", "본인이 작성한 문의만 삭제할 수 있습니다.");
                return "redirect:/inquiry/list.do";
            }

            inquiryService.deleteInquiry(inquiryId);
            redirectAttr.addFlashAttribute("resultMsg", "문의가 삭제되었습니다.");
        } catch (Exception e) {
            logger.error("문의 삭제 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/inquiry/list.do";
    }

    /* ===================================================================
     * 답변 등록 (ROLE_ADMIN만)
     * POST /inquiry/answer.do
     * =================================================================== */
    @RequestMapping(value = "/answer.do", method = RequestMethod.POST)
    public String updateAnswer(
            @ModelAttribute InquiryVO inquiryVO,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            if (!isAdmin()) {
                redirectAttr.addFlashAttribute("errorMsg", "관리자만 답변을 등록할 수 있습니다.");
                return "redirect:/inquiry/detail.do?inquiryId=" + inquiryVO.getInquiryId();
            }

            inquiryVO.setAnsweredBy(getLoginId());
            inquiryService.updateAnswer(inquiryVO);
            redirectAttr.addFlashAttribute("resultMsg", "답변이 등록되었습니다.");
        } catch (Exception e) {
            logger.error("답변 등록 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/inquiry/detail.do?inquiryId=" + inquiryVO.getInquiryId();
    }

    /* ── 내부 헬퍼 ── */

    /** 현재 로그인 사용자 ID 조회 */
    private String getLoginId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "anonymous";
    }

    /** 현재 로그인 사용자가 ROLE_ADMIN인지 확인 */
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority ga : authorities) {
            if ("ROLE_ADMIN".equals(ga.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
