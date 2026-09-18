package com.stuedit.web;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stuedit.service.AttendService;
import com.stuedit.service.StudentService;
import com.stuedit.vo.AttendVO;
import com.stuedit.vo.StudentVO;

/**
 * 출결 관리 컨트롤러
 * URL 패턴: /attend/*.do
 */
@Controller
@RequestMapping("/attend")
public class AttendController {

    private static final Logger logger = LogManager.getLogger(AttendController.class);

    @Autowired
    private AttendService attendService;

    @Autowired
    private StudentService studentService;

    /* ===================================================================
     * 출결 목록 조회 (페이징 + 기간·상태 검색)
     * GET /attend/list.do
     * =================================================================== */
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String selectAttendList(
            @ModelAttribute("searchVO") AttendVO searchVO,
            ModelMap model) throws Exception {

        if (searchVO.getPageSize()  == 0) searchVO.setPageSize(15);
        if (searchVO.getPageIndex() == 0) searchVO.setPageIndex(1);

        int totalCount   = attendService.selectAttendCount(searchVO);
        List<AttendVO> attendList = attendService.selectAttendList(searchVO);

        int pageSize   = searchVO.getPageSize();
        int pageIndex  = searchVO.getPageIndex();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int blockSize  = 10;
        int startPage  = ((pageIndex - 1) / blockSize) * blockSize + 1;
        int endPage    = Math.min(startPage + blockSize - 1, totalPages);

        // 특정 학생 조회 시 출석률 통계 함께 표시
        if (searchVO.getStudentId() != 0) {
            AttendVO stat = attendService.selectAttendStat(searchVO);
            model.addAttribute("attendStat", stat);

            StudentVO student = studentService.selectStudent(searchVO.getStudentId());
            model.addAttribute("targetStudent", student);
        }

        model.addAttribute("attendList",  attendList);
        model.addAttribute("totalCount",  totalCount);
        model.addAttribute("totalPages",  totalPages);
        model.addAttribute("startPage",   startPage);
        model.addAttribute("endPage",     endPage);

        return "attend/list";
    }

    /* ===================================================================
     * 출결 등록 폼
     * GET /attend/insertForm.do
     * =================================================================== */
    @RequestMapping(value = "/insertForm.do", method = RequestMethod.GET)
    public String insertForm(
            @RequestParam(value = "studentId", defaultValue = "0") int studentId,
            ModelMap model) throws Exception {

        AttendVO attendVO = new AttendVO();
        if (studentId != 0) {
            StudentVO student = studentService.selectStudent(studentId);
            model.addAttribute("targetStudent", student);
            attendVO.setStudentId(studentId);
        }
        model.addAttribute("attendVO", attendVO);
        return "attend/insertForm";
    }

    /* ===================================================================
     * 출결 등록 처리
     * POST /attend/insert.do
     * =================================================================== */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    public String insertAttend(
            @ModelAttribute AttendVO attendVO,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            attendService.insertAttend(attendVO);
            redirectAttr.addFlashAttribute("resultMsg", "출결이 등록되었습니다.");
        } catch (Exception e) {
            logger.error("출결 등록 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/attend/insertForm.do?studentId=" + attendVO.getStudentId();
        }

        if (attendVO.getStudentId() != 0) {
            return "redirect:/attend/list.do?studentId=" + attendVO.getStudentId();
        }
        return "redirect:/attend/list.do";
    }

    /* ===================================================================
     * 출결 수정 폼
     * GET /attend/updateForm.do?attendId=1
     * =================================================================== */
    @RequestMapping(value = "/updateForm.do", method = RequestMethod.GET)
    public String updateForm(
            @RequestParam("attendId") int attendId,
            ModelMap model) throws Exception {

        AttendVO attendVO = attendService.selectAttend(attendId);
        model.addAttribute("attendVO", attendVO);
        return "attend/insertForm"; // 등록 폼을 수정 폼으로 재사용
    }

    /* ===================================================================
     * 출결 수정 처리
     * POST /attend/update.do
     * =================================================================== */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    public String updateAttend(
            @ModelAttribute AttendVO attendVO,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            attendService.updateAttend(attendVO);
            redirectAttr.addFlashAttribute("resultMsg", "출결이 수정되었습니다.");
        } catch (Exception e) {
            logger.error("출결 수정 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/attend/updateForm.do?attendId=" + attendVO.getAttendId();
        }
        if (attendVO.getStudentId() != 0) {
            return "redirect:/attend/list.do?studentId=" + attendVO.getStudentId();
        }
        return "redirect:/attend/list.do";
    }

    /* ===================================================================
     * 출결 삭제
     * GET /attend/delete.do?attendId=1
     * =================================================================== */
    @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
    public String deleteAttend(
            @RequestParam("attendId") int attendId,
            @RequestParam(value = "studentId", defaultValue = "0") int studentId,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            attendService.deleteAttend(attendId);
            redirectAttr.addFlashAttribute("resultMsg", "출결이 삭제되었습니다.");
        } catch (Exception e) {
            logger.error("출결 삭제 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        if (studentId != 0) {
            return "redirect:/attend/list.do?studentId=" + studentId;
        }
        return "redirect:/attend/list.do";
    }
}
