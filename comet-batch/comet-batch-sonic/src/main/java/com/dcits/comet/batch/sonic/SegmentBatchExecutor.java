package com.dcits.comet.batch.sonic;

import com.alibaba.fastjson.JSON;
import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.launcher.IJobLauncher;
import com.dcits.comet.batch.launcher.JobParam;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.sonic.exception.BatchServiceException;
import com.dcits.comet.commons.utils.StringUtil;
import com.dcits.sonic.executor.api.ReportCompleted;
import com.dcits.sonic.executor.api.model.Attributes;
import com.dcits.sonic.executor.step.StepResult;
import com.dcits.sonic.executor.step.segment.SegmentRunningStep;
import com.dcits.sonic.executor.step.segment.SegmentStepExecutor;
import com.dcits.sonic.executor.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 分段执行器
 *
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 14:38
 **/
@Slf4j
public class SegmentBatchExecutor implements SegmentStepExecutor {
    @Override
    public ReportCompleted execute(SegmentRunningStep segmentRunningStep) {
        try {
            log.debug("SegmentRunningStep{}", segmentRunningStep.toString());
            //插件定义扩展参数
            Attributes attributes = segmentRunningStep.getAttributes();
            // 获取分段扩展参数，该参数为分段类生成的分段扩展参数
            Attributes segAttributes = segmentRunningStep.getSegmentAttributes();
            Map<String, String> parameters = attributes.getAttributeMap();
            log.debug("parameters{}", parameters);
            log.debug("segAttributes{}", segAttributes);

            JobParam jobParam = JSON.parseObject(JSON.toJSONString(parameters), JobParam.class);
            if (StringUtil.isEmpty(jobParam.getExeId())) {
                jobParam.setExeId(segmentRunningStep.getStepRunId());
            }

            BatchContext batchContext = new BatchContext();
            String params = parameters.get("params");

            if (StringUtils.isNotEmpty(params)) {
                batchContext.setParams(JsonUtil.jsonToMap(params));
            }
            jobParam.setBatchContext(batchContext);
            //TODO 删除测试
            //jobParam.setStepName("GenPostStep");
            //jobParam.setBeginIndex(1);
            //jobParam.setEndIndex(500000);
            //jobParam.setPageSize(500000);
            //TODO 删除区间
            IJobLauncher jobLauncher = SpringContextHolder.getBean(IJobLauncher.class);
            jobLauncher.run(jobParam.getStepName(), jobParam);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BatchServiceException(e.getMessage(), e);
        }
        //返回执行结果
        return StepResult.SUCCESS_BLANK;
    }
}
