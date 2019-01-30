package com.dcits;

import com.dcits.comet.batch.SimpleBatchExecutor;
import com.dcits.comet.batch.IBatch;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

import static java.lang.String.format;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.dcits"})
public class BatchApplication {

    public static void main(String[] args) {

        try {
            ConfigurableApplicationContext context = SpringApplication.run(BatchApplication.class, args);
            String[] names = context.getBeanNamesForType(IBatch.class);

            for (String name : names) {

                SimpleBatchExecutor batchExecutor = (SimpleBatchExecutor) context.getBean("simpleBatchExecutor");
                batchExecutor.exe(name,createJobParams());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(format("%s Job execution failed."));
        }
    }

    private static JobParameters createJobParams() {
        return new JobParametersBuilder()
                .addDate("date", new Date())
                .addString("name","wang yun")
                .toJobParameters()
                ;



    }

}
