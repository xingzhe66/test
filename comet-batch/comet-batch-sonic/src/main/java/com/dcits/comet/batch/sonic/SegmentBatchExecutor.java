package com.dcits.comet.batch.sonic;

import com.alibaba.fastjson.JSON;
import com.dcits.comet.batch.exception.BatchException;
import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.launcher.IJobLauncher;
import com.dcits.comet.batch.launcher.JobExeResult;
import com.dcits.comet.batch.launcher.JobParam;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.sonic.executor.api.ReportCompleted;
import com.dcits.sonic.executor.api.model.Attributes;
import com.dcits.sonic.executor.step.StepResult;
import com.dcits.sonic.executor.step.segment.SegmentRunningStep;
import com.dcits.sonic.executor.step.segment.SegmentStepExecutor;
import com.dcits.sonic.executor.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
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
            jobParam.setExeId(segmentRunningStep.getStepRunId());
            jobParam.setNode(segAttributes.getAttribute("node"));
            if (null == jobParam.getPageSize() || 0 == jobParam.getPageSize()) {
                jobParam.setPageSize(Integer.parseInt(segAttributes.getAttribute("pageSize")));
            }
            jobParam.setBeginIndex(Integer.parseInt(segAttributes.getAttribute("beginIndex")));
            jobParam.setEndIndex(Integer.parseInt(segAttributes.getAttribute("endIndex")));

            jobParam.setSegmentStart(segAttributes.getAttribute("beginIndex"));
            jobParam.setSegmentEnd(segAttributes.getAttribute("endIndex"));

            BatchContext batchContext = new BatchContext();
            String params = parameters.get("params");

            if (StringUtils.isNotEmpty(params)) {
                batchContext.setParams(JsonUtil.jsonToMap(params));
            }
            String async = jobParam.getAsync();
            if (StringUtils.isEmpty(async)) {
                jobParam.setAsync("0");//默认同步
            }
            String runType = jobParam.getRunType();
            if (StringUtils.isEmpty(runType)) {
                jobParam.setRunType("1");//默认单线程
            }
            jobParam.setBatchContext(batchContext);
            //jobParam.setStepName("BatchStep");
            IJobLauncher jobLauncher = SpringContextHolder.getBean(IJobLauncher.class);
            JobExeResult jobExeResult = jobLauncher.run(jobParam.getStepName(), jobParam);
            if (!BatchStatus.COMPLETED.getBatchStatus().equals(jobExeResult.getJobExecution().getStatus().getBatchStatus())) {
                log.error("Job:[stepName:{},exeId={}]completed with the following parameters: [jobId={}] and the following status: [{}]", jobParam.getStepName(), jobParam.getExeId(), jobExeResult.getJobExecution().getJobId(), jobExeResult.getJobExecution().getStatus());
                String exceptionMessage = createMessageContent(jobExeResult.getJobExecution());
                throw new BatchException(exceptionMessage);
            }

        } catch (BatchException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        //返回执行结果
        return StepResult.SUCCESS_BLANK;
    }

    private String formatExceptionMessage(Throwable exception) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }

    private String createMessageContent(JobExecution jobExecution) {
        List<Throwable> exceptions = jobExecution.getAllFailureExceptions();
        StringBuilder content = new StringBuilder();
        content.append("Job execution #");
        content.append(jobExecution.getId());
        content.append(" of job instance #");
        content.append(jobExecution.getJobInstance().getId());
        content.append(" failed with following exceptions:");
        for (Throwable exception : exceptions) {
            content.append("");
            content.append(formatExceptionMessage(exception));
        }
        return content.toString();
    }
}
