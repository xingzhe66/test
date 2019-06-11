package com.dcits.comet.batch.reader;

import com.dcits.comet.batch.param.BatchContext;

public interface IPercentageReport {

    void report(BatchContext batchContext, int totalNum, int rowCount);

}
