package com.dcits.comet.commons.data;

import com.dcits.comet.commons.constant.Constants;
import com.dcits.comet.commons.data.head.*;
import lombok.Data;

import java.io.Serializable;


@Data
public class BusinessResult implements Serializable {

    private static final long serialVersionUID = 5895580006436550018L;

    private SysHeadOut sysHeadOut;
    private AppHead appHead;
    private BaseResponse response;


    public BusinessResult(BaseResponse response) {
        Results ret = new Results(new Result(Constants.ResponseCode.CODE_SUCCESS, Constants.ResponseCode.MESSAGE_SUCCESS));
        String retStatus = Constants.ResponseStatus.STATUS_SUCCESS;
        this.sysHeadOut = new SysHeadOut(retStatus, ret);
        this.response = response;
    }

    public BusinessResult(AppHead appHead, BaseResponse response) {
        Results ret = new Results(new Result(Constants.ResponseCode.CODE_SUCCESS, Constants.ResponseCode.MESSAGE_SUCCESS));
        String retStatus = Constants.ResponseStatus.STATUS_SUCCESS;
        this.sysHeadOut = new SysHeadOut(retStatus, ret);
        this.appHead = appHead;
        this.response = response;
    }

    /**
     * 指定错误码的返回结果
     * 用于失败场景
     *
     * @param errCode 响应码
     * @param errMsg  响应信息
     */
    public BusinessResult(String errCode, String errMsg) {
        Results ret = new Results(new Result(errCode, errMsg));
        String retStatus = Constants.ResponseStatus.STATUS_FAILED;
        this.sysHeadOut = new SysHeadOut(retStatus, ret);
    }

    /**
     * 设置返回sysHead
     *
     * @param sysHead
     */
    private void setSysHeadOutInfo(SysHead sysHead) {
        this.sysHeadOut.setAuthUserId(sysHead.getAuthUserId());
        this.sysHeadOut.setTranDate(sysHead.getTranDate());
        this.sysHeadOut.setTranMode(sysHead.getTranMode());
        this.sysHeadOut.setSourceBranchNo(sysHead.getSourceBranchNo());
        this.sysHeadOut.setMessageCode(sysHead.getMessageCode());
        this.sysHeadOut.setProgramId(sysHead.getProgramId());
        this.sysHeadOut.setThreadNo(sysHead.getThreadNo());
        this.sysHeadOut.setDestBranchNo(sysHead.getDestBranchNo());
        this.sysHeadOut.setMacValue(sysHead.getMacValue());
        this.sysHeadOut.setSourceType(sysHead.getSourceType());
        this.sysHeadOut.setFilePath(sysHead.getFilePath());
        this.sysHeadOut.setSystemId(sysHead.getSystemId());
        this.sysHeadOut.setCompany(sysHead.getCompany());
        this.sysHeadOut.setReference(sysHead.getReference());
        this.sysHeadOut.setRunDate(sysHead.getRunDate());
        this.sysHeadOut.setSceneId(sysHead.getSceneId());
    }
}
