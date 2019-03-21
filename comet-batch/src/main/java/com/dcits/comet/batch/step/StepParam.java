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

    private int beginIndex;

    private int endIndex;

    private String runType;

    private int threadNum;

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
}
