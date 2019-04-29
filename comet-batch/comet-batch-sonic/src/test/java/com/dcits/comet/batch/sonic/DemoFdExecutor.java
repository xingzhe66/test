package com.dcits.comet.batch.sonic;


import java.util.*;

import com.dcits.comet.batch.IBStep;
import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.sonic.executor.api.ReportCompleted;
import com.dcits.sonic.executor.base.Parameters;
import com.dcits.sonic.executor.step.normal.NormalRunningStep;
import com.dcits.sonic.executor.step.normal.NormalStepExecutor;
import com.dcits.sonic.executor.step.StepContextManager;
import com.dcits.sonic.executor.step.segment.StepSegmentedStepSender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.text.Segment;

/**
 * @Author wangyun
 * @Date 2019/4/28
 **/
public class DemoFdExecutor implements NormalStepExecutor {

    public static final String STEP_NAME = "stepName";

    @Override
    public ReportCompleted execute(NormalRunningStep step) {
        //用户自定义参数
        Parameters params = step.getParameters();
//         执行分段逻辑 .....
        List<Segment> segments = doSegment(params);
        //生成分段信息
        StepSegmentedStepSender segmentedStepSender = new StepSegmentedStepSender(step);
        //批量添加分段，推荐分批生成分段、添加，避免内存溢出
//        segmentedStepSender.addBatch(segments);
        //分段结束后必须调用该方法
        segmentedStepSender.finished();
        //分段类约定返回null
        return null;
    }

    private List<Segment> doSegment(Parameters params) {
        List<Segment> segments=new ArrayList() ;
        //根据入参生成分段
//        ApplicationContext context= SpringContextHolder.getApplicationContext();
//        IBStep bstep= (IBStep) context.getBean((String) params.get(STEP_NAME));
//        BatchContext batchContext=new BatchContext();
//        batchContext.getParams().putAll(params);
//        List<String> nodes=bstep.getNodeList(batchContext);
//        for(String node:nodes) {
//            Segment segment=new Segment();
//            int countNum=bstep.getCountNum(batchContext,node);
////            segment.setStart(1);
////            segment.setEnd(countNum);
//            segments.add(segment);
//        }

        return segments;
    }
}
