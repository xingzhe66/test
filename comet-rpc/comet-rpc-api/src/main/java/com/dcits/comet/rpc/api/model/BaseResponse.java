package com.dcits.comet.rpc.api.model;

import com.dcits.comet.rpc.api.model.head.AppHead;
import com.dcits.comet.rpc.api.model.head.SysHeadOut;
import lombok.Data;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-27 18:35
 * @Version 1.0
 **/
@Data
public class BaseResponse extends BaseData {

    private SysHeadOut sysHead;
    private AppHead appHead;
}
