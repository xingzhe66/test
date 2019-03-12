package com.dcits.comet.commons.business;


import java.lang.annotation.*;

/**
 * @Author chengliang
 * @Description 服务定义
 * @Date 2019-03-05 8:56
 * @Version 1.0
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceType {
    ServiceTypeEnum value();
}
