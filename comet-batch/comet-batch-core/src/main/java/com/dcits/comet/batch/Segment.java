package com.dcits.comet.batch;

import com.dcits.comet.dao.model.BasePo;
import lombok.Data;

/**
 * @Author wangyun
 * @Date 2019/5/22
 **/
@Data
public class Segment extends BasePo {
    String start;
    String end;
    Integer count;
}
