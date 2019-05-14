package com.dcits.comet.batch.param;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author wangyun
 * @date 2019/3/21
 * @description 批量参数数据传输对象 Job执行成功后会返回给调用端。
 */
public class BatchContext implements Serializable {
    private static final long serialVersionUID = 324534523453453L;
//
//    private String exeId ;
//    private String jobExecutionId;
    /**
     * 业务自定义参数
     */
    private Map<String, Object> params = new ConcurrentHashMap<String, Object>();

    public BatchContext() {
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
