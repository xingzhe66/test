package com.dcits.comet.batch.listener;

import com.dcits.comet.batch.IBatchStep;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener {

    private IBatchStep batchStep;


    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }

    public void setBatchStep(IBatchStep batchStep) {
        this.batchStep = batchStep;
    }
}
