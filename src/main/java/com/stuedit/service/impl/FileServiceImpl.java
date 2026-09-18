package com.stuedit.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stuedit.dao.FileDAO;
import com.stuedit.service.FileService;
import com.stuedit.vo.FileVO;

/**
 * 파일 업로드/다운로드 서비스 구현체
 * - 저장 경로: upload.path (context-common.xml 설정)
 * - 허용 확장자: pdf, jpg, jpeg, png
 * - 최대 크기: 10MB (dispatcher-servlet.xml MultipartResolver 설정)
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);

    /** 허용 확장자 목록 */
    private static final Set<String> ALLOWED_EXT =
            new HashSet<>(Arrays.asList("pdf", "jpg", "jpeg", "png"));

    /** 최대 파일 크기: 10MB */
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024L;

    @Value("${upload.path:C:/upload/studit}")
    private String uploadPath;

    private final FileDAO fileDAO;

    public FileServiceImpl(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }

    /** 파일 업로드 */
    @Override
    public FileVO uploadFile(MultipartFile file, int refId, String refType) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("업로드할 파일이 없습니다.");
        }

        // 파일 크기 검증
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("파일 크기는 10MB를 초과할 수 없습니다.");
        }

        // 확장자 검증
        String originalName = file.getOriginalFilename();
        String ext = extractExt(originalName).toLowerCase();
        if (!ALLOWED_EXT.contains(ext)) {
            throw new RuntimeException("허용되지 않는 파일 형식입니다. (pdf, jpg, png만 가능)");
        }

        // 저장 경로 생성 (없으면 자동 생성)
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // UUID 저장명 생성
        String savedName = UUID.randomUUID().toString() + "." + ext;
        File dest = new File(dir, savedName);
        file.transferTo(dest);

        // DB 저장
        FileVO vo = new FileVO();
        vo.setRefId(refId);
        vo.setRefType(refType);
        vo.setOriginalName(originalName);
        vo.setSavedName(savedName);
        vo.setFilePath(uploadPath);
        vo.setFileSize(file.getSize());
        vo.setFileExt(ext);
        fileDAO.insertFile(vo);

        logger.info("파일 업로드 완료 - original: {}, saved: {}, size: {}bytes",
                originalName, savedName, file.getSize());
        return vo;
    }

    /** 파일 목록 조회 */
    @Override
    public List<FileVO> selectFileList(int refId, String refType) throws Exception {
        FileVO param = new FileVO();
        param.setRefId(refId);
        param.setRefType(refType);
        return fileDAO.selectFileList(param);
    }

    /** 파일 단건 조회 */
    @Override
    public FileVO selectFile(int fileId) throws Exception {
        FileVO vo = fileDAO.selectFile(fileId);
        if (vo == null) {
            throw new RuntimeException("존재하지 않는 파일입니다.");
        }
        return vo;
    }

    /** 파일 다운로드 */
    @Override
    public void downloadFile(int fileId, HttpServletResponse response) throws Exception {
        FileVO vo = selectFile(fileId);
        File file = new File(vo.getFilePath(), vo.getSavedName());

        if (!file.exists()) {
            throw new RuntimeException("파일이 서버에 존재하지 않습니다.");
        }

        // Content-Type 설정
        String contentType = resolveContentType(vo.getFileExt());
        response.setContentType(contentType);

        // 파일명 인코딩 (한글 파일명 지원)
        String encodedName = URLEncoder.encode(vo.getOriginalName(), "UTF-8")
                .replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + encodedName + "\"");
        response.setHeader("Content-Length", String.valueOf(file.length()));

        // 스트림 출력
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            IOUtils.copy(fis, os);
            os.flush();
        }
        logger.info("파일 다운로드 - original: {}", vo.getOriginalName());
    }

    /** 파일 삭제 (DB + 디스크) */
    @Override
    public void deleteFile(int fileId) throws Exception {
        FileVO vo = selectFile(fileId);
        // 디스크 파일 삭제
        File file = new File(vo.getFilePath(), vo.getSavedName());
        if (file.exists()) {
            file.delete();
        }
        // DB 삭제
        fileDAO.deleteFile(fileId);
        logger.info("파일 삭제 완료 - fileId: {}, original: {}", fileId, vo.getOriginalName());
    }

    /** 참조 ID 기준 전체 파일 삭제 */
    @Override
    public void deleteFileByRef(int refId, String refType) throws Exception {
        List<FileVO> list = selectFileList(refId, refType);
        for (FileVO vo : list) {
            File file = new File(vo.getFilePath(), vo.getSavedName());
            if (file.exists()) file.delete();
        }
        FileVO param = new FileVO();
        param.setRefId(refId);
        param.setRefType(refType);
        fileDAO.deleteFileByRef(param);
    }

    /** 확장자 추출 */
    private String extractExt(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    /** Content-Type 결정 */
    private String resolveContentType(String ext) {
        switch (ext.toLowerCase()) {
            case "pdf":  return "application/pdf";
            case "jpg":
            case "jpeg": return "image/jpeg";
            case "png":  return "image/png";
            default:     return "application/octet-stream";
        }
    }
}
