package com.dcits.comet.rpc.api.model;

import com.dcits.comet.rpc.api.constants.Constants;
import com.dcits.comet.rpc.api.constants.ResponseEnum;
import com.dcits.comet.rpc.api.model.head.AppHead;
import com.dcits.comet.rpc.api.model.head.Result;
import com.dcits.comet.rpc.api.model.head.SysHeadOut;

/**
 * 公共返回处理
 *
 * @author ChengLiang
 */
public class BusinessResult {

    public static <T extends BaseResponse> T success(T t) {
        Result ret = new Result(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage());
        String retStatus = Constants.ResponseStatus.STATUS_SUCCESS;
        SysHeadOut sysHeadOut = new SysHeadOut(retStatus, ret);
        t.setSysHead(sysHeadOut);
        return t;
    }

    public static <T extends BaseResponse> T success(T t, AppHead appHead) {
        success(t);
        t.setAppHead(appHead);
        return t;
    }

    /**
     * 指定错误码的返回结果
     * 用于失败场景
     *
     * @param errCode 响应码
     * @param errMsg  响应信息
     */
    public static <T extends BaseResponse> T error(String errCode, String errMsg) {
        Result ret = new Result(errCode, errMsg);
        String retStatus = Constants.ResponseStatus.STATUS_FAILED;
        SysHeadOut sysHeadOut = new SysHeadOut(retStatus, ret);
        T t = (T) new BaseResponse();
        t.setSysHead(sysHeadOut);
        return t;
    }

}
