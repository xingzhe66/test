package com.dcits.comet.batch.launcher;

import com.dcits.comet.batch.param.BatchContext;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 */
public class JobParam {

    private String stepName;

    private String exeId;
    /**
     异步标示
     0 同步。
     1 异步。

     */
    private String async;
    /**
     节点
     */
    private String node;

    private int pageSize;

    private int chunkSize;

    private int beginIndex;

    private int endIndex;

    private String runType;

    private int threadNum;

    private Comparable segmentStart;

    private Comparable segmentEnd;

    private String keyField;

    private BatchContext batchContext;

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getExeId() {
        return exeId;
    }

    public void setExeId(String exeId) {
        this.exeId = exeId;
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

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public Integer getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(Integer beginIndex) {
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

    public String getAsync() {
        return async;
    }

    public void setAsync(String async) {
        this.async = async;
    }

    public Comparable getSegmentStart() {
        return segmentStart;
    }

    public void setSegmentStart(Comparable segmentStart) {
        this.segmentStart = segmentStart;
    }

    public Comparable getSegmentEnd() {
        return segmentEnd;
    }

    public void setSegmentEnd(Comparable segmentEnd) {
        this.segmentEnd = segmentEnd;
    }

    public String getKeyField() {
        return keyField;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }
}
