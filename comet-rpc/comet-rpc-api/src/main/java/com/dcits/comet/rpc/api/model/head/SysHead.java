package com.dcits.comet.rpc.api.model.head;

import com.dcits.comet.rpc.api.model.BaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-26 16:07
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class SysHead extends BaseData {

    /**
     * 授权柜员标识
     */
    private String authUserId;

    /**
     * 交易日期
     */

    private String tranDate;

    /**
     * 渠道流水号<br>
     */
    private String seqNo;

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
     * 交易录入柜员标识<br>
     */

    private String apprUserId;

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
     * 复核标志<br>
     * E－交易录入<br>
     * A－交易批准<br>
     */

    private String apprFlag;

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
     * 授权标志<br>
     * AUTH_FLAG<br>
     * N－未授权<br>
     * M－授权通过<br>
     * O－确认通过<br>
     */

    private String authFlag;

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
     * 业务参考号 交易日期+10顺序号,顺序号每次+1
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
}
