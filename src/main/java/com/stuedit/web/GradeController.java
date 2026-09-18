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

import com.stuedit.service.GradeService;
import com.stuedit.service.StudentService;
import com.stuedit.vo.GradeVO;
import com.stuedit.vo.StudentVO;

/**
 * 성적 관리 컨트롤러
 * URL 패턴: /grade/*.do
 */
@Controller
@RequestMapping("/grade")
public class GradeController {

    private static final Logger logger = LogManager.getLogger(GradeController.class);

    @Autowired
    private GradeService gradeService;

    @Autowired
    private StudentService studentService;

    /* ===================================================================
     * 성적 목록 조회 (페이징 + 검색)
     * GET /grade/list.do
     * 학생 단건 조회 시: ?studentId=1 파라미터 포함
     * =================================================================== */
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String selectGradeList(
            @ModelAttribute("searchVO") GradeVO searchVO,
            ModelMap model) throws Exception {

        if (searchVO.getPageSize()  == 0) searchVO.setPageSize(10);
        if (searchVO.getPageIndex() == 0) searchVO.setPageIndex(1);

        int totalCount = gradeService.selectGradeCount(searchVO);
        List<GradeVO> gradeList = gradeService.selectGradeList(searchVO);

        int pageSize   = searchVO.getPageSize();
        int pageIndex  = searchVO.getPageIndex();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int blockSize  = 10;
        int startPage  = ((pageIndex - 1) / blockSize) * blockSize + 1;
        int endPage    = Math.min(startPage + blockSize - 1, totalPages);

        // 특정 학생 조회 시 평균 학점 함께 표시
        if (searchVO.getStudentId() != 0) {
            GradeVO avg = gradeService.selectGradeAvg(searchVO.getStudentId());
            model.addAttribute("gradeAvg", avg);

            StudentVO student = studentService.selectStudent(searchVO.getStudentId());
            model.addAttribute("targetStudent", student);
        }

        // 학기 드롭다운 목록
        List<String> semesterList = gradeService.selectSemesterList();

        model.addAttribute("gradeList",    gradeList);
        model.addAttribute("totalCount",   totalCount);
        model.addAttribute("totalPages",   totalPages);
        model.addAttribute("startPage",    startPage);
        model.addAttribute("endPage",      endPage);
        model.addAttribute("semesterList", semesterList);

        return "grade/list";
    }

    /* ===================================================================
     * 성적 등록 폼
     * GET /grade/insertForm.do
     * =================================================================== */
    @RequestMapping(value = "/insertForm.do", method = RequestMethod.GET)
    public String insertForm(
            @RequestParam(value = "studentId", defaultValue = "0") int studentId,
            ModelMap model) throws Exception {

        GradeVO gradeVO = new GradeVO();
        List<String> semesterList = gradeService.selectSemesterList();

        if (studentId != 0) {
            StudentVO student = studentService.selectStudent(studentId);
            model.addAttribute("targetStudent", student);
            gradeVO.setStudentId(studentId);
        }

        model.addAttribute("gradeVO",      gradeVO);
        model.addAttribute("semesterList", semesterList);
        return "grade/insertForm";
    }

    /* ===================================================================
     * 성적 등록 처리
     * POST /grade/insert.do
     * =================================================================== */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    public String insertGrade(
            @ModelAttribute GradeVO gradeVO,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            gradeService.insertGrade(gradeVO);
            redirectAttr.addFlashAttribute("resultMsg", "성적이 등록되었습니다.");
        } catch (Exception e) {
            logger.error("성적 등록 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/grade/insertForm.do?studentId=" + gradeVO.getStudentId();
        }

        // 특정 학생에서 등록한 경우 해당 학생 성적 목록으로 복귀
        if (gradeVO.getStudentId() != 0) {
            return "redirect:/grade/list.do?studentId=" + gradeVO.getStudentId();
        }
        return "redirect:/grade/list.do";
    }

    /* ===================================================================
     * 성적 수정 폼
     * GET /grade/updateForm.do?gradeId=1
     * =================================================================== */
    @RequestMapping(value = "/updateForm.do", method = RequestMethod.GET)
    public String updateForm(
            @RequestParam("gradeId") int gradeId,
            ModelMap model) throws Exception {

        GradeVO gradeVO       = gradeService.selectGrade(gradeId);
        List<String> semesterList = gradeService.selectSemesterList();

        model.addAttribute("gradeVO",      gradeVO);
        model.addAttribute("semesterList", semesterList);
        return "grade/updateForm";
    }

    /* ===================================================================
     * 성적 수정 처리
     * POST /grade/update.do
     * =================================================================== */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    public String updateGrade(
            @ModelAttribute GradeVO gradeVO,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            gradeService.updateGrade(gradeVO);
            redirectAttr.addFlashAttribute("resultMsg", "성적이 수정되었습니다.");
        } catch (Exception e) {
            logger.error("성적 수정 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/grade/updateForm.do?gradeId=" + gradeVO.getGradeId();
        }
        return "redirect:/grade/list.do?studentId=" + gradeVO.getStudentId();
    }

    /* ===================================================================
     * 성적 삭제
     * GET /grade/delete.do?gradeId=1&studentId=1
     * =================================================================== */
    @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
    public String deleteGrade(
            @RequestParam("gradeId")   int gradeId,
            @RequestParam(value = "studentId", defaultValue = "0") int studentId,
            RedirectAttributes redirectAttr) throws Exception {
        try {
            gradeService.deleteGrade(gradeId);
            redirectAttr.addFlashAttribute("resultMsg", "성적이 삭제되었습니다.");
        } catch (Exception e) {
            logger.error("성적 삭제 오류", e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        if (studentId != 0) {
            return "redirect:/grade/list.do?studentId=" + studentId;
        }
        return "redirect:/grade/list.do";
    }
}
