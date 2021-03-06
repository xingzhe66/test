package com.dcits.comet.batch;


import com.dcits.comet.batch.param.BatchContext;

import java.util.List;

public interface ISegmentStep<T,O> extends IBStep<T,O>,ISegmentSplit {
    /**
     * 获取SegmentList
     */
    List<Segment> getThreadSegmentList(BatchContext batchContext, Comparable allStart, Comparable allEnd, String node, Integer pageSize, String keyField, String stepName);

    List<T> getPageList(BatchContext batchContext, Comparable start,Comparable end ,String node,String keyField, String stepName);

}
