package com.dcits.comet.batch.reader;

import com.dcits.comet.batch.ISegmentStep;
import com.dcits.comet.batch.Segment;
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
 * @Date 2019/5/23
 **/
@Slf4j
public class SplitSegmentReader<T> implements ItemReader<T> {

    private ISegmentStep batchStep;

    private Object lock = new Object();

    protected volatile List<T> results = new CopyOnWriteArrayList();

    private volatile int current = 0;

    private volatile int currentPage = 0;

    private Comparable beginIndex;

    private Comparable endIndex;

    private String keyField;

    private String stepName;

    private Integer pageSize;

    private volatile List<Segment> list;

    private String node;

    public void setBatchStep(ISegmentStep batchStep) {
        this.batchStep = batchStep;
    }

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        synchronized (lock) {
            if (list == null) {
                BatchContext batchContext = BatchContextTool.getBatchContext();
                list = batchStep.getThreadSegmentList(batchContext, beginIndex, endIndex, node, pageSize, keyField, stepName);
            }
            if (results == null || current >= results.size()) {

                log.debug("Reading page " + currentPage);

                Segment segment = null;
                if (currentPage < list.size()) {
                    segment = list.get(currentPage++);
                } else {
                    return null;
                }
                doReadPage(segment.getStartKey(), segment.getEndKey());


                if (current >= results.size()) {
                    current = 0;
                }
            }
            int next = current++;

            if (next < results.size()) {
                return (T) results.get(next);
            } else {
                return null;
            }
        }

    }

    protected void doReadPage(Comparable start, Comparable end) {
        if (results == null) {
            results = new CopyOnWriteArrayList();
        } else {
            results.clear();
        }
        BatchContext batchContext = BatchContextTool.getBatchContext();
        List list = batchStep.getPageList(batchContext, start, end, node, keyField, stepName);
        if (null != list && list.size() != 0) {
            results.addAll(list);
        }
        log.info("doReadPage完毕,results大小为:" + results.size());
    }


    public void setEndIndex(Comparable endIndex) {
        this.endIndex = endIndex;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setBeginIndex(Comparable beginIndex) {
        this.beginIndex = beginIndex;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }
}
