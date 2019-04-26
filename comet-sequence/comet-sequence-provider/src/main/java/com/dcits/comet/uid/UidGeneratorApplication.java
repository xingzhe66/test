package com.dcits.comet.uid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.dcits.comet")
public class UidGeneratorApplication {

    public static void main(final String[] args) {
        SpringApplication.run(UidGeneratorApplication.class, args);
    }
}
