package com.stuedit.web;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stuedit.service.ScheduleService;
import com.stuedit.vo.ScheduleVO;

/**
 * 학사 일정 컨트롤러
 * 목록·상세: 로그인 사용자 전체
 * 등록·수정·삭제: ROLE_ADMIN 전용 (코드 이중 검증)
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    private static final Logger logger = LogManager.getLogger(ScheduleController.class);

    @Autowired
    private ScheduleService scheduleService;

    /** Date 바인딩: HTML date input (yyyy-MM-dd) 처리 */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
    }

    /* ===================================================================
     * 학사 일정 목록
     * GET /schedule/list.do
     * =================================================================== */
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String selectScheduleList(
            @ModelAttribute("searchVO") ScheduleVO searchVO,
            ModelMap model) throws Exception {

        if (searchVO.getPageSize()  == 0) searchVO.setPageSize(10);
        if (searchVO.getPageIndex() == 0) searchVO.setPageIndex(1);

        int totalCount     = scheduleService.selectScheduleCount(searchVO);
        List<ScheduleVO> scheduleList = scheduleService.selectScheduleList(searchVO);

        int pageSize   = searchVO.getPageSize();
        int pageIndex  = searchVO.getPageIndex();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int blockSize  = 10;
        int startPage  = ((pageIndex - 1) / blockSize) * blockSize + 1;
        int endPage    = Math.min(startPage + blockSize - 1, totalPages);

        model.addAttribute("scheduleList", scheduleList);
        model.addAttribute("totalCount",   totalCount);
        model.addAttribute("totalPages",   totalPages);
        model.addAttribute("startPage",    startPage);
        model.addAttribute("endPage",      endPage);

        return "schedule/list";
    }

    /* ===================================================================
     * 학사 일정 상세 조회 (JSON - 모달용)
     * GET /schedule/detail.do?scheduleId=1
     * =================================================================== */
    @RequestMapping(value = "/detail.do", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> selectSchedule(
            @RequestParam("scheduleId") int scheduleId) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            ScheduleVO schedule = scheduleService.selectSchedule(scheduleId);
            result.put("success", true);
            result.put("scheduleId",   schedule.getScheduleId());
            result.put("title",        schedule.getTitle());
            result.put("content",      schedule.getContent() != null ? schedule.getContent() : "");
            result.put("scheduleType", schedule.getScheduleType());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            result.put("startDate", schedule.getStartDate() != null ? sdf.format(schedule.getStartDate()) : "");
            result.put("endDate",   schedule.getEndDate()   != null ? sdf.format(schedule.getEndDate())   : "");
            result.put("regDate",   schedule.getRegDate()   != null ? sdf.format(schedule.getRegDate())   : "");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /* ===================================================================
     * 학사 일정 등록 폼 (ROLE_ADMIN)
     * GET /schedule/insertForm.do
     * =================================================================== */
    @RequestMapping(value = "/insertForm.do", method = RequestMethod.GET)
    public String insertForm(ModelMap model, RedirectAttributes redirectAttr) {
        if (!isAdmin()) {
            redirectAttr.addFlashAttribute("errorMsg", "관리자만 접근 가능합니다.");
            return "redirect:/schedule/list.do";
        }
        model.addAttribute("schedule", new ScheduleVO());
        return "schedule/insertForm";
    }

    /* ===================================================================
     * 학사 일정 등록 처리 (ROLE_ADMIN)
     * POST /schedule/insert.do
     * =================================================================== */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    public String insertSchedule(
            @ModelAttribute ScheduleVO scheduleVO,
            RedirectAttributes redirectAttr) throws Exception {

        if (!isAdmin()) {
            redirectAttr.addFlashAttribute("errorMsg", "관리자만 접근 가능합니다.");
            return "redirect:/schedule/list.do";
        }
        try {
            scheduleService.insertSchedule(scheduleVO);
            redirectAttr.addFlashAttribute("resultMsg", "학사 일정이 등록되었습니다.");
            return "redirect:/schedule/list.do";
        } catch (Exception e) {
            logger.error("학사 일정 등록 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/schedule/insertForm.do";
        }
    }

    /* ===================================================================
     * 학사 일정 수정 폼 (ROLE_ADMIN)
     * GET /schedule/updateForm.do?scheduleId=1
     * =================================================================== */
    @RequestMapping(value = "/updateForm.do", method = RequestMethod.GET)
    public String updateForm(
            @RequestParam("scheduleId") int scheduleId,
            ModelMap model,
            RedirectAttributes redirectAttr) throws Exception {

        if (!isAdmin()) {
            redirectAttr.addFlashAttribute("errorMsg", "관리자만 접근 가능합니다.");
            return "redirect:/schedule/list.do";
        }
        ScheduleVO schedule = scheduleService.selectSchedule(scheduleId);
        model.addAttribute("schedule", schedule);
        return "schedule/updateForm";
    }

    /* ===================================================================
     * 학사 일정 수정 처리 (ROLE_ADMIN)
     * POST /schedule/update.do
     * =================================================================== */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    public String updateSchedule(
            @ModelAttribute ScheduleVO scheduleVO,
            RedirectAttributes redirectAttr) throws Exception {

        if (!isAdmin()) {
            redirectAttr.addFlashAttribute("errorMsg", "관리자만 접근 가능합니다.");
            return "redirect:/schedule/list.do";
        }
        try {
            scheduleService.updateSchedule(scheduleVO);
            redirectAttr.addFlashAttribute("resultMsg", "학사 일정이 수정되었습니다.");
        } catch (Exception e) {
            logger.error("학사 일정 수정 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/schedule/list.do";
    }

    /* ===================================================================
     * 학사 일정 삭제 (ROLE_ADMIN, 소프트삭제)
     * GET /schedule/delete.do?scheduleId=1
     * =================================================================== */
    @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
    public String deleteSchedule(
            @RequestParam("scheduleId") int scheduleId,
            RedirectAttributes redirectAttr) throws Exception {

        if (!isAdmin()) {
            redirectAttr.addFlashAttribute("errorMsg", "관리자만 접근 가능합니다.");
            return "redirect:/schedule/list.do";
        }
        try {
            scheduleService.deleteSchedule(scheduleId);
            redirectAttr.addFlashAttribute("resultMsg", "학사 일정이 삭제되었습니다.");
        } catch (Exception e) {
            logger.error("학사 일정 삭제 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/schedule/list.do";
    }

    /** 현재 사용자가 ROLE_ADMIN 인지 확인 */
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}
