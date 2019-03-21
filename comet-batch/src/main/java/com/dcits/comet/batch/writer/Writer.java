package com.dcits.comet.batch.writer;

import com.dcits.comet.batch.IBStep;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.util.BatchContextTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description Writer
 */
public class Writer implements ItemWriter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Writer.class);


    private IBStep batchStep;


    public void setBatchStep(IBStep batchStep) {
        this.batchStep = batchStep;
    }

    @Override
    public void write(List list) throws Exception {
        BatchContext batchContext= BatchContextTool.getBatchContext();
        batchStep.writeChunk(batchContext,list);

        LOGGER.info("write写chunk完毕,chunk大小为:"+list.size());
    }

}
