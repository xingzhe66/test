package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.reader.IPercentageReport;
import com.dcits.sonic.executor.step.segment.SegmentRunningStep;

import static com.dcits.comet.batch.sonic.SegmentBatchExecutor.SEGMENT_RUNNING_STEP;

/**
 * @Author wangyun
 * @Date 2019/6/10
 **/

public class SonicPercentageReportImpl implements IPercentageReport {

    @Override
    public void report(BatchContext batchContext, int totalNum, int rowCount) {
        if(rowCount>0){
            SegmentRunningStep segmentRunningStep= (SegmentRunningStep) batchContext.getParams().get(SEGMENT_RUNNING_STEP);
            segmentRunningStep.setCompletedPercentage(totalNum/rowCount);
        }
    }
}
