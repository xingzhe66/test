package com.dcits.comet.commons.business;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceTransfer {
    ServiceCodeEnum serviceCode();
    MessageTypeEnum messageType();
    String messageCode();
    String uri();
}
