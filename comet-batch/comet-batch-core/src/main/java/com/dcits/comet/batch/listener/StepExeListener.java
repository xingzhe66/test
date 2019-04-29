package com.dcits.comet.batch.listener;

import com.dcits.comet.batch.IBStep;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.util.BatchContextTool;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
/**
 * @author wangyun
 * @date 2019/3/21
 * @description step监听
 */
public class StepExeListener implements StepExecutionListener {

    private IBStep batchStep;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        BatchContext batchContext= BatchContextTool.getBatchContext();
        batchStep.preBatchStep(batchContext);

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        BatchContext batchContext= BatchContextTool.getBatchContext();
        batchStep.afterBatchStep(batchContext);

        return stepExecution.getExitStatus();
    }

    public void setBatchStep(IBStep batchStep) {
        this.batchStep = batchStep;
    }
}
