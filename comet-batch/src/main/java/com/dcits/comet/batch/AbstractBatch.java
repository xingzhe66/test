package com.dcits.comet.batch;


import java.util.ArrayList;
import java.util.List;

public class  AbstractBatch<T,O> implements IBatch<T,O> {


    @Override
    public List getNodes() {
        List<String> list = new ArrayList();
        list.add((String)null);
        return list;
    }

    @Override
    public List getChunk(int offset, int pageSize) {
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
    public void write(List<O> item) {

    }
}
