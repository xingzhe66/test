package com.dcits.comet.batch.sonic;

import com.alibaba.fastjson.JSON;
import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.launcher.IJobLauncher;
import com.dcits.comet.batch.launcher.JobParam;
import com.dcits.comet.batch.model.ExeInput;
import com.dcits.comet.batch.sonic.exception.BatchServiceException;
import com.dcits.sonic.executor.api.ReportCompleted;
import com.dcits.sonic.executor.step.StepResult;
import com.dcits.sonic.executor.step.normal.NormalRunningStep;
import com.dcits.sonic.executor.step.normal.NormalStepExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;

/**
 * 普通Step开发,只需实现com.dcits.sonic.executor.step.normal.NormalStepExecutor接口，接口入参中可以拿到用户在开发时
 * 配置的自定义参数
 *
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 13:28
 * @see com.dcits.sonic.executor.step.normal.NormalStepExecutor
 **/
@Slf4j
public class NormalBatchExecutor implements NormalStepExecutor {
    @Override
    public ReportCompleted execute(NormalRunningStep normalRunningStep) {
        try {
            log.debug("NormalRunningStep{}", normalRunningStep.toString());
            //sonic参数
            Map<String, Object> parameters = normalRunningStep.getParameters();
            log.debug("parameters{}", parameters);

            ExeInput exeInput = JSON.parseObject(JSON.toJSONString(parameters), ExeInput.class);

            JobParam jobParam = new JobParam();
            BeanCopier beanCopier = BeanCopier.create(ExeInput.class, JobParam.class, false);
            beanCopier.copy(exeInput, jobParam, null);

            IJobLauncher jobLauncher = SpringContextHolder.getBean(IJobLauncher.class);

            jobLauncher.run(exeInput.getStepName(), jobParam);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BatchServiceException(e.getMessage(), e);
        }
        //返回执行结果
        return StepResult.SUCCESS_BLANK;
    }
}
