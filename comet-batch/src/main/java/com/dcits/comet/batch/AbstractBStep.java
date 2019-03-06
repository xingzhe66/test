package com.dcits.comet.batch;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AbstractBStep<T,O> implements IBStep<T,O> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractBStep.class);


    @Override
    public int getCountNum() {
        return 0;
    }

    @Override
    public List getNodeList() {
        List<String> list = new ArrayList();
        list.add((String)null);
        return list;
    }

    @Override
    public void preBatchStep() {
        logger.debug("preBatchStep");
    }

    @Override
    public List getPageList(int offset, int pageSize) {
        List<T> list = new ArrayList();
        list.add((T)null);
        return list;
    }

    @Override
    public T read() {
        return null;
    }

    @Override
    public O process(T item) {
        return (O)item;
    }

    @Override
    public void writeChunk(List<O> item) {

    }

    @Override
    public void afterBatchStep() {

    }


}
