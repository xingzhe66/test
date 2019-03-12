package com.dcits.comet.commons.data;

import com.dcits.comet.commons.data.head.AppHead;
import com.dcits.comet.commons.data.head.LocalHead;
import com.dcits.comet.commons.data.head.SysHead;
import lombok.Data;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-27 18:30
 * @Version 1.0
 **/
@Data
public class BaseRequest extends BaseData {
    private SysHead sysHead;
    private AppHead appHead;
    private LocalHead localHead;
}
