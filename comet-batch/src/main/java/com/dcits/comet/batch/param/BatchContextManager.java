package com.dcits.comet.batch.param;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description BatchContextManager
 */
public class BatchContextManager {

    private static final Map<String, BatchContext> map=new ConcurrentHashMap<String,BatchContext>();;

    private static volatile BatchContextManager batchContextManager;

    private BatchContextManager() {
    }

    public static BatchContextManager getInstance() {
        if (batchContextManager == null) {
            synchronized (BatchContextManager.class) {
                if (batchContextManager == null) {
                    batchContextManager = new BatchContextManager();
                }
            }
        }
        return batchContextManager;
    }

    public Map<String, BatchContext> getMap() {
        return this.map;
    }

    //todo 多线程执行process时会有线程安全问题
    //todo 对于多线程执行部分，最好不put
    public void put(String jobId,String key,Object value) {
        synchronized (map) {
            BatchContext batchContext = this.map.get(jobId);
            if (null == batchContext) {
                batchContext = new BatchContext();
            }
            batchContext.getParams().put(key,value);
            this.map.put(jobId,batchContext);
        }
    }

    public void putBatchContext(String jobId,BatchContext batchContext) {

        this.map.put(jobId,batchContext);

    }

    public Object get(String jobId,String key) {
        BatchContext batchContext =this.map.get(jobId);
        if(null==batchContext){
            return null;
        }
        return batchContext.getParams().get(key);
    }

    public void clear(String jobId) {

        BatchContext batchContext =this.map.get(jobId);

        if(null==batchContext){
            return;
        }
        map.remove(jobId);
    }


    public BatchContext getBatchContext(String jobId) {
        return map.get(jobId);
    }
}
