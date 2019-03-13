package com.dcits.comet.commons.data;

import com.dcits.comet.commons.data.head.AppHead;
import com.dcits.comet.commons.data.head.SysHeadOut;
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
