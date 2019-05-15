package com.dcits.comet.batch.sonic;

import com.dcits.sonic.executor.api.ReportCompleted;
import com.dcits.sonic.executor.step.StepResult;
import com.dcits.sonic.executor.step.segment.SegmentRunningStep;
import com.dcits.sonic.executor.step.segment.SegmentStepExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/14 11:02
 **/
@Slf4j
public class SDStepExecutor implements SegmentStepExecutor {
    @Override
    public ReportCompleted execute(SegmentRunningStep runningStep) {
        log.info("{}",runningStep);
        for (int i = 1; i < 10; i++) {
            // 执行业务逻辑...
            // 更新执行进度
            runningStep.setCompletedPercentage(0.1 * i);
        }
        return StepResult.SUCCESS_BLANK;
    }
}

