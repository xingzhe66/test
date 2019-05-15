package com.dcits.comet.uid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

/**
 * @author leijian
 * @date 2019年4月5日
 */
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = {"com.dcits.comet.uid.entity"}, basePackageClasses = Jsr310JpaConverters.class)
public class UidGeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(UidGeneratorApplication.class, args);
    }
}

