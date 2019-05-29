package com.dcits.comet.batch.reader;

import com.dcits.comet.batch.ISegmentStep;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.util.BatchContextTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author wangyun
 * @Date 2019/5/22
 **/
@Slf4j
public class SegmentReader<T> implements ItemReader<T> {

    private ISegmentStep batchStep;

    private Object lock = new Object();

    protected volatile List<T> results;

    private volatile int current = 0;

    private Comparable start;

    private Comparable end;

    private String node;

    public void setBatchStep(ISegmentStep batchStep) {
        this.batchStep = batchStep;
    }

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        synchronized (lock) {

            if (results == null) {
                doReadPage();
            }
            if (current < results.size()) {
                return results.get(current++);
            } else {
                return null;
            }
        }
    }

    protected void doReadPage() {
        if (results == null) {
            results = new CopyOnWriteArrayList();
        } else {
            results.clear();
        }
        BatchContext batchContext = BatchContextTool.getBatchContext();
//        start= (Comparable) batchContext.getParams().get("segment_start");
//        end= (Comparable) batchContext.getParams().get("segment_end");
        List list = batchStep.getPageList(batchContext, start, end, node, null);
        if (null != list && list.size() != 0) {
            results.addAll(list);
        }
        log.info("doReadPage完毕,results大小为:" + results.size());
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setStart(Comparable start) {
        this.start = start;
    }

    public void setEnd(Comparable end) {
        this.end = end;
    }
}
