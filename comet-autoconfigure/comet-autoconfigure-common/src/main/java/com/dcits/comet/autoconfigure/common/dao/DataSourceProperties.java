package com.dcits.comet.autoconfigure.common.dao;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName DataSourceProperties
 * @Author guanlt
 * @Date 2019/5/21 18:02
 * @Description 数据库连接自动加载
 * @Version 1.0
 **/
@Data
@ConfigurationProperties(prefix = DataSourceProperties.PREFIX, ignoreUnknownFields = true)
public class DataSourceProperties {

    public static final String PREFIX = "com.dcits.comet.autoconfig.datasource";

    /**  
    * @Author guanlt  
    * @Description 数据源前缀，多个以逗号隔开
    * @Date 2019/5/23   
    * @Param
    * @return   
    **/ 
    private String dataSourcePrefixs;

    /**  
    * @Author guanlt  
    * @Description 默认数据库算法策略类全路径
    * @Date 2019/5/22
    * @Param
    * @return   
    **/ 
    private String defaultStrategy;

    /**
     * @Author guanlt
     * @Description 分库算法策略类全路径
     * @Date 2019/5/22
     * @Param
     * @return
     **/
    private String dataSourceStrategy;

    /**  
    * @Author guanlt  
    * @Description 分库键
    * @Date 2019/5/22   
    * @Param
    * @return   
    **/ 
    private String shardingColumn;
}
