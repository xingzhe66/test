package com.dcits.comet.batch;

import com.dcits.comet.batch.param.BatchContext;

import java.util.List;

public interface ISegmentSplit {
    /**
     * 获取数据节点列表
     * 一个Step要处理多个对象(例如多个文件)时，需要覆盖此方法
     */
    public List<String> getNodeList(BatchContext batchContext);


    /**
     * 获取SegmentList
     */
    public List<Segment> getSegmentList(BatchContext batchContext, String node, Integer segmentSize,String keyField,String stepName);

}
