package com.zhan.org.eurekaservicer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaservicerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaservicerApplication.class, args);
    }

}
