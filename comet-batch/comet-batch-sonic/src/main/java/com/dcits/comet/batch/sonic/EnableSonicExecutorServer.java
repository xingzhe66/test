package com.dcits.comet.batch.sonic;

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
@Import({SonicExecutorServerRegistrar.class})
public @interface EnableSonicExecutorServer {
}
