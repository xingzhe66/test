package com.dcits.comet.batch.launcher;

import com.dcits.comet.batch.BatchBeanFactory;
import com.dcits.comet.batch.IBatchStep;
import com.dcits.comet.batch.IStep;
import com.dcits.comet.batch.ITaskletStep;
import com.dcits.comet.batch.exception.BatchException;
import com.dcits.comet.batch.listener.StepExeListener;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.param.BatchContextManager;
import com.fasterxml.jackson.core.JsonParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**

 */
@Component("commonJobLauncher")
public class CommonJobLauncher implements IJobLauncher {

    protected static final Log LOGGER = LogFactory.getLog(CommonJobLauncher.class);
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    ConfigurableApplicationContext context;


    public JobExeResult run(String jobName, JobParam jobParam) {

        JobExecution jobExecution = null;
        JobExeResult jobExeResult =new JobExeResult();

        int pageSize = jobParam.getPageSize();
        int chunkSize = jobParam.getChunkSize();
        String jobId = jobParam.getJobId();
        int beginIndex = jobParam.getBeginIndex();
        int endIndex = jobParam.getEndIndex();
        BatchContext batchContext = jobParam.getBatchContext();

        try {


            if (null == batchContext) {
                batchContext = new BatchContext();
            }

            JobParameters jobParameters = createJobParams(jobId, jobName);

            StepBuilderFactory stepBuilders = (StepBuilderFactory) context.getBean("stepBuilders");

            //todo 因为reader等的scope设置为step，所以必须在reader等实例化之前，有一个Step，否则报错。spring batch的坑！

            StepSynchronizationManager.register(new StepExecution("step_" + jobName, new JobExecution(123L)));


            IStep stepObj = (IStep) context.getBean(jobName);
            //todo 把相关配置放在接口中传入
            DataSourceTransactionManager dataSourceTransactionManager = (DataSourceTransactionManager) context.getBean("batchTransactionManager");
            Step step =null;
            if(stepObj instanceof IBatchStep) {
                //todo spring bean是单例可能有并发问题，后续可能改为new对象。
                ItemReader reader = BatchBeanFactory.getReader(jobName, pageSize, beginIndex, endIndex);
                ItemWriter writer = BatchBeanFactory.getWriter(jobName);
                ItemProcessor processor = BatchBeanFactory.getProcessor(jobName);

                StepExeListener stepListener=new StepExeListener();
                stepListener.setBatchStep((IBatchStep)stepObj);

                if (null == dataSourceTransactionManager) {
                    LOGGER.warn("请配置数据库事务管理器！");
                }
                step = stepBuilders.get("step_" + jobName)
                        //.tasklet(tasklet)
                        .listener(stepListener)
                        .transactionManager(dataSourceTransactionManager)
                        .chunk(chunkSize)
                        .reader(reader).faultTolerant().skip(JsonParseException.class).skipLimit(1)
                        //    .listener(new MessageItemReadListener(errorWriter))
                        .writer(writer).faultTolerant().skip(Exception.class).skipLimit(1)
                        //    .listener(new MessageWriteListener())
                        .processor(processor)
                        .build();

            } else if(stepObj instanceof ITaskletStep) {

                ITaskletStep taskletStep = (ITaskletStep) stepObj;

                step = stepBuilders.get("step_" + jobName)
                        .transactionManager(dataSourceTransactionManager)
                        .tasklet(taskletStep)
                        .build();
            } else {
                throw new BatchException("不支持的Step类型");

            }

            SimpleJob job = new SimpleJob();
            job.setName("job_" + jobName);
            job.setJobRepository(jobRepository);

            job.addStep(step);

            JobLauncher jobLauncher = context.getBean(JobLauncher.class);

            try {

                BatchContextManager.getInstance().putBatchContext(jobId, batchContext);
                jobExecution = jobLauncher.run(job, jobParameters);


            } catch (JobExecutionAlreadyRunningException e) {
                throw e;
            } catch (JobRestartException e) {
                throw e;
            } catch (JobInstanceAlreadyCompleteException e) {
                throw e;
            } catch (JobParametersInvalidException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            }

            if ((null != jobExecution) && (!jobExecution.getExitStatus().equals(ExitStatus.COMPLETED))) {
                throw new BatchException("job执行返回值为空，jobId："+jobId);
            }
            jobExeResult.setJobExecution(jobExecution);
            jobExeResult.setJobId(jobParam.getJobId());
            jobExeResult.setBatchContext(BatchContextManager.getInstance().getBatchContext(jobParam.getJobId()));
            jobExeResult.setJobName(jobParam.getJobName());


        } catch (Exception e) {
            e.printStackTrace();
            throw new BatchException(e.getMessage(),e);
        } finally {
            //清理context
            BatchContextManager.getInstance().clear(jobId);
        }

        BatchContextManager.getInstance().clear(jobParam.getJobId());

        return jobExeResult;
    }


    private JobParameters createJobParams(String jobId, String jobName) {
        return new JobParametersBuilder()
//                    .addDate("date", new Date())
                .addString("jobId", jobId)
                .addString("jobName", jobName)
                .toJobParameters()
                ;
    }
}
