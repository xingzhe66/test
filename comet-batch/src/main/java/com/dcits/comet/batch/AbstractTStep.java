package com.dcits.comet.batch;

import com.dcits.comet.batch.exception.BatchException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

public abstract class AbstractTStep implements ITStep {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        try {
            this.exe();
        } catch (Throwable throwable) {
            throw new BatchException("批量执行错误！错误信息："+throwable.getMessage());
        }

        return RepeatStatus.FINISHED;
    }

    public abstract void exe() throws Throwable;
}
