package com.dcits.comet.batch.sonic;

import com.alibaba.fastjson.JSON;
import com.dcits.comet.batch.AbstractRowNumStep;
import com.dcits.comet.batch.AbstractSegmentStep;
import com.dcits.comet.batch.IStep;
import com.dcits.comet.batch.Segment;
import com.dcits.comet.batch.exception.BatchException;
import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.launcher.JobParam;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.sonic.executor.api.model.Attributes;
import com.dcits.sonic.executor.step.segment.AbstractStepSegmenter;
import com.dcits.sonic.executor.step.segment.SegmentRunningStep;
import com.dcits.sonic.executor.step.segment.SegmentedStepSender;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 分段
 *
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 14:13
 **/
@Slf4j
public class SegmentBatchExecutorPartition extends AbstractStepSegmenter {

    @Override
    public void doSegment(SegmentRunningStep segmentRunningStep, SegmentedStepSender segmentedStepSender) {
        // 用户自定义拓展参数
        Attributes attributes = segmentRunningStep.getAttributes();
        Map<String, String> parameters = attributes.getAttributeMap();
        JobParam jobParam = JSON.parseObject(JSON.toJSONString(parameters), JobParam.class);
        // 执行分段逻辑，生成子分段扩展参数，生成的每个Attributes则为一个分段
        List<Attributes> attributesList = new LinkedList<>();
        BatchContext batchContext = new BatchContext();
        batchContext.getParams().putAll(parameters);
        IStep stepObj = SpringContextHolder.getBean(jobParam.getStepName());
        if (stepObj instanceof AbstractSegmentStep) {
            attributesList = buildSegment((AbstractSegmentStep) stepObj, batchContext);
        } else if (stepObj instanceof AbstractRowNumStep) {
            attributesList = buildRowNum((AbstractRowNumStep) stepObj, batchContext);
        } else {
            throw new BatchException("不支持的Step类型");
        }
        // 添加子分段扩展信息，
        segmentedStepSender.addBatch(attributesList);
    }

    private List<Attributes> buildRowNum(AbstractRowNumStep abstractRowNumStep, BatchContext batchContext) {
        List<Attributes> attributesList = new ArrayList<>();
        List<String> nodes = abstractRowNumStep.getNodeList(batchContext);
        if (null == nodes || nodes.size() == 0) {
            return attributesList;
        }
        for (String node : nodes) {
            Map<String, String> attrMap = new HashMap<>();
            int countNum = abstractRowNumStep.getCountNum(batchContext, node);
            if (countNum <= 0) {
                break;
            }
            attrMap.put("beginIndex", "1");
            attrMap.put("endIndex", "" + countNum);
            attrMap.put("pageSize", "" + countNum);
            attrMap.put("node", node);
            Attributes segAttributes = new Attributes(attrMap);
            attributesList.add(segAttributes);
        }

        return attributesList;
    }

    private List<Attributes> buildSegment(AbstractSegmentStep abstractSegmentStep, BatchContext batchContext) {
        List<Attributes> attributesList = new ArrayList<>();
        List<String> nodes = abstractSegmentStep.getNodeList(batchContext);
        if (null == nodes || nodes.size() == 0) {
            return attributesList;
        }
        for (String node : nodes) {
            List<Segment> segments = abstractSegmentStep.getSegmentList(batchContext, node);
            if (null != segments && segments.size() != 0) {
                for (Segment segment : segments) {
                    Map<String, String> attrMap = new HashMap<>();
                    attrMap.put("beginIndex", segment.getStartKey());
                    attrMap.put("endIndex", segment.getEndKey());
                    attrMap.put("pageSize", String.valueOf(segment.getRowCount()));
                    attrMap.put("node", node);
                    Attributes segAttributes = new Attributes(attrMap);
                    attributesList.add(segAttributes);
                }
            }
        }
        return attributesList;
    }


}
