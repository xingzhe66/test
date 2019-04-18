package com.dcits.comet.mq.model;

import com.dcits.comet.dao.model.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.dcits.comet.dao.annotation.TablePk;
import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;

/**
 * @Author guihj
 * @Description MQ消息队列信息表(PARAM)
 * @Date 2019-04-17 16:24:06
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper=false)
@TableType(name="PRODUCT_MSG",value=TableTypeEnum.UPRIGHT)
public class ProductMsgPo extends BasePo {

    /**
     * This field corresponds to the database column PRODUCT_MSG.producted_msg_id
     * @Description  消息id
     */
    private Long productedMsgId;
    /**
     * This field corresponds to the database column PRODUCT_MSG.flow_id
     * @Description  流程id
     */
    @TablePk(index=1)
    private Long flowId;
    /**
     * This field corresponds to the database column PRODUCT_MSG.message
     * @Description  消息内容
     */
    private String message;
    /**
     * This field corresponds to the database column PRODUCT_MSG.create_time
     * @Description  创建时间
     */
    private String createTime;
    /**
     * This field corresponds to the database column PRODUCT_MSG.last_update
     * @Description  最后一次更新时间
     */
    private String lastUpdate;
    /**
     * This field corresponds to the database column PRODUCT_MSG.status
     * @Description  状态:1-消息建立；2-待发送；3-发送成功；4-异常
     */
    private Integer status;
    /**
     * This field corresponds to the database column PRODUCT_MSG.message_type
     * @Description  消息类型
     */
    private String messageType;
    /**
     * This field corresponds to the database column PRODUCT_MSG.seq_no
     * @Description  消息序列号
     */
    private Integer seqNo;
}
