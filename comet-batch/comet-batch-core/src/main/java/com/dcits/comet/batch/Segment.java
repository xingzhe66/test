package com.dcits.comet.batch;

import com.dcits.comet.dao.model.BasePo;
import lombok.Data;
import lombok.ToString;

/**
 * @Author wangyun
 * @Date 2019/5/22
 **/
@Data
@ToString
public class Segment extends BasePo {
    String startKey;
    String endKey;
    Integer rowCount;
}
