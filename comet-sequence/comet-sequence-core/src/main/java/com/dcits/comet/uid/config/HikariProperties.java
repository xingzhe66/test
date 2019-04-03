package com.dcits.comet.uid.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/3 9:26
 **/
@Slf4j
@Component
public class HikariProperties {

    @Value("${spring.datasource.ds-uid.hikari.connection-timeout}")
    volatile long connectionTimeout;
    @Value("${spring.datasource.ds-uid.hikari.idle-timeout}")
    volatile long idleTimeout;
    @Value("${spring.datasource.ds-uid.hikari.max-lifetime}")
    volatile long maxLifetime;
    @Value("${spring.datasource.ds-uid.hikari.maximum-pool-size}")
    volatile int maxPoolSize;
    @Value("${spring.datasource.ds-uid.hikari.minimum-idle}")
    volatile int minIdle;
    @Value("${spring.datasource.ds-uid.hikari.username}")
    volatile String username;
    @Value("${spring.datasource.ds-uid.hikari.password}")
    volatile String password;
    @Value("${spring.datasource.ds-uid.hikari.connection-test-query}")
    String connectionTestQuery;
    @Value("${spring.datasource.ds-uid.hikari.driver-class-name}")
    String driverClassName;
    @Value("${spring.datasource.ds-uid.hikari.jdbc-url}")
    String jdbcUrl;
    @Value("${spring.datasource.ds-uid.hikari.pool-name}")
    String poolName;
    @Value("${spring.datasource.ds-uid.hikari.auto-commit:true}")
    boolean isAutoCommit;
}
