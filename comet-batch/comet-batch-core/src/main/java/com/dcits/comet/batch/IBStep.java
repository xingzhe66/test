package com.dcits.comet.batch;

import com.dcits.comet.batch.param.BatchContext;

import java.util.List;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 */
public interface IBStep<T,O> extends IStep<T,O>{


    /**
     * 获取数据节点列表
     * 一个Step要处理多个对象(例如多个文件)时，需要覆盖此方法
     */
    public List<String> getNodeList(BatchContext batchContext);

    /**
     * 获取数据总数量
     * 返回的结果用于计算这个object中的数据需要分多少分页来处理。
     */
    public int getTotalCountNum(BatchContext batchContext, String node);
    /**
     * 获取数据总数量
     * 返回的结果用于计算这个object中的数据需要分多少分页来处理。
     */
    public int getCountNum(BatchContext batchContext, String node);


    /**
     * 在Step执行之前执行
     * 每个Step只执行一次
     */
    public void preBatchStep(BatchContext batchContext);


    /**
     *获得一个page的数据
     * 具体大小根据pagesize确定
     * 则此方法是本地多线程，或分布式并发的
     */
    public List<T> getPageList(BatchContext batchContext, int offset, int pageSize, String node);

    /**
     * 获得一个单条数据
     */
//    public T read();
    /**
     *对read到的单条进行转换处理
     */
    public O process(BatchContext batchContext, T item);
    /**
     * 传入一个chunkSize大小的数据列表，并处理
     *结束后提交事务
     */
    public void writeChunk(BatchContext batchContext, List<O> item);

    /**
     * 在Step执行之后执行
     * 每个Step只执行一次
     */
    public void afterBatchStep(BatchContext batchContext);




}
