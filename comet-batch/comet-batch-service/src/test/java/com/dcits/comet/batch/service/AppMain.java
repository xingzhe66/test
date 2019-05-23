package com.dcits.comet.batch.service;


import com.dcits.comet.batch.annotation.BatchConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {BatchAutoConfiguration.class})
//@EnableBatchProcessing
@ComponentScan(basePackages = {"com.dcits.comet"})
@BatchConfiguration
public class AppMain {
    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
    }
}
