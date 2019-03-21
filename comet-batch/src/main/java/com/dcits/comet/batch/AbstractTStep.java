package com.dcits.comet.batch;

import com.dcits.comet.batch.exception.BatchException;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.util.BatchContextTool;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
/**
 * @author wangyun
 * @date 2019/3/21
 * @description AbstractTStep抽象tasklet step
 */
public abstract class AbstractTStep implements ITStep {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        BatchContext batchContext= BatchContextTool.getBatchContext();
        try {
            this.exe(batchContext);
        } catch (Throwable throwable) {
            throw new BatchException("批量执行错误！错误信息："+throwable.getMessage());
        }

        return RepeatStatus.FINISHED;
    }

    public abstract void exe(BatchContext batchContext) throws Throwable;
}
