package com.dcits.comet.batch;


import java.util.ArrayList;
import java.util.List;

public class AbstractBatchStep<T,O> implements IBatchStep<T,O> {


    @Override
    public List getNodeList() {
        List<String> list = new ArrayList();
        list.add((String)null);
        return list;
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
        return null;
    }

    @Override
    public void writeChunk(List<O> item) {

    }
}
