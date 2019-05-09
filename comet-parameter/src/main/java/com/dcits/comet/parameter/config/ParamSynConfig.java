package com.dcits.comet.parameter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName ParamSynConfig
 * @Author huangjjg
 * @Date 2019/5/9 9:26
 * @Description ParamSynConfig
 * @Version 1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "batch.param.syn")
public class ParamSynConfig {

    private String serviceClassName;

    private String fileRevPath;

    @Value("${spring.application.name}")
    private String appName;

}
