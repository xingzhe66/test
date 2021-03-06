package com.dcits.comet.batch.sonic.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author ChengLiang
 */
@Configuration
public class DatasourceConfig {


    @ConfigurationProperties(prefix = "spring.datasource.ds-0.hikari")
    @Bean(name = "ds_0")
    public DataSource dataSource0() {
        return new HikariDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.ds-1.hikari")
    @Bean(name = "ds_1")
    public DataSource dataSource1() {
        return new HikariDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.ds-2.hikari")
    @Bean(name = "ds_2")
    public DataSource dataSource2() {
        return new HikariDataSource();
    }


}
