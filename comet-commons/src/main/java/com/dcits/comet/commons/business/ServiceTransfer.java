package com.dcits.comet.commons.business;

import java.lang.annotation.*;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description 服务码转换注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceTransfer {
    ServiceCodeEnum serviceCode();
    MessageTypeEnum messageType();
    String messageCode();
    String uri();
}
