package com.dcits.comet.dao.param;

import java.lang.annotation.*;

/**
 * @Author chengliang
 * @Description //缓存数据表
 * @Date 2019-03-05 8:56
 * @Version 1.0
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamTable {

    String value();
}
