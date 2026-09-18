package com.stuedit.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.stuedit.vo.FileVO;

/**
 * 파일 DAO
 */
@Repository("fileDAO")
public class FileDAO extends AbstractBaseDAO {

    public void insertFile(FileVO vo) {
        getSqlSession().insert("fileSQL.insertFile", vo);
    }

    public List<FileVO> selectFileList(FileVO vo) {
        return getSqlSession().selectList("fileSQL.selectFileList", vo);
    }

    public FileVO selectFile(int fileId) {
        return getSqlSession().selectOne("fileSQL.selectFile", fileId);
    }

    public void deleteFile(int fileId) {
        getSqlSession().delete("fileSQL.deleteFile", fileId);
    }

    public void deleteFileByRef(FileVO vo) {
        getSqlSession().delete("fileSQL.deleteFileByRef", vo);
    }
}
