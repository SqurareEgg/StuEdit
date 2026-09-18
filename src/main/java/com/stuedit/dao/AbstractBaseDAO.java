package com.stuedit.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 공통 DAO 기반 클래스
 *
 * MyBatis-Spring 2.1.x 에서는 SqlSessionDaoSupport.setSqlSessionFactory()에
 * @Autowired 가 없어서 자동 주입이 안 됨.
 * 이 클래스에서 @Autowired 를 오버라이드하여 모든 DAO 가 공유.
 */
public abstract class AbstractBaseDAO extends SqlSessionDaoSupport {

    @Autowired
    @Override
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }
}
