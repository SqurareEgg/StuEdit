package com.stuedit.vo;

import java.util.Date;

/**
 * 파일 정보 Value Object
 * TB_FILE 테이블과 매핑
 */
public class FileVO {

    /* ── 기본 컬럼 ── */
    private int    fileId;       // PK (AUTO_INCREMENT)
    private int    refId;        // 참조 ID (공지사항 ID 등)
    private String refType;      // 참조 타입 (NOTICE 등)
    private String originalName; // 원본 파일명
    private String savedName;    // 저장 파일명 (UUID)
    private String filePath;     // 저장 경로
    private long   fileSize;     // 파일 크기 (bytes)
    private String fileExt;      // 확장자 (pdf/jpg/png)
    private Date   regDate;      // 등록일

    /* ── Getter / Setter ── */

    public int getFileId() { return fileId; }
    public void setFileId(int fileId) { this.fileId = fileId; }

    public int getRefId() { return refId; }
    public void setRefId(int refId) { this.refId = refId; }

    public String getRefType() { return refType; }
    public void setRefType(String refType) { this.refType = refType; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getSavedName() { return savedName; }
    public void setSavedName(String savedName) { this.savedName = savedName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getFileExt() { return fileExt; }
    public void setFileExt(String fileExt) { this.fileExt = fileExt; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }
}
