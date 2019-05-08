package com.dcits.comet.uid.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/5 20:46
 **/
@ConfigurationProperties(prefix = "ds.uid.datasource", ignoreUnknownFields = false)
@Data
public class UidGeneratorProperties {

    @Autowired
    private Environment environment;

    private volatile long connectionTimeout;

    private volatile long idleTimeout;

    private volatile long maxLifetime;

    private volatile int maxPoolSize;

    private volatile int minIdle;

    private volatile String username;

    private volatile String password;

    private String connectionTestQuery;

    private String driverClassName;

    private String jdbcUrl;

    private String poolName;

    private boolean autoCommit;

}
