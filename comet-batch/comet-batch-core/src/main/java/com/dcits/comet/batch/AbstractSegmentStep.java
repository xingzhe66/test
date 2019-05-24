package com.dcits.comet.batch;

import com.dcits.comet.batch.param.BatchContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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
    public List<Segment> getThreadSegmentList(BatchContext batchContext, Comparable allStart,Comparable allEnd, String node){
        List<Segment> list=new ArrayList<>();
        Segment segment=new Segment();
        segment.setStartKey(String.valueOf(allStart));
        segment.setEndKey(String.valueOf(allEnd));
        segment.setRowCount(0);
        list.add(segment);
        return list;
    }

    @Override
    public void preBatchStep(BatchContext batchContext) {

    }

    @Override
    public O process(BatchContext batchContext, T item) {
        return (O) item;
    }



    @Override
    public void afterBatchStep(BatchContext batchContext) {

    }
}
