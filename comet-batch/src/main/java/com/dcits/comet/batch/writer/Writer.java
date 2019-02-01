package com.dcits.comet.batch.writer;

import com.dcits.comet.batch.IBatchStep;
import com.dcits.comet.batch.helper.JobParameterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class Writer implements ItemWriter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Writer.class);


    private IBatchStep batchStep;


    public void setBatchStep(IBatchStep batchStep) {
        this.batchStep = batchStep;
    }

    @Override
    public void write(List list) throws Exception {

        batchStep.writeChunk(list);

       // LOGGER.debug("write....."+ JobParameterHelper.get("name"));
    }

}
