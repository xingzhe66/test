package com.dcits.comet.batch.launcher;

import com.dcits.comet.batch.constant.BatchConstant;
import com.dcits.comet.batch.param.BatchContext;
import lombok.Data;

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
    private String async = BatchConstant.ASYNC_TYPE_SYNC;
    /**
     节点
     */
    private String node;

    private int pageSize;

    private int chunkSize;

    private int beginIndex;

    private int endIndex;

    private String runType = BatchConstant.RUN_TYPE_SIMPLE;

    private int threadNum;

    private Comparable segmentStart;

    private Comparable segmentEnd;

    private int segmentRowCount=0;

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

    public String getAsync() {
        return async;
    }

    public void setAsync(String async) {
        this.async = async;
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
