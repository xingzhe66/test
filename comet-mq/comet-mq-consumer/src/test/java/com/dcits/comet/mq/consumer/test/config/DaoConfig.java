package com.dcits.comet.mq.consumer.test.config;

import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dao.mybatis.DaoSupportImpl;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {

    @Bean(name = "daoSupport")
    public DaoSupport getParamDaoSupport(@Qualifier("shardSqlSessionTemplate") SqlSessionTemplate shardSqlSessionTemplate) {
        DaoSupportImpl daoSupport = new DaoSupportImpl();
        daoSupport.setSqlSessionTemplate(shardSqlSessionTemplate);
        return daoSupport;
    }

}
