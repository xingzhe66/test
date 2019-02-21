package com.dcits.comet.batch.launcher;

import com.dcits.comet.batch.param.BatchContext;
import org.springframework.batch.core.JobExecution;

public class JobExeResult {

    private String jobName;

    private String jobId;

    private BatchContext batchContext;

    private JobExecution jobExecution;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

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
}
