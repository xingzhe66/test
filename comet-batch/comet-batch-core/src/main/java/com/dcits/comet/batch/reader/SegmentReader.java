package com.dcits.comet.batch.reader;

import com.dcits.comet.batch.IBStep;
import com.dcits.comet.batch.ICStep;
import com.dcits.comet.batch.Segment;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.util.BatchContextTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author wangyun
 * @Date 2019/5/22
 **/
@Slf4j
public class SegmentReader implements ItemReader<T> {
    private ICStep batchStep;

    private Object lock = new Object();

    protected volatile List<T> results=new CopyOnWriteArrayList();

    private volatile int current = 0;

    private int offset=1000;

    private int beginIndex=0;

    private int endIndex=Integer.MAX_VALUE;

    private int currentmax=-1;

    private int currentmin=-1;

    private String node;
    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        synchronized (lock) {
            if(currentmin==-1){
                currentmin=beginIndex;
            }
            if(currentmax==-1){
                currentmax=currentmin+offset;
            }
            if (results == null  || current >= results.size()) {

                if(currentmax>endIndex){
                    return null;
                }

                if (log.isDebugEnabled()) {
                    log.debug("Reading page " + currentmax);
                }

                doReadPage(currentmin,currentmax);

                currentmin=currentmin+offset;

                currentmax=currentmax+offset;

                if (current >= results.size()) {
                    current = 0;
                }
            }
            int next = current++;

            if(currentmax>endIndex){
                return null;
            }
            if (next < results.size()) {
                return results.get(next);
            }
            else {
                return null;
            }


        }

    }

    protected void doReadPage(int currentmin,int currentmax) {
        if (results == null) {
            results = new CopyOnWriteArrayList();
        } else {
            results.clear();
        }
        BatchContext batchContext= BatchContextTool.getBatchContext();
        results.addAll(batchStep.getPageList(batchContext, currentmin,currentmax,node));
        log.info("doReadPage完毕,results大小为:"+results.size());
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
    public void setNode(String node) {
        this.node = node;
    }
}
