package com.dcits.comet.uid;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/1 17:26
 **/
@SpringBootApplication(scanBasePackages = {"com.dcits.comet"})
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @ConfigurationProperties(prefix = "spring.datasource.ds-0.hikari")
    @Bean(name = "ds_uid")
    public DataSource dataSource0() {
        log.info("123");
        return new HikariDataSource();
    }


}
