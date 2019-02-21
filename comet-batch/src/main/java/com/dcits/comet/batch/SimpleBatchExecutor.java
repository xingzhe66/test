package com.dcits.comet.batch;

import com.dcits.comet.batch.holder.SpringContextHolder;
import com.fasterxml.jackson.core.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static java.lang.String.format;

@Component
@Deprecated
public class SimpleBatchExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleBatchExecutor.class);

    private final int CHUNK_SIZE=500;

    @Resource
    private SpringContextHolder springContextHolder;

    public JobExecution exe(String jobname,JobParameters jobParameters){
        return this.exe(jobname,jobParameters,CHUNK_SIZE);
    }


    public JobExecution exe(String jobname,JobParameters jobParameters,Integer chunkSize){

//        ConfigurableApplicationContext context= (ConfigurableApplicationContext) springContextHolder.getApplicationContext();
//
//        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getBeanFactory();
//        JobRepository jobRegistry = (JobRepository) context.getBean("jobRepository");
//        SimpleJob job = new SimpleJob();
//        job.setName("job_" + jobname);
//        job.setJobRepository(jobRegistry);
//
//        StepBuilderFactory stepBuilders = (StepBuilderFactory) context.getBean("stepBuilders");
//        //todo 因为reader等的scope设置为step，所以必须在reader等实例化之前，有一个Step，否则报错。spring batch的坑！
//        StepSynchronizationManager.register(new StepExecution("step_" + jobname, new JobExecution(123L)));
//
//
//        IBatchStep batchStep = (IBatchStep) context.getBean(jobname);
//        ItemReader reader = (ItemReader) context.getBean("reader_" + jobname);
//        ItemWriter writer = (ItemWriter) context.getBean("writer_" + jobname);
//        ItemProcessor processor = (ItemProcessor) context.getBean("processor_" + jobname);
//
//        //todo 把相关配置放在接口中传入
//        DataSourceTransactionManager dataSourceTransactionManager= context.getBean(DataSourceTransactionManager.class);
//
//        if(null==dataSourceTransactionManager){
//            LOGGER.warn("请配置数据库事务管理器！");
//        }
//
//        Step step = stepBuilders.get("step_" + jobname)
//                //.tasklet(tasklet)
//                .transactionManager(dataSourceTransactionManager)
//                //todo 把相关配置放在接口中传入
//                .chunk(chunkSize)
//                .reader(reader).faultTolerant().skip(JsonParseException.class).skipLimit(1)
//                //    .listener(new MessageItemReadListener(errorWriter))
//                .writer(writer).faultTolerant().skip(Exception.class).skipLimit(1)
//                //    .listener(new MessageWriteListener())
//                .processor(processor)
//                .build();
//       // dbf.registerSingleton("step_" + jobname,step);
//
//        job.addStep(step);
//      //  dbf.registerSingleton("job_" + jobname,job);
//
//
//        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
//
//        JobExecution jobExecution = null;
//        try {
//            //todo 增加前处理
//            batchStep.preBatchStep();
//
//            jobExecution = jobLauncher.run(job, jobParameters);
//
//            //todo 增加后处理
//            batchStep.afterBatchStep();
//
//        } catch (JobExecutionAlreadyRunningException e) {
//            e.printStackTrace();
//        } catch (JobRestartException e) {
//            e.printStackTrace();
//        } catch (JobInstanceAlreadyCompleteException e) {
//            e.printStackTrace();
//        } catch (JobParametersInvalidException e) {
//            e.printStackTrace();
//        }
//
//        if (!jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
//            throw new RuntimeException(format("%s Job execution failed.", jobname));
//        }
//
//        return jobExecution;

        return null;
    }
}
