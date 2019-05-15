package com.dcits.comet.batch.sonic.config;

import com.dcits.sonic.api.config.ClientProfile;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 16:07
 **/
@ConfigurationProperties(prefix = SonicClientProfileConfig.PREFIX, ignoreUnknownFields = true)
public class SonicClientProfileConfig extends ClientProfile {
    public static final String PREFIX = "com.dcits.sonic.client";

}
