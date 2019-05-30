package com.dcits.comet.autoconfigure.online.service.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author chengliang
 * @Description 产品及定价
 * @Date 2019-02-27 18:50
 * @Version 1.0
 **/
@Configuration
@EnableConfigurationProperties({SwaggerProperties.class})
@ConditionalOnProperty(name = "com.dcits.comet.autoconfig.swagger.isEnable", havingValue = "true")
public class Swagger2Config {
    /**
     * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     *
     * @return Docket
     */
    @Bean
    public Docket createRestApi(final SwaggerProperties swaggerProperties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select()
                .build()
                .apiInfo(apiInfo(swaggerProperties));
    }

    /**
     * api文档的详细信息函数,注意这里的注解引用的是哪个
     *
     * @return
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                // //大标题
                .title(swaggerProperties.getTitle())
                // 版本号
                .version("1.0")
//                .termsOfServiceUrl("NO terms of service")
                // 描述
                .description("API 描述")
                //作者
                .contact(new Contact("chengliang", "https://blog.csdn.net/m0_37726449", "chengliang@dcits.com"))
//                .license("The Apache License, Version 2.0")
//                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }
}
