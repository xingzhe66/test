package com.dcits.comet.batch.sonic.config;

import com.dcits.sonic.executor.lock.db.SimpleDBLockFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/13 15:54
 **/
@ConfigurationProperties(prefix = SonicSimpleDBLockFactory.PREFIX, ignoreUnknownFields = true)
@Data
public class SonicSimpleDBLockFactory extends SimpleDBLockFactory {

    public static final String PREFIX = "com.dcits.sonic.lock";
   
    private boolean enable = false;
}
