package com.dcits.comet.commons.data.head;

import com.dcits.comet.commons.data.BaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-26 16:07
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class SysHead extends BaseData {

    /**
     * 系统ID
     */
    private String systemId;

    /**
     * 系统运行日期
     */

    private String runDate;

    /**
     * 交易日期
     */
    private String tranDate;

    /**
     * 渠道流水号
     */
    private String seqNo;

    /**
     * 交易屏幕标识
     */
    private String programId;

    /**
     * 分支行标识
     */
    private String branchId;

    /**
     * 柜员标识
     */
    private String userId;

    /**
     * 渠道类型
     */
    private String sourceType;

    /**
     * 操作员语言
     */
    private String userLang;

    /**
     * 法人
     */
    private String company;
}
