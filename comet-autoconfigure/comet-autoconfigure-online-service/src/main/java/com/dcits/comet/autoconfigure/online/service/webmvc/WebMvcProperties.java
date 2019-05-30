package com.dcits.comet.autoconfigure.online.service.webmvc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @ClassName WebMvcProperties
 * @Author guanlt
 * @Date 2019/5/21 18:02
 * @Description 自定义拦截器自动加载配置文件
 * @Version 1.0
 **/
@Data
@ConfigurationProperties(prefix = WebMvcProperties.PREFIX)
public class WebMvcProperties {
    public static final String PREFIX = "com.dcits.comet.autoconfig.mvc";

    /**
    * @Author guanlt
    * @Description 需要拦截的路径，多个以逗号隔开
    * @Date 2019/5/21
    * @Param
    * @return
    **/
    private String pathPatterns;
}
