package com.dcits.comet.batch.launcher;

import com.dcits.comet.batch.exception.BatchException;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.param.BatchContextManager;
import com.dcits.comet.batch.step.StepFactory;
import com.dcits.comet.batch.step.StepParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
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


    public JobExeResult run(String stepName, JobParam jobParam) {

        JobExecution jobExecution = null;

        JobExeResult jobExeResult =new JobExeResult();

        String exeId = jobParam.getExeId();

        BatchContext batchContext = jobParam.getBatchContext();

        try {


            if (null == batchContext) {
                batchContext = new BatchContext();
            }

            JobParameters jobParameters = createJobParams(exeId, stepName);


            //todo 因为reader等的scope设置为step，所以必须在reader等实例化之前，有一个Step，否则报错。spring batch的坑！

//            StepSynchronizationManager.register(new StepExecution("step_" + jobName, new JobExecution(123L)));

            StepParam stepParam=new StepParam();
            BeanCopier beanCopier = BeanCopier.create(JobParam.class, StepParam.class, false);
            beanCopier.copy(jobParam,stepParam,null);

            Step step = StepFactory.build(stepParam);

            SimpleJob job = new SimpleJob();
            job.setName("job_" + stepName);
            job.setJobRepository(jobRepository);
            job.addStep(step);

            JobLauncher jobLauncher = context.getBean(JobLauncher.class);

            try {

                BatchContextManager.getInstance().putBatchContext(exeId, batchContext);

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
                throw new BatchException("执行返回值为空，exeId："+exeId);
            }
            jobExeResult.setJobExecution(jobExecution);
            jobExeResult.setJobId(jobParam.getExeId());
            jobExeResult.setBatchContext(BatchContextManager.getInstance().getBatchContext(jobParam.getExeId()));
            jobExeResult.setJobName(jobParam.getStepName());


        } catch (Exception e) {
            e.printStackTrace();
            throw new BatchException(e.getMessage(),e);
        } finally {
            //清理context
            BatchContextManager.getInstance().clear(exeId);
        }

        BatchContextManager.getInstance().clear(jobParam.getExeId());

        return jobExeResult;
    }


    private JobParameters createJobParams(String exeId, String stepName) {
        return new JobParametersBuilder()
//                    .addDate("date", new Date())
                .addString("exeId", exeId)
                .addString("stepName", stepName)
                .toJobParameters()
                ;
    }
}
