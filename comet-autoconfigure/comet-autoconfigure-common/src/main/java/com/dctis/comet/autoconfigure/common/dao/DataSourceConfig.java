package com.dctis.comet.autoconfigure.common.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author ChengLiang
 */
@Configuration
@ConditionalOnProperty(name = "com.dcits.comet.autoconfig.datasource.isShardingDataSource", havingValue = "false")
public class DataSourceConfig {

    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return new HikariDataSource();
    }

}
