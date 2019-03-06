package com.dcits.comet.batch.helper;

import org.springframework.batch.core.scope.context.JobSynchronizationManager;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;

/**
 * 获取JobParameter
 */
public class JobParameterHelper {

    /**
     * 获取启动参数；
     * 因为多线程情况下，有些线程先执行完会把Context清空，会导致报错，所以多线程下禁止使用
     * @param key
     * @return
     */
    public static String get(String key){
        return JobSynchronizationManager.getContext().getJobExecution().getJobParameters().getString(key);
    }

}
