package com.dcits.comet.batch.launcher;

import com.dcits.comet.batch.constant.BatchConstant;
import com.dcits.comet.batch.dao.BatchContextDao;
import com.dcits.comet.batch.exception.BatchException;
import com.dcits.comet.batch.listener.JobListener;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.param.BatchContextManager;
import com.dcits.comet.batch.step.StepFactory;
import com.dcits.comet.batch.step.StepParam;
import com.dcits.comet.batch.util.BatchContextTool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import static com.dcits.comet.batch.constant.BatchConstant.*;


/**
job执行器
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
    @Autowired
    BatchContextDao batchContextDao;

    @Override
    public JobExeResult run(String stepName, JobParam jobParam) {

        JobExecution jobExecution = null;

        JobExeResult jobExeResult =new JobExeResult();
        jobExeResult.setStepName(jobParam.getStepName());
        jobExeResult.setExeId(jobParam.getExeId());

        String exeId = jobParam.getExeId();

        BatchContext batchContext = jobParam.getBatchContext();

        try {
            if (null == batchContext) {
                batchContext = new BatchContext();
            }
            JobParameters jobParameters = createJobParams(exeId, stepName);
            StepParam stepParam=new StepParam();
            BeanCopier beanCopier = BeanCopier.create(JobParam.class, StepParam.class, false);
            beanCopier.copy(jobParam,stepParam,null);

            Step step = StepFactory.build(stepParam);

            JobBuilderFactory jobBuilders = context.getBean(JobBuilderFactory.class);
            JobListener jobListener=new JobListener();
            jobListener.setBatchContextDao(batchContextDao);

            Job job = jobBuilders.get(JOB_PEX + stepName)
                    .repository(jobRepository)
                    .start(step)
                    .listener(jobListener)
                    .build();
//            SimpleJob job = new SimpleJob();
//            job.setName(JOB_PEX + stepName);
//            job.setJobRepository(jobRepository);
//            job.addStep(step);
//            job.setJobExecutionListeners(new JobListener());

            SimpleJobLauncher jobLauncher = (SimpleJobLauncher) context.getBean(JobLauncher.class);
            /**
             * SimpleAsyncTaskExecutor这个实现不重用任何线程，或者说它每次调用都启动一个新线程。
            // 但是，它还是支持对并发总数设限，当超过线程并发总数限制时
            // ，阻塞新的调用，直到有位置被释放。
             * 默认SyncTaskExecutor类这个实现不会异步执行。相反，每次调用都在发起调用的线程中执行。它的主要用处是在不需要多线程的时候。
            */
            if(BatchConstant.ASYNC_TYPE_ASYNC.equals(jobParam.getAsync())) {
               // ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
               // SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
                jobLauncher.setTaskExecutor(taskExecutor);
            }

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

            if (null != jobExecution) {
                jobExeResult.setJobExecution(jobExecution);
                //todo 异步情况下，batchcontext需要在query到执行成功时返回
//                batchContextDao.saveBatchContext(jobParam.getExeId(),
//                        String.valueOf(jobExecution.getJobId()),
//                        BatchContextManager.getInstance().getBatchContext(jobParam.getExeId()));
                jobExeResult.setBatchContext(BatchContextManager.getInstance().getBatchContext(jobParam.getExeId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BatchException(e.getMessage(),e);
        } finally {
            //清理context
            BatchContextManager.getInstance().clear(exeId);
        }

        return jobExeResult;
    }


    private JobParameters createJobParams(String exeId, String stepName) {
        return new JobParametersBuilder()
//                    .addDate("date", new Date())
                .addString(EXE_ID, exeId)
                .addString(STEP_NAME, stepName)
                .toJobParameters()
                ;
    }
}
