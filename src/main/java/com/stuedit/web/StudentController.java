package com.stuedit.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.stuedit.service.StudentService;
import com.stuedit.vo.StudentVO;

/**
 * 학생 관리 컨트롤러
 * URL 패턴: /student/*.do
 */
@Controller
@RequestMapping("/student")
public class StudentController {

    private static final Logger logger = LogManager.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    /* ===================================================================
     * 목록 조회 (페이징 + 검색)
     * GET /student/list.do
     * =================================================================== */
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String selectStudentList(
            @ModelAttribute("searchVO") StudentVO searchVO,
            ModelMap model) throws Exception {

        // 기본값 설정
        if (searchVO.getPageSize()  == 0) searchVO.setPageSize(10);
        if (searchVO.getPageIndex() == 0) searchVO.setPageIndex(1);

        // 총 건수 조회
        int totalCount = studentService.selectStudentCount(searchVO);

        // 목록 조회
        List<StudentVO> studentList = studentService.selectStudentList(searchVO);

        // 페이징 계산
        int pageSize   = searchVO.getPageSize();
        int pageIndex  = searchVO.getPageIndex();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int blockSize  = 10; // 페이지 블록 크기
        int startPage  = ((pageIndex - 1) / blockSize) * blockSize + 1;
        int endPage    = Math.min(startPage + blockSize - 1, totalPages);

        model.addAttribute("studentList",  studentList);
        model.addAttribute("totalCount",   totalCount);
        model.addAttribute("totalPages",   totalPages);
        model.addAttribute("startPage",    startPage);
        model.addAttribute("endPage",      endPage);

        logger.debug("학생 목록 조회 완료 - totalCount: {}, page: {}/{}", totalCount, pageIndex, totalPages);
        return "student/list";
    }

    /* ===================================================================
     * 상세 조회
     * GET /student/detail.do?studentId=1
     * =================================================================== */
    @RequestMapping(value = "/detail.do", method = RequestMethod.GET)
    public String selectStudent(
            @RequestParam("studentId") int studentId,
            @ModelAttribute("searchVO") StudentVO searchVO,
            ModelMap model) throws Exception {

        StudentVO student = studentService.selectStudent(studentId);
        model.addAttribute("student", student);
        return "student/detail";
    }

    /* ===================================================================
     * 등록 폼
     * GET /student/insertForm.do
     * =================================================================== */
    @RequestMapping(value = "/insertForm.do", method = RequestMethod.GET)
    public String insertForm(ModelMap model) {
        model.addAttribute("student", new StudentVO());
        return "student/insertForm";
    }

    /* ===================================================================
     * 등록 처리
     * POST /student/insert.do
     * =================================================================== */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    public String insertStudent(
            @ModelAttribute StudentVO studentVO,
            ModelMap model) throws Exception {
        try {
            studentService.insertStudent(studentVO);
            model.addAttribute("resultMsg", "학생이 등록되었습니다.");
            return "redirect:/student/list.do";
        } catch (Exception e) {
            logger.error("학생 등록 오류", e);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("student", studentVO);
            return "student/insertForm";
        }
    }

    /* ===================================================================
     * 수정 폼
     * GET /student/updateForm.do?studentId=1
     * =================================================================== */
    @RequestMapping(value = "/updateForm.do", method = RequestMethod.GET)
    public String updateForm(
            @RequestParam("studentId") int studentId,
            @ModelAttribute("searchVO") StudentVO searchVO,
            ModelMap model) throws Exception {

        StudentVO student = studentService.selectStudent(studentId);
        model.addAttribute("student", student);
        return "student/updateForm";
    }

    /* ===================================================================
     * 수정 처리
     * POST /student/update.do
     * =================================================================== */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    public String updateStudent(
            @ModelAttribute StudentVO studentVO,
            ModelMap model) throws Exception {
        try {
            studentService.updateStudent(studentVO);
            return "redirect:/student/detail.do?studentId=" + studentVO.getStudentId();
        } catch (Exception e) {
            logger.error("학생 수정 오류", e);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("student", studentVO);
            return "student/updateForm";
        }
    }

    /* ===================================================================
     * 엑셀 다운로드 (현재 검색 조건 반영, 페이징 없음)
     * GET /student/excelDownload.do
     * =================================================================== */
    @RequestMapping(value = "/excelDownload.do", method = RequestMethod.GET)
    public void excelDownload(
            @ModelAttribute StudentVO searchVO,
            HttpServletResponse response) throws Exception {

        List<StudentVO> list = studentService.selectStudentListAll(searchVO);

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("학생목록");

            // ── 헤더 스타일 ──
            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            Font headerFont = wb.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // ── 데이터 스타일 ──
            CellStyle dataStyle = wb.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // ── 헤더 행 ──
            String[] headers = {"번호", "학번", "이름", "학과", "학년", "성별", "연락처", "이메일", "등록일"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }
            sheet.setColumnWidth(1, 3500);  // 학번
            sheet.setColumnWidth(3, 5000);  // 학과
            sheet.setColumnWidth(6, 4500);  // 연락처
            sheet.setColumnWidth(7, 6000);  // 이메일

            // ── 데이터 행 ──
            int rowNum = 1;
            for (StudentVO s : list) {
                Row row = sheet.createRow(rowNum);
                createCell(row, 0, String.valueOf(rowNum), dataStyle);
                createCell(row, 1, s.getStudentNum(), dataStyle);
                createCell(row, 2, s.getStudentName(), dataStyle);
                createCell(row, 3, s.getDeptName(), dataStyle);
                createCell(row, 4, s.getGrade() + "학년", dataStyle);
                createCell(row, 5, "M".equals(s.getGender()) ? "남" : "여", dataStyle);
                createCell(row, 6, s.getPhone(), dataStyle);
                createCell(row, 7, s.getEmail(), dataStyle);
                createCell(row, 8,
                        s.getRegDate() != null
                            ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(s.getRegDate())
                            : "",
                        dataStyle);
                rowNum++;
            }

            // ── 응답 헤더 설정 ──
            String fileName = java.net.URLEncoder.encode("학생목록.xlsx", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            wb.write(response.getOutputStream());
            response.getOutputStream().flush();
            logger.info("학생 목록 엑셀 다운로드 완료 - {}건", list.size());

        } catch (IOException e) {
            logger.error("엑셀 다운로드 오류", e);
            throw e;
        }
    }

    /** 셀 생성 헬퍼 */
    private void createCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    /* ===================================================================
     * 삭제 처리 (소프트삭제)
     * GET /student/delete.do?studentId=1
     * =================================================================== */
    @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
    public String deleteStudent(
            @RequestParam("studentId") int studentId) throws Exception {
        studentService.deleteStudent(studentId);
        logger.info("학생 삭제 처리 - studentId: {}", studentId);
        return "redirect:/student/list.do";
    }
}
