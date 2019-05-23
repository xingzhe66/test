package com.dcits.comet.batch;


import com.dcits.comet.batch.param.BatchContext;

import java.math.BigDecimal;
import java.util.List;

public interface ISegmentStep<T,O> extends IBStep<T,O>,ISegmentSplit {

    List<T> getPageList(BatchContext batchContext, Comparable start,Comparable end ,String node);

}
