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
//        new SpringApplicationBuilder(BatchApplication.class)
//                .web(WebApplicationType.NONE)
//                .run(args);
        String jobName = "";

        try {
            ConfigurableApplicationContext context = SpringApplication.run(BatchApplication.class, args);
            String[] names = context.getBeanNamesForType(IBatch.class);

            for (String name : names) {

                BatchExecutor batchExecutor = (BatchExecutor) context.getBean("batchExecutor");
                batchExecutor.exe(context,name);
                Job job = (Job) context.getBean("job_"+name);

                JobLauncher jobLauncher = context.getBean(JobLauncher.class);
                JobExecution jobExecution = jobLauncher.run(job, createJobParams());
                if (!jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                    throw new RuntimeException(format("%s Job execution failed.", jobName));
                }


//
//
//
//
//              //  if(name.equals("cBatch")) continue;
//                IBatch batch = (IBatch) context.getBean(name);
//
//                DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getBeanFactory();
//
//                BeanDefinitionBuilder readerBuider = BeanDefinitionBuilder.genericBeanDefinition(Reader.class);
//                //向里面的属性注入值，提供get set方法
//                readerBuider.addPropertyValue("batch", batch);
//                //dataSourceBuider.setParentName("");  同配置 parent
//                readerBuider.setScope("step");   //同配置 scope
//                //将实例注册spring容器中   bs 等同于  id配置
//                dbf.registerBeanDefinition("reader_" + name, readerBuider.getBeanDefinition());
//
//                BeanDefinitionBuilder writerBuider = BeanDefinitionBuilder.genericBeanDefinition(Writer.class);
//                //向里面的属性注入值，提供get set方法
//                writerBuider.addPropertyValue("batch", batch);
//                //dataSourceBuider.setParentName("");  同配置 parent
//                writerBuider.setScope("step");  // 同配置 scope
//                //将实例注册spring容器中   bs 等同于  id配置
//                dbf.registerBeanDefinition("writer_" + name, writerBuider.getBeanDefinition());
//
//
//                BeanDefinitionBuilder processorBuider = BeanDefinitionBuilder.genericBeanDefinition(Processor.class);
//                //向里面的属性注入值，提供get set方法
//                processorBuider.addPropertyValue("batch", batch);
//                //dataSourceBuider.setParentName("");  同配置 parent
//                processorBuider.setScope("step");   //同配置 scope
//                //将实例注册spring容器中   bs 等同于  id配置
//                dbf.registerBeanDefinition("processor_" + name, processorBuider.getBeanDefinition());
//
//
//                StepSynchronizationManager.register(new StepExecution("step_" + name, new JobExecution(123L)));
//
//                JobRepository jobRegistry = (JobRepository) context.getBean("jobRepository");
//                SimpleJob job = new SimpleJob();
//                job.setName("job_" + name);
//                job.setJobRepository(jobRegistry);
//
//                StepBuilderFactory stepBuilders = (StepBuilderFactory) context.getBean("stepBuilders");
//
//                ItemReader reader = (ItemReader) context.getBean("reader_" + name);
//                ItemWriter writer = (ItemWriter) context.getBean("writer_" + name);
//                ItemProcessor processor = (ItemProcessor) context.getBean("processor_" + name);
//
//                Step step = stepBuilders.get("step_" + name)
//                        .<SysLog, SysLog>chunk(500)
//                        .reader(reader).faultTolerant().skip(JsonParseException.class).skipLimit(1)
//                        //    .listener(new MessageItemReadListener(errorWriter))
//                        .writer(writer).faultTolerant().skip(Exception.class).skipLimit(1)
//                        //    .listener(new MessageWriteListener())
//                        .processor(processor)
//                        .build();
//              //  dbf.registerSingleton("step_" + name,step);
//
//                job.addStep(step);
//               // dbf.registerSingleton("job_" + name,job);
//
//
//                //Job job = jobRegistry.getJob("job1");
//                //Job job = (Job) context.getBean("job1");
//                JobLauncher jobLauncher = context.getBean(JobLauncher.class);
//                JobExecution jobExecution = jobLauncher.run(job, createJobParams());
//                if (!jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
//                    throw new RuntimeException(format("%s Job execution failed.", jobName));
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(format("%s Job execution failed.", jobName));
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
