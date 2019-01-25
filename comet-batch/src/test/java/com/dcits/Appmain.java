package com.dcits;

import com.dcits.comet.batch.configration.BatchAConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
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
