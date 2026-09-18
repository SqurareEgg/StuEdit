package com.stuedit.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.stuedit.vo.ChatLogVO;

/**
 * 챗봇 대화 기록 DAO
 * TB_CHAT_LOG 테이블 CRUD
 */
@Repository("chatLogDAO")
public class ChatLogDAO extends AbstractBaseDAO {

    private static final Logger logger = LogManager.getLogger(ChatLogDAO.class);

    /**
     * 대화 기록 저장
     * @param vo 저장할 대화 로그 VO
     */
    public void insertChatLog(ChatLogVO vo) {
        logger.debug("insertChatLog - userId: {}", vo.getUserId());
        getSqlSession().insert("chatlogSQL.insertChatLog", vo);
    }

    /**
     * 사용자 대화 이력 조회 (최근 20개, 시간순)
     * @param userId 조회할 사용자 ID
     * @return 대화 로그 목록 (오래된 순)
     */
    public List<ChatLogVO> selectChatHistory(int userId) {
        logger.debug("selectChatHistory - userId: {}", userId);
        return getSqlSession().selectList("chatlogSQL.selectChatHistory", userId);
    }
}
