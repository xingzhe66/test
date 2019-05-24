package com.dcits.comet.batch.step;

import com.dcits.comet.batch.param.BatchContext;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 */
public class StepParam {

    private String StepName;

    /**
     节点
     */
    private String node;

    private int pageSize;

    private int chunkSize;

    private Comparable beginIndex;

    private Comparable endIndex;

    private String runType;

    private int threadNum;

    private Comparable segmentStart;

    private Comparable segmentEnd;

    private String keyField;

    private BatchContext batchContext;

    public String getStepName() {
        return StepName;
    }

    public void setStepName(String stepName) {
        this.StepName = stepName;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Comparable getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Comparable endIndex) {
        this.endIndex = endIndex;
    }

    public Comparable getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(Comparable beginIndex) {
        this.beginIndex = beginIndex;
    }

    public BatchContext getBatchContext() {
        return batchContext;
    }

    public void setBatchContext(BatchContext batchContext) {
        this.batchContext = batchContext;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setSegmentStart(Comparable segmentStart) {
        this.segmentStart = segmentStart;
    }

    public void setSegmentEnd(Comparable segmentEnd) {
        this.segmentEnd = segmentEnd;
    }

    public Comparable getSegmentStart() {
        return segmentStart;
    }

    public Comparable getSegmentEnd() {
        return segmentEnd;
    }

    public String getKeyField() {
        return keyField;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }
}
