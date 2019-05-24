package com.dcits.comet.batch.service.model;

import com.dcits.comet.batch.Segment;
import lombok.Data;

import java.util.List;

/**
 * @Author wangyun
 * @Date 2019/5/24
 **/
@Data
public class SegmentListOutput {
    private List<Segment> segmentList;
}
