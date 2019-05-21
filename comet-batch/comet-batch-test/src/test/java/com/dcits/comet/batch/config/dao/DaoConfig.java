package com.dcits.comet.batch.config.dao;

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

    @Bean(name = "paramDaoSupport")
    public ParamDaoSupport getParamDaoSupport(@Qualifier("shardSqlSessionTemplate") SqlSessionTemplate shardSqlSessionTemplate) {
        ParamDaoSupport paramDaoSupport = new ParamDaoSupport();
        paramDaoSupport.setSqlSessionTemplate(shardSqlSessionTemplate);
        return paramDaoSupport;
    }

    @DependsOn({"shardSqlSessionTemplate"})
    @Bean(name = "daoSupport")
    public DaoSupport getDaoSupport(@Qualifier("shardSqlSessionTemplate") SqlSessionTemplate shardSqlSessionTemplate, @Qualifier("paramDaoSupport") ParamDaoSupport paramDaoSupport) throws SQLException {
        DaoSupportImpl daoSupport = new DaoSupportImpl();
        daoSupport.setSqlSessionTemplate(shardSqlSessionTemplate);
        daoSupport.initPropertyColumnMapper();
        ParamSupportImpl paramSupport = new ParamSupportImpl(paramDaoSupport);
        return (DaoSupport) new DaoDynamicProxy().getProxyObject(daoSupport, paramSupport);
    }
}
