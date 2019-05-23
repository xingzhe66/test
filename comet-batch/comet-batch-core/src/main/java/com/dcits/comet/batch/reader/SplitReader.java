package com.dcits.comet.batch.reader;

import com.dcits.comet.batch.IBStep;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.util.BatchContextTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author wangyun
 * @Date 2019/5/23
 **/
@Slf4j
public class SplitReader implements ItemReader<T> {

    private IBStep batchStep;

    private Object lock = new Object();

    protected volatile List<T> results=new CopyOnWriteArrayList();

    private volatile int current = 0;

    private int pageSize =1000;

    private int beginIndex=0;

    private int endIndex=Integer.MAX_VALUE;

    private int currentmax=-1;

    private int currentmin=-1;

    private String node;

    public void setBatchStep(IBStep batchStep) {
        this.batchStep = batchStep;
    }

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        synchronized (lock) {
            if(currentmin==-1){
                currentmin=beginIndex;
            }
            if(currentmax==-1){
                currentmax=currentmin+ pageSize;
            }
            if (results == null  || current >= results.size()) {
//                for(;true;) {
                if (currentmin + current > endIndex) {
                    return null;
                }

                if (log.isDebugEnabled()) {
                    log.debug("Reading page " + currentmax);
                }

                doReadPage(currentmin);

                currentmin = currentmin + pageSize;

                currentmax = currentmax + pageSize;

                if (current >= results.size()) {
                    current = 0;
                }
//                    else{
//                        break;
//                    }
//                }
            }
            int next = current++;

            if(currentmin+current>endIndex){
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

    protected void doReadPage(int currentmin) {
        if (results == null) {
            results = new CopyOnWriteArrayList();
        } else {
            results.clear();
        }
        BatchContext batchContext= BatchContextTool.getBatchContext();
//        results.addAll(batchStep.getPageList(batchContext, currentmin,endIndex,node));
        log.info("doReadPage完毕,results大小为:"+results.size());
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
    public void setNode(String node) {
        this.node = node;
    }
}
