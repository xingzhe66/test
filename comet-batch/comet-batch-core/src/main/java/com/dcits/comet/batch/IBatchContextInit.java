package com.dcits.comet.batch;

import com.dcits.comet.batch.param.BatchContext;

/**
 * @author chengliang
 * @date 2019/5/29
 * @description 批量Job初始化接口，初始化job级别全局变量
 */
public interface IBatchContextInit {
    /**
     * 初始化job级别全局变量
     *
     * @param batchContext
     */
    default void init(BatchContext batchContext) {
    }

}
