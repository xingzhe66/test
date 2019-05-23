package com.dcits.comet.batch;


import com.dcits.comet.batch.param.BatchContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description AbstractBStep抽象batch step
 */
@Slf4j
public abstract class AbstractRowNumStep<T, O> implements IRowNumStep<T, O> {

    @Override
    public void preBatchStep(BatchContext batchContext) {
        // logger.debug("preBatchStep");
    }


    @Override
    public O process(BatchContext batchContext, T item) {
        return (O) item;
    }

    @Override
    public void writeChunk(BatchContext batchContext, List<O> items) {
        for (O item : items) {
            writeOne(batchContext, item);
        }
    }

    public void writeOne(BatchContext batchContext, O item) {

    }

    @Override
    public void afterBatchStep(BatchContext batchContext) {

    }


}
