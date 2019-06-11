package com.dcits.comet.batch.reader;

import com.dcits.comet.batch.IBatchContextInit;
import com.dcits.comet.batch.ISegmentStep;
import com.dcits.comet.batch.Segment;
import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.util.BatchContextTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

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

    private volatile int totalNum = 0;

    private volatile int currentPage = 0;

    private Comparable beginIndex;

    private Comparable endIndex;

    private int rowCount=0;

    private String keyField;

    private String stepName;

    private Integer pageSize;

    private volatile List<Segment> segmentList;

    private String node;

    public void setBatchStep(ISegmentStep batchStep) {
        this.batchStep = batchStep;
    }

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        synchronized (lock) {
            BatchContext batchContext = BatchContextTool.getBatchContext();
            if (segmentList == null) {

                segmentList = batchStep.getThreadSegmentList(batchContext, beginIndex, endIndex, node, pageSize, keyField, stepName);
            }
            if (results == null || current >= results.size()) {

                log.debug("Reading page " + currentPage);

                Segment threadSegment = null;
                if (segmentList != null && currentPage < segmentList.size()) {
                    threadSegment = segmentList.get(currentPage++);
                } else {
                    return null;
                }
                doReadPage(threadSegment.getStartKey(), threadSegment.getEndKey());

                try {
                    if(rowCount>0) {
                        //每页更新一次进度条
                        IPercentageReport percentageReport = SpringContextHolder.getBean(IPercentageReport.class);
                        percentageReport.report(batchContext, totalNum, rowCount);
                    }

                } catch (NoSuchBeanDefinitionException e) {
                    log.warn("No qualifying bean of type '{}' available", IBatchContextInit.class.getName());
                }

                if (current >= results.size()) {
                    current = 0;
                }
            }
            int next = current++;

            if (next < results.size()) {
                totalNum++;
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

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
}
