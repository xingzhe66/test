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

    @Value("${ds.uid.datasource.connection-timeout}")
    volatile long connectionTimeout;
    @Value("${ds.uid.datasource.idle-timeout}")
    volatile long idleTimeout;
    @Value("${ds.uid.datasource.max-lifetime}")
    volatile long maxLifetime;
    @Value("${ds.uid.datasource.maximum-pool-size}")
    volatile int maxPoolSize;
    @Value("${ds.uid.datasource.minimum-idle}")
    volatile int minIdle;
    @Value("${ds.uid.datasource.username}")
    volatile String username;
    @Value("${ds.uid.datasource.password}")
    volatile String password;
    @Value("${ds.uid.datasource.connection-test-query}")
    String connectionTestQuery;
    @Value("${ds.uid.datasource.driver-class-name}")
    String driverClassName;
    @Value("${ds.uid.datasource.jdbc-url}")
    String jdbcUrl;
    @Value("${ds.uid.datasource.pool-name}")
    String poolName;
    @Value("${ds.uid.datasource.auto-commit:true}")
    boolean isAutoCommit;
}
