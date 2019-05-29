package com.dcits.comet.rpc.api.annotation;


import java.lang.annotation.*;

/**
 * @Author chengliang
 * @Description 服务定义
 * @Date 2019-03-05 8:56
 * @Version 1.0
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceType {
    ServiceCodeEnum value();
}
