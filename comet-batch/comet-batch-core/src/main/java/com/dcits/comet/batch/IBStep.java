package com.dcits.comet.batch;

import com.dcits.comet.batch.param.BatchContext;

import java.util.List;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 */
public interface IBStep<T,O> extends IStep{
    /**
     * 在Step执行之前执行
     * 每个Step只执行一次
     */
    public void preBatchStep(BatchContext batchContext);

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
