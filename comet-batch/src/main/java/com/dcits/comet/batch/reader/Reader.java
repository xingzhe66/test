package com.dcits.comet.batch.reader;

import com.dcits.comet.batch.IBStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

public class Reader extends AbstractPagingReader {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);

    private IBStep batchStep;

    private int beginIndex;

    private Object lock = new Object();


    public void setBeginIndex(int beginIndex){
        this.beginIndex=beginIndex;
    }

    public void setBatchStep(IBStep batchStep) {
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

    public void init()  {

        try {
            jumpToItem(beginIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
