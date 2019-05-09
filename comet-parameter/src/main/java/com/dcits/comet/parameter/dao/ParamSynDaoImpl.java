package com.dcits.comet.parameter.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @ClassName ParamSynDaoImpl
 * @Author huangjjg
 * @Date 2019/5/9 14:17
 * @Description ParamSynDaoImpl
 * @Version 1.0
 **/
@Slf4j
public class ParamSynDaoImpl implements ParamSynDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void exeSql(String sql) {
        jdbcTemplate.execute(sql);
    }
}
