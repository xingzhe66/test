package com.dcits.comet.autoconfigure.online.service.webmvc;

import com.dcits.comet.mvc.filter.HttpServletRequestReplacedFilter;
import com.dcits.comet.mvc.request.BusinessRequestInterceptorImpl;
import com.dcits.comet.autoconfigure.common.exception.AutoConfigException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName DataSourceProperties
 * @Author guanlt
 * @Date 2019/5/21 18:02
 * @Description 自定义拦截器自动加载
 * @Version 1.0
 **/
@Configuration
@ConditionalOnProperty(name = "com.dcits.comet.autoconfig.mvc.isEnable", havingValue = "true")
@EnableConfigurationProperties({WebMvcProperties.class})
public class WebMvcAutoConfig implements WebMvcConfigurer {

    @Autowired
    WebMvcProperties mvcProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String pathPatterns = mvcProperties.getPathPatterns();
        if (StringUtils.isBlank(pathPatterns)) {
            throw new AutoConfigException("pathPatterns is empty");
        }
        String[] pathPatternArray = pathPatterns.split(",");
        registry.addInterceptor(new BusinessRequestInterceptorImpl()).addPathPatterns(pathPatternArray);
    }

    @Bean
    public FilterRegistrationBean httpServletRequestReplacedRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestReplacedFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("httpServletRequestReplacedFilter");
        registration.setOrder(1);
        return registration;
    }
}
