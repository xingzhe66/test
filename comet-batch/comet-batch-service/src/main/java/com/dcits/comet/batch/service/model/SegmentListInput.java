package com.dcits.comet.batch.service.model;

import com.dcits.comet.batch.param.BatchContext;
import lombok.Data;

/**
 * @Author wangyun
 * @Date 2019/5/24
 **/
@Data
public class SegmentListInput {
    /**
     step名称
     */
    private String stepName;
    /**
     step名称
     */
    private Integer segmentSize;

    /**
     step名称
     */
    private String keyField;


    private BatchContext batchContext;

}
