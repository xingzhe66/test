package com.dcits.comet.batch.service.model;

import com.dcits.comet.batch.param.BatchContext;

import java.io.Serializable;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 */
public class ExeInput implements Serializable {

    /**
    step名称
     */
    private String stepName;
    /**
     执行id，全局唯一
     */
    private String exeId;
    /**
     节点
     */
    private String node;
    /**
    分页大小，确定每次读出多少条到内存
     */
    private int pageSize;
    /**
     chunk大小，确定多少行提交一次事务
     */
    private int chunkSize;
    /**
     从第几条开始读，不包含当前条
     */
    private int beginIndex;
    /**
     到第几条开结束，包含当前条
     */
    private int endIndex;

    /**
     异步标示
     0 同步。
     1 异步。

     */
    private String async;

    /**
     执行类型
     1 单线程。
     2 多线程。

     */
    private String runType;
    /**
     线程数
     */
    private int threadNum;
    /**
     上下文
     */
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
}
