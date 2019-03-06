package com.dcits.comet.batch.writer;

import com.dcits.comet.batch.IBStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class Writer implements ItemWriter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Writer.class);


    private IBStep batchStep;


    public void setBatchStep(IBStep batchStep) {
        this.batchStep = batchStep;
    }

    @Override
    public void write(List list) throws Exception {

        batchStep.writeChunk(list);

        LOGGER.info("write.....执行完毕");
    }

}
