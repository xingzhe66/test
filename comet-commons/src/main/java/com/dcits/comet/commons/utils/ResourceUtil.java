package com.dcits.comet.commons.utils;


import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import com.dcits.comet.commons.exception.ConfigException;


public class ResourceUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceUtil.class);
    public final static String PREFIX_CONFIG = "config/";


    public static Resource[] getResource(String path, String prefix) {
        Resource[] locations;
        try {
            locations = new PathMatchingResourcePatternResolver().getResources(path);
        } catch (Exception e) {
            throw new ConfigException("加载错误码配置文件失败", e);
        }
        return locations;
    }


    /**
     * 加载自定义路径的配置文件
     *
     * @param configKeys
     * @return
     */
    public static Resource[] getResource(List<String> configKeys) {
        Resource[] locations = null;
        try {
            locations = new Resource[configKeys.size()];
            for (int i = 0; i < configKeys.size(); i++) {
                    locations[i] = new PathMatchingResourcePatternResolver().getResource(configKeys.get(i));
            }

        } catch (Exception e) {
            throw new ConfigException("加载错误码配置文件失败", e);
        }
        return locations;
    }
}
