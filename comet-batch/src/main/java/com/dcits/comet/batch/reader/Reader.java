package com.dcits.comet.batch.reader;

import com.dcits.comet.batch.IBatchStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

public class Reader extends AbstractPagingReader {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);

    private IBatchStep batchStep;

    public void setBatchStep(IBatchStep batchStep) {
        this.batchStep = batchStep;
    }

    @Override
    protected void doReadPage(int offset, int pageSize) {
        if (results == null) {
            results = new CopyOnWriteArrayList();
        } else {
            results.clear();
        }
        results.addAll(batchStep.getPageList(offset, pageSize));
    }



}
