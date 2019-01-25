package com.dcits;

import com.dcits.comet.batch.configration.BatchAConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = WebMvcConfigurer.class)
@ComponentScan(basePackages = { "com.dcits"})
@EnableBatchProcessing

/**
 * EnableBatchProcessing
 * 会自动注入以下bean
 JobRepository bean 名称 "jobRepository"
 JobLauncher bean名称"jobLauncher"
 JobRegistry bean名称"jobRegistry"
 PlatformTransactionManager bean名称 "transactionManager"
 JobBuilderFactory bean名称"jobBuilders"
 StepBuilderFactory bean名称"stepBuilders"
 *
 * 也就是当配置文件里定义spring.batch.job.enabled为true，
 * 或者没定义（默认为true）的时候，会初始化一个
 * JobLauncherCommandLineRunner的bean。
 * 也就是只要能找到ApplicationRunner或者CommandLineRunner的子类，就挨个执行。
 *
 *这个注解可能会导致Job的多次执行，需要注意。
 *
 * TODO：后续将配置拆出来，不允许使用此注解。
 */
public class Appmain  {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Appmain.class);
//   @Autowired
//   private ApplicationContext applicationContext;

//    @Autowired
//   private SimpleJobLauncher jobLauncher;

//    @Autowired
//    private  Job job;

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(
                Appmain.class, args)));

//        new SpringApplicationBuilder(Appmain.class)
//                .web(WebApplicationType.NONE)
//                .run(args);
//

//        SpringApplication app =new SpringApplication(Appmain.class);
//        app.setWebApplicationType(WebApplicationType.NONE);
//        app.run(args);

    }

//    @Override
//    public void run(String... args) throws Exception {
//        JobParameters jobParameters=new JobParametersBuilder()
//                .addDate("date", new Date())
//                .toJobParameters();
//        jobLauncher2.run(job,jobParameters);
//
//    }
}
