package com.dcits.comet.dao.annotation;

import java.lang.annotation.*;

/**
 * @Author chengliang
 * @Description //缓存数据表
 * @Date 2019-03-05 8:56
 * @Version 1.0
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableType {
    /* *
        * 表名
        **/
    String name();
    /* *
       *表类型PARAM:参数表    LEVEL:水平分库表     UPRIGHT:垂直分库表
        **/
    TableTypeEnum value();
}
