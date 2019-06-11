package com.dcits.comet.batch.step;

import com.dcits.comet.batch.param.BatchContext;
import lombok.Data;

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

    private int beginIndex;

    private int endIndex;

    private String runType;

    private int threadNum;

    private Comparable segmentStart;

    private Comparable segmentEnd;

    private int segmentRowCount=0;

    private String keyField;

    private BatchContext batchContext;

    public String getStepName() {
        return StepName;
    }

    public void setStepName(String stepName) {
        StepName = stepName;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
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

    public int getSegmentRowCount() {
        return segmentRowCount;
    }

    public void setSegmentRowCount(int segmentRowCount) {
        this.segmentRowCount = segmentRowCount;
    }

    public String getKeyField() {
        return keyField;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }

    public BatchContext getBatchContext() {
        return batchContext;
    }

    public void setBatchContext(BatchContext batchContext) {
        this.batchContext = batchContext;
    }
}
