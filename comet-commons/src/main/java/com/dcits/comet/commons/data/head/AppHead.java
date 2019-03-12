package com.dcits.comet.commons.data.head;

import com.dcits.comet.commons.data.BaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-26 16:17
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class AppHead extends BaseData {

    /**
     * 本页记录总数
     */
    private String totalNum;
}
