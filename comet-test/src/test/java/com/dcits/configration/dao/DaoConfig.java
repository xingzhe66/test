package com.dcits.configration.dao;

import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dao.mybatis.DaoSupportImpl;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.sql.SQLException;

@Configuration
//@PropertySource("classpath:activemq/activemq.properties")
//@ImportResource({ "classpath:.xml" })
public class DaoConfig {

    @DependsOn({ "shardSqlSessionTemplate"})
    @Bean(name = "daoSupport",initMethod = "initPropertyColumnMapper")
    public DaoSupport getDaoSupport(@Qualifier("shardSqlSessionTemplate") SqlSessionTemplate shardSqlSessionTemplate) throws SQLException {
        DaoSupportImpl daoSupport=new DaoSupportImpl();
        daoSupport.setSqlSessionTemplate(shardSqlSessionTemplate);
        return daoSupport;
    }
}
