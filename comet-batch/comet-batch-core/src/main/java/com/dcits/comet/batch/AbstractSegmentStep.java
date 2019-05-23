package com.dcits.comet.batch;

import com.dcits.comet.batch.param.BatchContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author wangyun
 * @Date 2019/5/23
 **/
@Slf4j
public abstract class AbstractSegmentStep <T, O> implements ISegmentStep<T, O> {
    @Override
    public List<String> getNodeList(BatchContext batchContext) {
        return null;
    }

    @Override
    public List<Segment> getSegmentList(BatchContext batchContext, String node) {
        return null;
    }

    @Override
    public void preBatchStep(BatchContext batchContext) {

    }

    @Override
    public O process(BatchContext batchContext, T item) {
        return null;
    }

    @Override
    public void writeChunk(BatchContext batchContext, List<O> item) {

    }

    @Override
    public void afterBatchStep(BatchContext batchContext) {

    }
}
