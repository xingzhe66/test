package com.dcits.comet.flow.config;

import com.dcits.comet.dao.DaoDynamicProxy;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dao.mybatis.DaoSupportImpl;
import com.dcits.comet.dao.param.ParamDaoSupport;
import com.dcits.comet.dao.param.ParamSupportImpl;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.sql.SQLException;

@Configuration
public class DaoConfig {
    @Bean(name = "daoSupport")
    public DaoSupport getParamDaoSupport(@Qualifier("shardSqlSessionTemplate") SqlSessionTemplate shardSqlSessionTemplate) {
        DaoSupportImpl daoSupport = new DaoSupportImpl();
        daoSupport.setSqlSessionTemplate(shardSqlSessionTemplate);
        return daoSupport;
    }
}
