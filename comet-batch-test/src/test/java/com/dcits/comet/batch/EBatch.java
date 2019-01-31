package com.dcits.comet.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class EBatch implements ITaskletStep{
    protected static final Logger LOGGER = LoggerFactory.getLogger(EBatch.class);



    @Nullable
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        LOGGER.info("hello~~~~");

        return null;
    }
}
