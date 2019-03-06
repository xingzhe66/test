package com.dcits.comet.batch.launcher;

import com.dcits.comet.batch.exception.BatchException;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.param.BatchContextManager;
import com.dcits.comet.batch.step.StepFactory;
import com.dcits.comet.batch.step.StepParam;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.task.TaskExecutor;
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

        String jobId = jobParam.getJobId();

        BatchContext batchContext = jobParam.getBatchContext();

        try {


            if (null == batchContext) {
                batchContext = new BatchContext();
            }

            JobParameters jobParameters = createJobParams(jobId, jobName);


            //todo 因为reader等的scope设置为step，所以必须在reader等实例化之前，有一个Step，否则报错。spring batch的坑！

//            StepSynchronizationManager.register(new StepExecution("step_" + jobName, new JobExecution(123L)));

            StepParam stepParam=new StepParam();
            BeanCopier beanCopier = BeanCopier.create(JobParam.class, StepParam.class, false);
            beanCopier.copy(jobParam,stepParam,null);
            //在springbatch中，忽略job层，一个job一个step，jobName和stepName一致
            stepParam.setStepName(jobParam.getJobName());

            Step step = StepFactory.build(stepParam);

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
