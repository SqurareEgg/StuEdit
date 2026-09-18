package com.stuedit.web;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stuedit.service.FileService;
import com.stuedit.vo.FileVO;

/**
 * 파일 업로드/다운로드 컨트롤러
 */
@Controller
@RequestMapping("/file")
public class FileController {

    private static final Logger logger = LogManager.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    /* ===================================================================
     * 파일 업로드
     * POST /file/upload.do
     * 공지사항 등록·수정 폼에서 호출
     * =================================================================== */
    @RequestMapping(value = "/upload.do", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(
            @RequestParam("file")    MultipartFile file,
            @RequestParam("refId")   int refId,
            @RequestParam("refType") String refType) {
        try {
            FileVO result = fileService.uploadFile(file, refId, refType);
            return "OK:" + result.getFileId() + ":" + result.getOriginalName();
        } catch (Exception e) {
            logger.error("파일 업로드 오류", e);
            return "ERROR:" + e.getMessage();
        }
    }

    /* ===================================================================
     * 파일 다운로드
     * GET /file/download.do?fileId=1
     * =================================================================== */
    @RequestMapping(value = "/download.do", method = RequestMethod.GET)
    public void downloadFile(
            @RequestParam("fileId") int fileId,
            HttpServletResponse response) {
        try {
            fileService.downloadFile(fileId, response);
        } catch (Exception e) {
            logger.error("파일 다운로드 오류 - fileId: {}", fileId, e);
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            } catch (Exception ignored) {}
        }
    }

    /* ===================================================================
     * 파일 삭제
     * GET /file/delete.do?fileId=1
     * =================================================================== */
    @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
    public String deleteFile(
            @RequestParam("fileId")  int fileId,
            @RequestParam(value = "redirectUrl", defaultValue = "/main.do") String redirectUrl,
            RedirectAttributes redirectAttr) {
        try {
            fileService.deleteFile(fileId);
            redirectAttr.addFlashAttribute("resultMsg", "파일이 삭제되었습니다.");
        } catch (Exception e) {
            logger.error("파일 삭제 오류 - fileId: {}", fileId, e);
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:" + redirectUrl;
    }
}
