package com.dcits.comet.batch.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@ImportResource(value = "classpath:data-source-context.xml")
@PropertySource(value = "classpath:batch-hsql.properties")
@Configuration
public class HsqlDatasourceConfig {
}
