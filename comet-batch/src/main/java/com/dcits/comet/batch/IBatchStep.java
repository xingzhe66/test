package com.dcits.comet.batch;

import java.util.List;

public interface IBatchStep<T,O> {
    /**
     * 获取数据节点
     * todo
     */
    public List getNodeList();
    /**
     *获得一个page的数据
     * 具体大小根据pagesize确定
     */
    public List getPageList(int offset, int pageSize);

    /**
     * 获得一个单条数据
     * ？todo 可能会取消
     */
    public T read();
    /**
     *对单条进行处理
     */
    public O process(T item);
    /**
     *对一个chunksize进行写处理，并且提交事务
     * 注意chunksize和pagesize可以不同
     */
    public void  writeChunk(List<O> item);
}