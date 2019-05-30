package com.dcits.comet.batch.sonic.entity;

import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;
import com.dcits.comet.dao.model.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/25 17:39
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableType(name = "WORKER_NODE", value = TableTypeEnum.UPRIGHT)
public class WorkerNodePo extends BasePo {

    private Long id;
    private String hostName;
    private String port;

}
