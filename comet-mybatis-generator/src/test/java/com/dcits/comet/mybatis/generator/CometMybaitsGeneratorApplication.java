package com.dcits.comet.mybatis.generator;

import com.dcits.comet.mybatis.generator.entity.GeneratorProperties;
import com.dcits.comet.mybatis.generator.service.CodeGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@MapperScan("com.dcits.comet.mybatis.generator.mapper") //扫描的mapper
@SpringBootApplication
@Slf4j
public class CometMybaitsGeneratorApplication implements CommandLineRunner {
    @Autowired
    CodeGeneratorService codeGeneratorService;

    public static void main(String[] args) {
        SpringApplication.run(CometMybaitsGeneratorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        InputStream in =null;
        try {
            Properties prop = new Properties();
            //获取当前类的包名
            String packageName = CometMybaitsGeneratorApplication.class.getPackage().getName();
            //获取当前类的路径
            String path= this.getClass().getResource("/").getPath();
            path=path.substring(1,path.indexOf("/target"));
            //读取属性文件generator.properties
            in = new BufferedInputStream(new FileInputStream(path+"/src/test/resources/config/generator.properties"));
            prop.load(in);     ///加载属性列表
            GeneratorProperties generatorProperties =new GeneratorProperties(prop);
            generatorProperties.setBasedir(path);
            generatorProperties.setBasePackage(packageName);
            codeGeneratorService.createCode(generatorProperties);
            log.info("生成代码成功，请刷新该模块");
        } catch (Exception e) {
            log.info("生成代码失败...............");
            e.printStackTrace();
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
