package com.dcits;

import com.dcits.comet.batch.BatchExecutor;
import com.dcits.comet.batch.IBatch;
import com.dcits.comet.batch.processor.Processor;
import com.dcits.comet.batch.reader.Reader;
import com.dcits.comet.batch.writer.Writer;
import com.dcits.yunyun.entity.SysLog;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowStep;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

                BatchExecutor batchExecutor = (BatchExecutor) context.getBean("batchExecutor");
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
