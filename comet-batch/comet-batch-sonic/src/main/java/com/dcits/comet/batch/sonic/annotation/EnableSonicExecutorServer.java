package com.dcits.comet.batch.sonic.annotation;


import com.dcits.comet.batch.sonic.config.SonicClientProfileConfig;
import com.dcits.comet.batch.sonic.config.SonicExecutorServerRegistrar;
import com.dcits.comet.batch.sonic.config.SonicRemoteProfileConfig;
import com.dcits.comet.batch.sonic.config.SonicSimpleDbLockFactory;
//import com.dcits.sonic.executor.spring.support.SoincSpringApplicationContext;
import com.dcits.sonic.executor.spring.support.SoincSpringApplicationContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 16:04
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConfigurationProperties({SonicClientProfileConfig.class, SonicRemoteProfileConfig.class, SonicSimpleDbLockFactory.class})
@Import({SonicExecutorServerRegistrar.class, SoincSpringApplicationContext.class})
public @interface EnableSonicExecutorServer {

}
