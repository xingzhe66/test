package com.dcits.comet.batch.sonic;

import com.alibaba.fastjson.JSON;
import com.dcits.comet.batch.IBStep;
import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.model.ExeInput;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.sonic.executor.api.ReportCompleted;
import com.dcits.sonic.executor.step.segment.SegmentRunningStep;
import com.dcits.sonic.executor.step.segment.SegmentStepExecutor;
import com.dcits.sonic.executor.step.segment.StepSegmentedStepSender;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 分段Step开发.
 * 分段step的开发分为分段类开发及子分段执行类开发，分段类一般可以复用，分段类及执行类需实现接口
 * SegmentStepExecutor。
 * SegmentBatchExecutor:用于生成子任务返回给调度端
 *
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 14:13
 **/
@Slf4j
public class SegmentBatchExecutorPartition implements SegmentStepExecutor {
    @Override
    public ReportCompleted execute(SegmentRunningStep segmentRunningStep) {
        log.debug("SegmentRunningStep{}", segmentRunningStep.toString());
        //用户自定义拓展参数
        Map<String, Object> parameters = segmentRunningStep.getParameters();
        log.debug("parameters{}", parameters);
        ExeInput exeInput = JSON.parseObject(JSON.toJSONString(parameters), ExeInput.class);
        // 执行分段逻辑 .....
        List<SegmentRunningStep.Segment> AttributesList = doSegment(exeInput, parameters);
        //生成分段信息
        StepSegmentedStepSender segmentedStepSender = new StepSegmentedStepSender(segmentRunningStep);
        //批量添加分段，推荐分批生成分段、添加，避免内存溢出
        segmentedStepSender.addBatch(AttributesList);
        //分段结束后必须调用该方法
        segmentedStepSender.finished();
        //分段类约定返回null
        return null;
    }

    //生成分段执行用到的拓展参数，如偏移量等
    private List<SegmentRunningStep.Segment> doSegment(ExeInput exeInput, Map<String, Object> parameters) {
        List<SegmentRunningStep.Segment> segments = new ArrayList();
        //根据入参生成分段
        BatchContext batchContext = new BatchContext();
        batchContext.getParams().putAll(parameters);

        IBStep bstep = SpringContextHolder.getBean(exeInput.getStepName());
        List<String> nodes = bstep.getNodeList(batchContext);
        for (String node : nodes) {
            SegmentRunningStep.Segment segment = new SegmentRunningStep.Segment();
            int countNum = bstep.getCountNum(batchContext, node);
            segment.setStart(1);
            segment.setEnd(countNum);
            segments.add(segment);
        }
        return segments;
    }
}
