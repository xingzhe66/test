package com.dcits.comet.batch.launcher;

import com.dcits.comet.batch.param.BatchContext;
import org.springframework.batch.core.JobExecution;

public class JobExeResult {

    private String stepName;

    private String exeId;

    private BatchContext batchContext;

    private JobExecution jobExecution;


    public BatchContext getBatchContext() {
        return batchContext;
    }

    public void setBatchContext(BatchContext batchContext) {
        this.batchContext = batchContext;
    }

    public JobExecution getJobExecution() {
        return jobExecution;
    }

    public void setJobExecution(JobExecution jobExecution) {
        this.jobExecution = jobExecution;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getExeId() {
        return exeId;
    }

    public void setExeId(String exeId) {
        this.exeId = exeId;
    }
}
