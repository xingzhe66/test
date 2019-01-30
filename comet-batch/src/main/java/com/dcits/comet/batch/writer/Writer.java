package com.dcits.comet.batch.writer;

import com.dcits.comet.batch.IBatch;
import com.dcits.comet.batch.helper.JobParameterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class Writer implements ItemWriter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Writer.class);

//    @Value("#{jobParameters[name]}")
//    String name;



    private IBatch batch;

    public void setBatch(IBatch batch) {
        this.batch = batch;
    }

    @Override
    public void write(List list) throws Exception {
       // LOGGER.info();
        batch.write(list);

        LOGGER.info("write....."+ JobParameterHelper.get("name"));
    }
}
