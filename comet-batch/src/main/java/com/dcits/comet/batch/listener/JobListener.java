package com.dcits.comet.batch.listener;

import com.dcits.comet.batch.IBStep;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener {

    private IBStep batchStep;


    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }

    public void setBatchStep(IBStep batchStep) {
        this.batchStep = batchStep;
    }
}
