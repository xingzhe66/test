package com.dcits.comet.batch.param;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 批量参数数据传输对象
 *
 */
public class BatchContext implements Serializable {
    private static final long serialVersionUID = 324534523453453L;

    /** 执行类型,用于区分续跑还是正常执行 **/
    private short exeType = 1;
    /** 所属分组（业务群），需要与group对应 */
    private String catalog;
    /** 业务自定义参数 */
    private Map<String, Object> params = new ConcurrentHashMap<String, Object>();


    public BatchContext() {
    }


    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getCatalog() {
        return catalog;
    }


    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }


    public short getExeType() {
        return exeType;
    }


    public void setExeType(short exeType) {
        this.exeType = exeType;
    }
}
