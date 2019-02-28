package com.dcits.comet.batch.annotation;

import com.dcits.comet.batch.config.BatchConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BatchConfig.class})
public @interface BatchConfiguration {
}

