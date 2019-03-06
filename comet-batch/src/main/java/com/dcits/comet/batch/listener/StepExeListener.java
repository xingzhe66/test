package com.dcits.comet.batch.listener;

import com.dcits.comet.batch.IBStep;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepExeListener implements StepExecutionListener {

    private IBStep batchStep;

    @Override
    public void beforeStep(StepExecution stepExecution) {

        batchStep.preBatchStep();

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        batchStep.afterBatchStep();

        return stepExecution.getExitStatus();
    }

    public void setBatchStep(IBStep batchStep) {
        this.batchStep = batchStep;
    }
}
