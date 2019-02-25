package com.dcits.comet.mybatis.generator;

import com.dcits.comet.mybatis.generator.service.CodeGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

//@MapperScan("com.dcits.comet.mybatis.generator.mapper") //扫描的mapper
@SpringBootApplication

public class CometMybaitsGeneratorApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CometMybaitsGeneratorApplication.class);
    @Autowired
    CodeGeneratorService codeGeneratorService;

    public static void main(String[] args) {
        SpringApplication.run(CometMybaitsGeneratorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            codeGeneratorService.createCode();
            LOGGER.info("生成代码成功");
        } catch (Exception e) {
            LOGGER.info("生成代码失败");
            e.printStackTrace();
        }
    }
}
