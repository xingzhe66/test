package com.dcits.comet.batch;

import com.dcits.comet.batch.param.BatchContext;

import java.util.List;

public interface IRowNumSplit {

    /**
     * 获取数据节点列表
     * 一个Step要处理多个对象(例如多个文件)时，需要覆盖此方法
     */
    public List<String> getNodeList(BatchContext batchContext);

    /**
     * 获取数据总数量
     * 返回的结果用于计算这个object中的数据需要分多少分页来处理。
     */
    public int getCountNum(BatchContext batchContext, String node);


}
