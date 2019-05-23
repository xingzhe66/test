package com.dcits.comet.batch;

import com.dcits.comet.batch.param.BatchContext;

import java.util.List;

/**
 * @Author wangyun
 * @Date 2019/5/23
 **/
public interface IRowNumStep<T,O> extends IBStep<T,O>,IRowNumSplit {
    /**
     *获得一个page的数据
     * 具体大小根据pagesize确定
     * 则此方法是本地多线程，或分布式并发的
     */
    public List<T> getPageList(BatchContext batchContext, int offset, int pageSize, String node);

}
