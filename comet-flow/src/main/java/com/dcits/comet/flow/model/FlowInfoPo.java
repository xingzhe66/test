package com.dcits.comet.flow.model;


import com.dcits.comet.dao.model.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.dcits.comet.dao.annotation.TablePk;
import com.dcits.comet.dao.annotation.PartitionKey;
import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;

/**
 * @Author guihj
 * @Description 流程信息表(PARAM)
 * @Date 2019-04-17 16:24:06
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper=false)
@TableType(name="FLOW_INFO",value=TableTypeEnum.UPRIGHT)
public class FlowInfoPo extends BasePo {

    /**tring
     * This field corresponds to the database column FLOW_INFO.flow_id
     * @Description  流程id
     */
    @TablePk(index=1)
    private String flowId;
    /**
     * This field corresponds to the database column FLOW_INFO.flow_class_name
     * @Description  流程开始时间
     */
    private String flowClassName;
    /**
     * This field corresponds to the database column FLOW_INFO.start_time
     * @Description  流程开始时间
     */
    private String startTime;
    /**
     * This field corresponds to the database column FLOW_INFO.end_time
     * @Description  流程结束时间
     */
    private String endTime;
    /**
     * This field corresponds to the database column FLOW_INFO.flow_status
     * @Description  流程状态：1-开始，2-结束，3-异常
     */
    private Integer flowStatus;
    /**
     * This field corresponds to the database column FLOW_INFO.flow_in
     * @Description  流程入参
     */
    private String flowIn;
    /**
     * This field corresponds to the database column FLOW_INFO.flow_out
     * @Description  流程返回结果
     */
    private String flowOut;
}
