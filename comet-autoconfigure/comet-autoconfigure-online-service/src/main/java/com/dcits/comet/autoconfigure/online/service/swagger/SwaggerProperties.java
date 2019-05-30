package com.dcits.comet.autoconfigure.online.service.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName SwaggerProperties
 * @Author guanlt
 * @Date 2019/5/22 18:51
 * @Description swagger读取配置文件
 * @Version 1.0
 **/
@Data
@ConfigurationProperties(prefix = SwaggerProperties.PREFIX)
public class SwaggerProperties {

    public static final String PREFIX = "com.dcits.comet.autoconfig.swagger";

    /**
    * @Author guanlt
    * @Description Swagger标题
    * @Date 2019/5/22
    * @Param
    * @return
    **/
    private String title;
}
