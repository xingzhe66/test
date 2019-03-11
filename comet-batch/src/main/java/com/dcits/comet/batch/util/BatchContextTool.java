package com.dcits.comet.batch.util;

import com.dcits.comet.batch.helper.JobParameterHelper;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.param.BatchContextManager;

public class BatchContextTool {

    private static final String EXE_ID="exeId";

//    public static void put(String jobId,String key,Object value){
//        BatchContextManager.getInstance().put(jobId,key,value);
//    }
    /**
     * 获取批量上下文
     */
    public static BatchContext getBatchContext(){
        return BatchContextManager.getInstance().getBatchContext(JobParameterHelper.get(EXE_ID));
    }
    /**
     * 批量上下文put
     */
    public static void put(String key,Object value){
        BatchContextManager.getInstance().put(JobParameterHelper.get(EXE_ID),key,value);
    }
    /**
     * 批量上下文put
     */
    public static Object get(String key){
        return BatchContextManager.getInstance().get(JobParameterHelper.get(EXE_ID),key);
    }

}
