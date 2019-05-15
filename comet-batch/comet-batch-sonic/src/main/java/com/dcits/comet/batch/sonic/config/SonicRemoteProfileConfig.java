package com.dcits.comet.batch.sonic.config;

import com.dcits.sonic.api.config.RemoteProfile;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/13 14:38
 **/
@ConfigurationProperties(prefix = SonicRemoteProfileConfig.PREFIX, ignoreUnknownFields = true)
public class SonicRemoteProfileConfig extends RemoteProfile {
    public static final String PREFIX = "com.dcits.sonic.remote";
}
