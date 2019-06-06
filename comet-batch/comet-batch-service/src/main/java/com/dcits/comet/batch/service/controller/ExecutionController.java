package com.dcits.comet.batch.service.controller;

import com.dcits.comet.batch.ISegmentStep;
import com.dcits.comet.batch.IStep;
import com.dcits.comet.batch.Segment;
import com.dcits.comet.batch.dao.BatchContextDao;
import com.dcits.comet.batch.exception.BatchException;
import com.dcits.comet.batch.launcher.CommonJobLauncher;
import com.dcits.comet.batch.launcher.JobExeResult;
import com.dcits.comet.batch.launcher.JobParam;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.service.constant.BatchServiceConstant;
import com.dcits.comet.batch.service.exception.BatchServiceException;
import com.dcits.comet.batch.service.model.ExeInput;
import com.dcits.comet.batch.service.model.ExeOutput;
import com.dcits.comet.batch.service.model.QueryInput;
import com.dcits.comet.batch.service.model.QueryOutput;
import com.dcits.comet.batch.service.model.SegmentListInput;
import com.dcits.comet.batch.service.model.SegmentListOutput;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description 批量执行服务
 */
@RequestMapping("/batch/")
@RestController
@Slf4j
public class ExecutionController {
    @Autowired
    ConfigurableApplicationContext context;
    @Autowired
    CommonJobLauncher commonJobLauncher;
    @Autowired
    private JobRepository jobRepository;
    @Autowired(required = false)
    private BatchContextDao batchContextDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionController.class);

    @RequestMapping(value = "/exe", method = RequestMethod.POST)
    public ExeOutput exe(@RequestBody ExeInput exeInput) {

        JobExeResult jobExeResult = null;
        ExeOutput exeOutput = new ExeOutput();
        try {

            JobParam jobParam = new JobParam();
            BeanCopier beanCopier = BeanCopier.create(ExeInput.class, JobParam.class, false);
            beanCopier.copy(exeInput, jobParam, null);

            BeanCopier beanCopier2 = BeanCopier.create(ExeInput.class, ExeOutput.class, false);
            beanCopier2.copy(exeInput, exeOutput, null);

            jobExeResult = commonJobLauncher.run(exeInput.getStepName(), jobParam);

            if (null != jobExeResult) {
                exeOutput.setBatchContext(jobExeResult.getBatchContext());
                exeOutput.setCreateTime(jobExeResult.getJobExecution().getCreateTime());
                exeOutput.setEndTime(jobExeResult.getJobExecution().getEndTime());
                exeOutput.setExitStatus(jobExeResult.getJobExecution().getExitStatus());
                exeOutput.setStartTime(jobExeResult.getJobExecution().getStartTime());
                exeOutput.setLastUpdated(jobExeResult.getJobExecution().getLastUpdated());
                exeOutput.setStatus(jobExeResult.getJobExecution().getStatus());
                if (!BatchStatus.COMPLETED.getBatchStatus().equals(jobExeResult.getJobExecution().getStatus().getBatchStatus())) {
                    log.error("Job:[stepName:{},exeId={}]completed with the following parameters: [jobId={}] and the following status: [{}]", jobParam.getStepName(), jobParam.getExeId(), jobExeResult.getJobExecution().getJobId(), jobExeResult.getJobExecution().getStatus());
                    String exceptionMessage = createMessageContent(jobExeResult.getJobExecution());
                    throw new BatchException(exceptionMessage);
                }
            }


        } catch (Exception e) {
            log.error("batch执行异常");
            e.printStackTrace();
            throw e;
        }
        exeOutput.setServiceStatus(BatchServiceConstant.SERVICE_STATUS_SUCCESS);
        return exeOutput;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public QueryOutput query(@RequestBody QueryInput queryInput) {
        String stepName = queryInput.getStepName();
        String exeId = queryInput.getExeId();
        JobParameters jobParameter = new JobParametersBuilder()
//                    .addDate("date", new Date())
                .addString("exeId", exeId)
                .addString("stepName", stepName)
                .toJobParameters();
        JobExecution jobExecution = jobRepository.getLastJobExecution("job_" + stepName, jobParameter);
        if (jobExecution == null) {
            throw new BatchServiceException("step执行信息不存在");
        }

        QueryOutput queryOutput = new QueryOutput();

        BeanCopier beanCopier2 = BeanCopier.create(JobExecution.class, QueryOutput.class, false);
        beanCopier2.copy(jobExecution, queryOutput, null);
        BatchContext batchContext = batchContextDao.getBatchContext(exeId);
        //不可能step执行完成而batchContext为null，除非中断或事务问题；事务一致需要时间，所以需要重新查询
        if (jobExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode()) && null == batchContext) {
            //更新batchContext和jobExecution不在同一个事务，所以可能交易完成了，但上下文没有更新
            throw new BatchServiceException("请重新查询");
        }
        queryOutput.setExeId(exeId);
        queryOutput.setStepName(queryInput.getStepName());
        queryOutput.setBatchContext(batchContext);
        return queryOutput;


    }

    @RequestMapping(value = "/getsegmentlist", method = RequestMethod.POST)
    public SegmentListOutput getSegmentList(@RequestBody SegmentListInput segmentListInput) {
        IStep iStep = (IStep) context.getBean(segmentListInput.getStepName());
        SegmentListOutput segmentListOutput = new SegmentListOutput();
        List<Segment> list = new ArrayList<>();
        if (iStep instanceof ISegmentStep) {
            ISegmentStep segmentStep = ((ISegmentStep) iStep);
            List<String> nodes = segmentStep.getNodeList(segmentListInput.getBatchContext());
            if (null == nodes || nodes.size() == 0) {
                return null;
            }
            for (String node : nodes) {
                List list1 = segmentStep.getSegmentList(segmentListInput.getBatchContext(), node, segmentListInput.getSegmentSize(), segmentListInput.getKeyField(), segmentListInput.getStepName());
                if (list1 != null) list.addAll(list1);
            }

        } else {

        }
        segmentListOutput.setSegmentList(list);

        return segmentListOutput;
    }

    private String formatExceptionMessage(Throwable exception) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }

    public String createMessageContent(JobExecution jobExecution) {
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
