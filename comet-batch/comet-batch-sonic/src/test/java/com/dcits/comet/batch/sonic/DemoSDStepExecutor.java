package com.dcits.comet.batch.sonic;
import com.dcits.comet.batch.constant.BatchConstant;
import com.dcits.comet.batch.param.BatchContext;

import com.dcits.comet.batch.exception.BatchException;
import com.dcits.comet.batch.launcher.CommonJobLauncher;
import com.dcits.comet.batch.launcher.JobExeResult;
import com.dcits.comet.batch.launcher.JobParam;
import com.dcits.sonic.executor.api.ReportCompleted;
import com.dcits.sonic.executor.api.StepStatus;
import com.dcits.sonic.executor.step.StepResult;
import com.dcits.sonic.executor.step.segment.SegmentRunningStep;
import com.dcits.sonic.executor.step.segment.SegmentStepExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.Segment;
import java.util.Map;

/**
 * @Author wangyun
 * @Date 2019/4/28
 **/
public class DemoSDStepExecutor implements SegmentStepExecutor {

    @Autowired
    CommonJobLauncher commonJobLauncher;

    @Override
    public ReportCompleted execute(SegmentRunningStep runningStep) {
        // 获取分段信息
//        Segment segment = runningStep.getSegment();
//        runningStep.getParameters();
//        JobExeResult jobExeResult = null;
//        JobParam jobParam=new JobParam();
//        jobParam.setStepName(runningStep.getClazzName());
//        jobParam.setExeId(runningStep.getStepRunId());
//        jobParam.setPageSize(100);
//        jobParam.setChunkSize(50);
//        jobParam.setNode("");
//        jobParam.setEndIndex((Integer)segment.getEnd());
//        jobParam.setBeginIndex((Integer) segment.getStart());
//        jobParam.setBatchContext(new BatchContext());
//        jobParam.setRunType(BatchConstant.RUN_TYPE_SIMPLE);
//        jobParam.setThreadNum(0);
//        jobParam.setAsync(BatchConstant.ASYNC_TYPE_SYNC);
        try {
//            jobExeResult=commonJobLauncher.run(jobParam.getStepName(),jobParam);
        } catch (Exception e) {
            e.printStackTrace();
            return new StepResult(StepStatus.FAILED, (Map)null, "fail", e.toString());
//            throw new BatchException(e.getMessage(),e);
        }
        // 根据分段信息执行业务逻辑 ...
        // 返回结果
        return StepResult.SUCCESS_BLANK;
    }
}
