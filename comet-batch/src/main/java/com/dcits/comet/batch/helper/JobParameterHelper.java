package com.dcits.comet.batch.helper;

import org.springframework.batch.core.scope.context.StepSynchronizationManager;

public class JobParameterHelper {

    public static String get(String key){
        return (String) StepSynchronizationManager.getContext().getJobParameters().get(key);
    }

}
