package com.dcits.comet.batch;

import java.util.List;

public interface IBatch<T,O> {
    public List getNodes();

    public List getChunk(int offset, int pageSize);

    public T read();

    public O process(T item);

    public void  write(List<O> item);
}
