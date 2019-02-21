package com.dcits;

import com.dcits.comet.batch.IBatchStep;
import com.dcits.comet.batch.ITaskletStep;
import com.dcits.comet.batch.SimpleBatchExecutor;
import com.dcits.comet.batch.SimpleTaskletStepExecutor;
import com.dcits.comet.batch.processor.Processor;
import com.dcits.comet.batch.reader.Reader;
import com.dcits.comet.batch.writer.Writer;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.Date;

import static java.lang.String.format;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.dcits"})
public class BatchServiceApplication {

    public static void main(String[] args) {

        try {
            ConfigurableApplicationContext context = SpringApplication.run(BatchServiceApplication.class, args);
            String[] names = context.getBeanNamesForType(IBatchStep.class);
            JobParameters jobParameters=createJobParams();
            DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getBeanFactory();

            for (String name : names) {
                //Bean构建
                BeanDefinitionBuilder readerBuider = BeanDefinitionBuilder.genericBeanDefinition(Reader.class);
                //向里面的属性注入值，提供get set方法
                readerBuider.addPropertyReference("batchStep",name); //因为实例还未生成，所以只定义引用；
                //todo 把相关配置放在接口中传入
                readerBuider.addPropertyValue("pageSize","1000");

                //.addPropertyValue("batch", batch);
                readerBuider.setScope("step");   //作用域为step，为了让jobParameters注解生效
                //将实例注册spring容器中   bs 等同于  id配置
                dbf.registerBeanDefinition("reader_" + name, readerBuider.getBeanDefinition());

                BeanDefinitionBuilder writerBuider = BeanDefinitionBuilder.genericBeanDefinition(Writer.class);
                writerBuider.addPropertyReference("batchStep",name);
                writerBuider.setScope("step");
                dbf.registerBeanDefinition("writer_" + name, writerBuider.getBeanDefinition());


                BeanDefinitionBuilder processorBuider = BeanDefinitionBuilder.genericBeanDefinition(Processor.class);
                processorBuider.addPropertyReference("batchStep", name);
                processorBuider.setScope("step");
                dbf.registerBeanDefinition("processor_" + name, processorBuider.getBeanDefinition());

                JobRepository jobRegistry = (JobRepository) context.getBean("jobRepository");
                SimpleJob job = new SimpleJob();
                job.setName("job_" + name);
                job.setJobRepository(jobRegistry);

                StepBuilderFactory stepBuilders = (StepBuilderFactory) context.getBean("stepBuilders");

                //todo 因为reader等的scope设置为step，所以必须在reader等实例化之前，有一个Step，否则报错。spring batch的坑！

                StepSynchronizationManager.register(new StepExecution("step_" + name, new JobExecution(123L)));

                IBatchStep batchStep = (IBatchStep) context.getBean(name);
                ItemReader reader = (ItemReader) context.getBean("reader_" + name);
                ItemWriter writer = (ItemWriter) context.getBean("writer_" + name);
                ItemProcessor processor = (ItemProcessor) context.getBean("processor_" + name);


                //todo 把相关配置放在接口中传入
                DataSourceTransactionManager dataSourceTransactionManager= context.getBean(DataSourceTransactionManager.class);

                if(null==dataSourceTransactionManager){
                   // LOGGER.warn("请配置数据库事务管理器！");
                }

                Step step = stepBuilders.get("step_" + name)
                        //.tasklet(tasklet)
                        .transactionManager(dataSourceTransactionManager)
                        //todo 把相关配置放在接口中传入
                        .chunk(500)
                        .reader(reader).faultTolerant().skip(JsonParseException.class).skipLimit(1)
                        //    .listener(new MessageItemReadListener(errorWriter))
                        .writer(writer).faultTolerant().skip(Exception.class).skipLimit(1)
                        //    .listener(new MessageWriteListener())
                        .processor(processor)
                        .build();
                // dbf.registerSingleton("step_" + jobname,step);

                job.addStep(step);
                //  dbf.registerSingleton("job_" + jobname,job);


                JobLauncher jobLauncher = context.getBean(JobLauncher.class);

                JobExecution jobExecution = null;
                try {
                    //todo 增加前处理
                    batchStep.preBatchStep();

                    jobExecution = jobLauncher.run(job, jobParameters);

                    //todo 增加后处理
                    batchStep.afterBatchStep();

                } catch (JobExecutionAlreadyRunningException e) {
                    e.printStackTrace();
                } catch (JobRestartException e) {
                    e.printStackTrace();
                } catch (JobInstanceAlreadyCompleteException e) {
                    e.printStackTrace();
                } catch (JobParametersInvalidException e) {
                    e.printStackTrace();
                }

                if (!jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                    throw new RuntimeException(format("%s Job execution failed.", name));
                }

//                SimpleBatchExecutor batchExecutor = (SimpleBatchExecutor) context.getBean("simpleBatchExecutor");
//                JobExecution a = batchExecutor.exe(name, jobParameters);
//                jobParameters=a.getJobParameters();
            }

//            String[] taskletnames = context.getBeanNamesForType(ITaskletStep.class);
//
//            for (String name : taskletnames) {
//
//                SimpleTaskletStepExecutor batchExecutor = (SimpleTaskletStepExecutor) context.getBean("simpleTaskletStepExecutor");
//                batchExecutor.exe(name,jobParameters);
//            }



        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(format("Job execution failed."));
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
