package com.dcits.comet.batch.sonic;


import com.dcits.sonic.executor.api.ReportCompleted;
import com.dcits.sonic.executor.step.StepResult;
import com.dcits.sonic.executor.step.normal.NormalRunningStep;
import com.dcits.sonic.executor.step.normal.NormalStepExecutor;

/**
 * @Author wangyun
 * @Date 2019/4/28
 **/
public class DemoExecutor implements NormalStepExecutor {
    @Override
    public ReportCompleted execute(NormalRunningStep step) {
        //获取自定义参数
//        Map<String, Object> params = step.getParameters();
        // 执行业务逻辑 .....
        //返回执行结果
        return StepResult.SUCCESS_BLANK;
    }
}
