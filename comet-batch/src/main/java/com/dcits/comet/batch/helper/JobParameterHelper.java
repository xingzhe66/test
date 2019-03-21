package com.dcits.comet.batch.helper;

import org.springframework.batch.core.scope.context.JobSynchronizationManager;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description 获取JobParameter
 */
public class JobParameterHelper {

    /**
     * 获取启动参数；
     * 多线程可用
     * @param key
     * @return
     */
    public static String get(String key){

        if(null==StepSynchronizationManager.getContext()) return null;

        if(null==StepSynchronizationManager.getContext().getStepExecution()) return null;

        return StepSynchronizationManager.getContext().getStepExecution().getJobParameters().getString(key);
        //JobSynchronizationManager.getContext().getJobExecution().getJobParameters().getString(key);
    }

}
