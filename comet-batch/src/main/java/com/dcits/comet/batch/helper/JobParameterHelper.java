package com.dcits.comet.batch.helper;

import org.springframework.batch.core.scope.context.JobSynchronizationManager;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;

/**
 * 获取JobParameter
 */
public class JobParameterHelper {

    public static String get(String key){
        return JobSynchronizationManager.getContext().getJobExecution().getJobParameters().getString(key);
    }

}
