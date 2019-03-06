package com.dcits.comet.batch.processor;

import com.dcits.comet.batch.IBStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;


public class Processor implements ItemProcessor{

    protected static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private IBStep batchStep;

    public void setBatchStep(IBStep batchStep) {
        this.batchStep = batchStep;
    }

    @Nullable
    @Override
    public Object process(Object o) throws Exception {
        return batchStep.process(o);
    }


}
