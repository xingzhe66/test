package com.dcits.comet.batch.model;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 */
public class QueryInput {
    private String stepName;

    private String exeId;

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
}
