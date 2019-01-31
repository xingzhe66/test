package com.dcits.comet.batch.processor;

import com.dcits.comet.batch.IBatchStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;


public class Processor implements ItemProcessor{

    protected static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private IBatchStep batchStep;

    public void setBatchStep(IBatchStep batchStep) {
        this.batchStep = batchStep;
    }

    @Nullable
    @Override
    public Object process(Object o) throws Exception {
        return batchStep.process(o);
    }


}
