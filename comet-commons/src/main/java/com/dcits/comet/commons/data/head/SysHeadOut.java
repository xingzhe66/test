package com.dcits.comet.commons.data.head;

import com.dcits.comet.commons.data.BaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-26 16:07
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class SysHeadOut extends BaseData {

    private static final long serialVersionUID = 6187646013421072249L;

    /**
     * 返回状态
     */
    private String retStatus;

    /**
     * 返回结果集
     */
    private Results ret;

    /**
     * 授权柜员标识
     */
    private String authUserId;

    /**
     * 交易日期
     */

    private String tranDate;


    /**
     * 交易模式<br>
     */

    private String tranMode;

    /**
     * 源节点编号<br>
     */

    private String sourceBranchNo;

    /**
     * 报文代码<br>
     */

    private String messageCode;

    /**
     * 交易屏幕标识<br>
     */

    private String programId;

    /**
     * 线程编号<br>
     */

    private String threadNo;

    /**
     * 目标节点编号<br>
     */

    private String destBranchNo;

    /**
     * 分支行标识<br>
     */

    private String branchId;

    /**
     * 柜员标识<br>
     */

    private String userId;

    /**
     * 传输密押<br>
     */

    private String macValue;

    /**
     * 渠道类型<br>
     */

    private String sourceType;

    /**
     * 操作员语言<br>
     * CHINESE－中文；<br>
     * AMERICAN/ENGLISH－英文；<br>
     */

    private String userLang;

    /**
     * 文件绝对路径<br>
     */

    private String filePath;

    /**
     * 交易时间<br>
     */

    private String tranTimestamp;

    /**
     * 报文类型<br>
     */

    private String messageType;

    /**
     * 服务代码<br>
     */

    private String serviceCode;

    /**
     * 请求方系统ID<br>
     */

    private String systemId;

    /**
     * 法人
     */

    private String company;

    /**
     * 业务参考号
     */
    private String reference;

    /**
     * 系统运行日期
     */
    private String runDate;

    /**
     * SCENE_ID 服务场景
     */
    private String sceneId;

    public SysHeadOut(String retStatus, Results ret) {
        this.retStatus = retStatus;
        this.ret = ret;
        this.tranTimestamp = getCurrentDate();
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
        return sdf.format(new Date());
    }
}
