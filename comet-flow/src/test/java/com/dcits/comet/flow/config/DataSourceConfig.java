package com.dcits.comet.flow.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @ClassName DataSourceConfig
 * @Author guihj
 * @Date 2019/4/10 13:37
 * @Description TODO
 * @Version 1.0
 **/

@Configuration
public class DataSourceConfig {

    @Bean(name="shardingDataSource")
    @ConfigurationProperties(prefix="spring.datasource.mq.hikari")
    public DataSource getDataSource() {
        return  new HikariDataSource();
    }
}
