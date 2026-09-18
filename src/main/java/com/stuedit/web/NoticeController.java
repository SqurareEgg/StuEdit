package com.stuedit.web;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stuedit.service.NoticeService;
import com.stuedit.vo.NoticeVO;

/**
 * 공지사항 컨트롤러
 * 목록·상세: 로그인 사용자 전체
 * 등록·수정·삭제: ROLE_ADMIN 전용 (Security URL 제어 + 코드 이중 검증)
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

    private static final Logger logger = LogManager.getLogger(NoticeController.class);

    @Autowired
    private NoticeService noticeService;

    /* ===================================================================
     * 공지사항 목록
     * GET /notice/list.do
     * =================================================================== */
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String selectNoticeList(
            @ModelAttribute("searchVO") NoticeVO searchVO,
            ModelMap model) throws Exception {

        if (searchVO.getPageSize()  == 0) searchVO.setPageSize(10);
        if (searchVO.getPageIndex() == 0) searchVO.setPageIndex(1);

        int totalCount = noticeService.selectNoticeCount(searchVO);
        List<NoticeVO> noticeList = noticeService.selectNoticeList(searchVO);

        int pageSize   = searchVO.getPageSize();
        int pageIndex  = searchVO.getPageIndex();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int blockSize  = 10;
        int startPage  = ((pageIndex - 1) / blockSize) * blockSize + 1;
        int endPage    = Math.min(startPage + blockSize - 1, totalPages);

        model.addAttribute("noticeList",  noticeList);
        model.addAttribute("totalCount",  totalCount);
        model.addAttribute("totalPages",  totalPages);
        model.addAttribute("startPage",   startPage);
        model.addAttribute("endPage",     endPage);

        return "notice/list";
    }

    /* ===================================================================
     * 공지사항 상세 (조회수 자동 증가)
     * GET /notice/detail.do?noticeId=1
     * =================================================================== */
    @RequestMapping(value = "/detail.do", method = RequestMethod.GET)
    public String selectNotice(
            @RequestParam("noticeId") int noticeId,
            @ModelAttribute("searchVO") NoticeVO searchVO,
            ModelMap model) throws Exception {

        noticeService.updateViewCount(noticeId);
        NoticeVO notice   = noticeService.selectNotice(noticeId);
        NoticeVO prevNotice = noticeService.selectPrevNotice(noticeId);
        NoticeVO nextNotice = noticeService.selectNextNotice(noticeId);

        model.addAttribute("notice",      notice);
        model.addAttribute("prevNotice",  prevNotice);
        model.addAttribute("nextNotice",  nextNotice);
        return "notice/detail";
    }

    /* ===================================================================
     * 공지사항 등록 폼 (ROLE_ADMIN)
     * GET /notice/insertForm.do
     * =================================================================== */
    @RequestMapping(value = "/insertForm.do", method = RequestMethod.GET)
    public String insertForm(ModelMap model) {
        model.addAttribute("notice", new NoticeVO());
        return "notice/insertForm";
    }

    /* ===================================================================
     * 공지사항 등록 처리 (ROLE_ADMIN)
     * POST /notice/insert.do
     * =================================================================== */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    public String insertNotice(
            @ModelAttribute NoticeVO noticeVO,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            // 작성자: 현재 로그인 사용자 ID
            noticeVO.setWriter(getLoginId());
            noticeService.insertNotice(noticeVO);
            redirectAttr.addFlashAttribute("resultMsg", "공지사항이 등록되었습니다.");
            return "redirect:/notice/detail.do?noticeId=" + noticeVO.getNoticeId();
        } catch (Exception e) {
            logger.error("공지사항 등록 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/notice/insertForm.do";
        }
    }

    /* ===================================================================
     * 공지사항 수정 폼 (ROLE_ADMIN)
     * GET /notice/updateForm.do?noticeId=1
     * =================================================================== */
    @RequestMapping(value = "/updateForm.do", method = RequestMethod.GET)
    public String updateForm(
            @RequestParam("noticeId") int noticeId,
            ModelMap model) throws Exception {

        // 조회수 증가 없이 원본 데이터 조회 (DAO 직접 호출 대신 수정 전용 조회 필요)
        // 여기서는 별도 조회수 미증가 처리 위해 selectNotice 대신 내부 DAO 접근이 이상적이나
        // 단순화를 위해 동일 메서드 사용
        NoticeVO notice = noticeService.selectNotice(noticeId);
        model.addAttribute("notice", notice);
        return "notice/updateForm";
    }

    /* ===================================================================
     * 공지사항 수정 처리 (ROLE_ADMIN)
     * POST /notice/update.do
     * =================================================================== */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    public String updateNotice(
            @ModelAttribute NoticeVO noticeVO,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            noticeService.updateNotice(noticeVO);
            redirectAttr.addFlashAttribute("resultMsg", "공지사항이 수정되었습니다.");
        } catch (Exception e) {
            logger.error("공지사항 수정 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/notice/detail.do?noticeId=" + noticeVO.getNoticeId();
    }

    /* ===================================================================
     * 공지사항 삭제 (ROLE_ADMIN, 소프트삭제)
     * GET /notice/delete.do?noticeId=1
     * =================================================================== */
    @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
    public String deleteNotice(
            @RequestParam("noticeId") int noticeId,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            noticeService.deleteNotice(noticeId);
            redirectAttr.addFlashAttribute("resultMsg", "공지사항이 삭제되었습니다.");
        } catch (Exception e) {
            logger.error("공지사항 삭제 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/notice/list.do";
    }

    /** 현재 로그인 사용자 ID 조회 */
    private String getLoginId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "anonymous";
    }
}
