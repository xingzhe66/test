package com.dcits.comet.batch.util;

import com.dcits.comet.batch.helper.JobParameterHelper;
import com.dcits.comet.batch.param.BatchContextManager;

public class BatchContextTool {

    private static final String JOBID_NAME="exeId";

//    public static void put(String jobId,String key,Object value){
//        BatchContextManager.getInstance().put(jobId,key,value);
//    }
    /**
     * 批量上下文put
     */
    public static void put(String key,Object value){
        BatchContextManager.getInstance().put(JobParameterHelper.get(JOBID_NAME),key,value);
    }
    /**
     * 批量上下文put
     */
    public static Object get(String key){
        return BatchContextManager.getInstance().get(JobParameterHelper.get(JOBID_NAME),key);
    }

}
