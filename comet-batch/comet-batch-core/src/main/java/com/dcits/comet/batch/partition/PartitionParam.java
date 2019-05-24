package com.dcits.comet.batch.partition;

import lombok.Data;
import lombok.ToString;

/**
 * 分片参数
 *
 * @author leijian
 * @version 1.0
 * @date 2019/5/24 16:49
 **/
@ToString
@Data
public class PartitionParam {

    private Integer segmentSize;
    private String keyField;
    private String stepName;
}
