package com.dcits.comet.batch.sonic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/13 15:54
 **/
@ConfigurationProperties(prefix = SonicSimpleDbLockFactory.PREFIX, ignoreUnknownFields = true)
@Data
public class SonicSimpleDbLockFactory /*extends SimpleDBLockFactory*/ {

    public static final String PREFIX = "com.dcits.sonic.lock";

    private String url;
    private String user;
    private String password;
    private String dbType;
    private String driverName;

    private boolean enable = false;
}
