package com.dcits.comet.batch;


import com.dcits.comet.batch.param.BatchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * @author wangyun
 * @date 2019/3/21
 * @description AbstractBStep抽象batch step
 */
public abstract class AbstractBStep<T,O> implements IBStep<T,O> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractBStep.class);

    @Override
    public List<String> getNodeList(BatchContext batchContext) {
        List<String> list = new ArrayList();
        list.add((String)"-1");
        return list;
    }

    @Override
    public int getCountNum(BatchContext batchContext,String node) {
        return -1;
    }


    @Override
    public void preBatchStep(BatchContext batchContext) {
       // logger.debug("preBatchStep");
    }

    @Override
    public List<T> getPageList(BatchContext batchContext, int offset, int pageSize,String node) {
        List<T> list = new ArrayList();
        list.add((T)null);
        return list;
    }

    @Override
    public O process(BatchContext batchContext,T item) {
        return (O)item;
    }

    @Override
    public void writeChunk(BatchContext batchContext,List<O> items) {
        for(O item:items){
            writeOne(batchContext,item);
        }
    }

    public void writeOne(BatchContext batchContext,O item){

    }

    @Override
    public void afterBatchStep(BatchContext batchContext) {

    }


}
