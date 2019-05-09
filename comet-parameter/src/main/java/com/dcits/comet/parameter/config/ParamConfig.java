package com.dcits.comet.parameter.config;

import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.parameter.dao.ParamSynDao;
import com.dcits.comet.parameter.dao.ParamSynDaoImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @ClassName ParamConfig
 * @Author huangjjg
 * @Date 2019/5/9 14:15
 * @Description ParamConfig
 * @Version 1.0
 **/
@Configuration
public class ParamConfig {

    @Bean
    public ParamSynDao paramSynDao(){
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        JdbcTemplate jdbcTemplate = (JdbcTemplate)context.getBean("jdbcTemplate");
        ParamSynDaoImpl paramSynDao = new ParamSynDaoImpl();
        paramSynDao.setJdbcTemplate(jdbcTemplate);
        return paramSynDao;
    }
}
