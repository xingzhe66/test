package com.dcits.comet.batch.annotation;

import com.dcits.comet.batch.config.BatchConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description 批量配置注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BatchConfig.class})
public @interface BatchConfiguration {
}

