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
import com.dcits.sonic.executor.step.normal.NormalRunningStep;
import com.dcits.sonic.executor.step.normal.NormalStepExecutor;
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
 * 普通Step开发,只需实现com.dcits.sonic.executor.step.normal.NormalStepExecutor接口，接口入参中可以拿到用户在开发时
 * 配置的自定义参数
 *
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 13:28
 * @see com.dcits.sonic.executor.step.normal.NormalStepExecutor
 **/
@Slf4j
public class NormalBatchExecutor implements NormalStepExecutor {
    @Override
    public ReportCompleted execute(NormalRunningStep normalRunningStep) {
        try {
            log.debug("NormalRunningStep{}", normalRunningStep.toString());
            //获取用户自定义拓展参数，开发时通过插件配置
            Attributes attributes = normalRunningStep.getAttributes();

            Map<String, String> parameters = attributes.getAttributeMap();
            log.debug("parameters{}", parameters);

            JobParam jobParam = JSON.parseObject(JSON.toJSONString(attributes.getAttributeMap()), JobParam.class);
            jobParam.setExeId(normalRunningStep.getStepRunId());

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
        } finally {

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

