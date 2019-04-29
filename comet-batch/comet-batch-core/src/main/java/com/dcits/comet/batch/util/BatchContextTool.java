package com.dcits.comet.batch.util;

import com.dcits.comet.batch.helper.JobParameterHelper;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.param.BatchContextManager;
/**
 * @author wangyun
 * @date 2019/3/21
 * @description 只能在JOB运行时使用此工具
 */
public class BatchContextTool {

    private static final String EXE_ID="exeId";

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
