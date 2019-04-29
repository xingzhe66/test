package com.dcits.comet.batch.config.sharding;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

//@Configuration
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



}
