//package com.dcits.comet.batch.sonic;
//
///**
// * @Author wangyun
// * @Date 2019/4/28
// **/
//import com.dcits.sonic.executor.api.ReportCompleted;
//import com.dcits.sonic.executor.step.StepResult;
//import com.dcits.sonic.executor.step.segment.SegmentRunningStep;
//import com.dcits.sonic.executor.step.segment.SegmentStepExecutor;
//
//public class DemoSDStepExecutorPersent implements SegmentStepExecutor {
//    @Override
//    public ReportCompleted execute(SegmentRunningStep runningStep) {
//        for (int i = 1; i < 10; i++) {
//        // 执行业务逻辑
//        // 更新执行进度
//        //    runningStep.setCompletedPercentage(0.1 * i);
//        }
//        return StepResult.SUCCESS_BLANK;
//    }
//}
