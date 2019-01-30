package com.dcits.comet.batch;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BatchExecutor {

    public void exe(ConfigurableApplicationContext context,String jobname){
        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getBeanFactory();
        JobRepository jobRegistry = (JobRepository) context.getBean("jobRepository");
        SimpleJob job = new SimpleJob();
        job.setName("job_" + jobname);
        job.setJobRepository(jobRegistry);

        StepBuilderFactory stepBuilders = (StepBuilderFactory) context.getBean("stepBuilders");
        //todo 因为reader等的scope设置为step，所以必须在reader等实例化之前，有一个Step，否则报错。spring batch的坑！
        StepSynchronizationManager.register(new StepExecution("step_" + jobname, new JobExecution(123L)));

        ItemReader reader = (ItemReader) context.getBean("reader_" + jobname);
        ItemWriter writer = (ItemWriter) context.getBean("writer_" + jobname);
        ItemProcessor processor = (ItemProcessor) context.getBean("processor_" + jobname);

        Step step = stepBuilders.get("step_" + jobname)
                .chunk(5)
                .reader(reader).faultTolerant().skip(JsonParseException.class).skipLimit(1)
                //    .listener(new MessageItemReadListener(errorWriter))
                .writer(writer).faultTolerant().skip(Exception.class).skipLimit(1)
                //    .listener(new MessageWriteListener())
                .processor(processor)
                .build();
        dbf.registerSingleton("step_" + jobname,step);

        job.addStep(step);
        dbf.registerSingleton("job_" + jobname,job);

    }
}
