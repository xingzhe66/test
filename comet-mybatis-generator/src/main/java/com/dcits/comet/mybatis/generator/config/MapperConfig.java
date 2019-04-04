package com.dcits.comet.mybatis.generator.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MapperConfig
 * @Author guihj
 * @Date 2019/4/4 11:28
 * @Description TODO
 * @Version 1.0
 **/
@Configuration
@MapperScan("com.dcits.comet.mybatis.generator.mapper") //扫描的mapper
public class MapperConfig {
}
