package com.dcits.comet.rpc.api.model.head;

import lombok.Data;

/**
 * 返回对象
 *
 * @author ChengLiang
 */
@Data
public class Result {
    private String retCode;

    private String retMsg;

    public Result(String retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }
}
