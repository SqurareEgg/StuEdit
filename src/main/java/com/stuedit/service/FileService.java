package com.stuedit.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.stuedit.vo.FileVO;

/**
 * 파일 업로드/다운로드 서비스 인터페이스
 */
public interface FileService {

    /** 파일 업로드 (단건) — 저장 후 FileVO 반환 */
    FileVO uploadFile(MultipartFile file, int refId, String refType) throws Exception;

    /** 파일 목록 조회 */
    List<FileVO> selectFileList(int refId, String refType) throws Exception;

    /** 파일 단건 조회 */
    FileVO selectFile(int fileId) throws Exception;

    /** 파일 다운로드 — Response에 파일 스트림 출력 */
    void downloadFile(int fileId, HttpServletResponse response) throws Exception;

    /** 파일 삭제 (DB + 디스크) */
    void deleteFile(int fileId) throws Exception;

    /** 참조 ID 기준 전체 파일 삭제 */
    void deleteFileByRef(int refId, String refType) throws Exception;
}
