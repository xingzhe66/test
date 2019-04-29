package com.dcits.comet.batch.listener;

import com.dcits.comet.batch.IBStep;
import com.dcits.comet.batch.dao.BatchContextDao;
import com.dcits.comet.batch.helper.JobParameterHelper;
import com.dcits.comet.batch.param.BatchContextManager;
import com.dcits.comet.batch.util.BatchContextTool;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import static com.dcits.comet.batch.constant.BatchConstant.EXE_ID;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description job监听
 */
public class JobListener implements JobExecutionListener {

    private IBStep batchStep;
    private BatchContextDao batchContextDao;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getExitStatus().getExitCode())) {
            if(BatchStatus.COMPLETED.getBatchStatus().equals(jobExecution.getStatus().getBatchStatus())) {
                batchContextDao.saveBatchContext(
                        jobExecution.getJobParameters().getString(EXE_ID),
                        String.valueOf(jobExecution.getJobId()),
                        BatchContextManager.getInstance().getBatchContext(jobExecution.getJobParameters().getString(EXE_ID))
                );
            }
        }
        //清理context
        BatchContextManager.getInstance().clear(jobExecution.getJobParameters().getString(EXE_ID));
    }

    public void setBatchStep(IBStep batchStep) {
        this.batchStep = batchStep;
    }

    public void setBatchContextDao(BatchContextDao batchContextDao) {
        this.batchContextDao = batchContextDao;
    }
}
