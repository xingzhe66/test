package com.dcits.comet.batch.processor;

import com.dcits.comet.batch.IBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;


public class Processor implements ItemProcessor{

    protected static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private IBatch batch;
    public void setBatch(IBatch batch) {
        this.batch = batch;
    }

    @Nullable
    @Override
    public Object process(Object o) throws Exception {
        return batch.process(o);
    }
}
