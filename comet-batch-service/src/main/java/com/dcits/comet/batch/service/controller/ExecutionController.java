package com.dcits.comet.batch.service.controller;

import com.dcits.comet.batch.dao.BatchContextDao;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;

import static java.lang.String.format;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description 批量执行服务
 */
@RequestMapping("/batch/")
@RestController
public class ExecutionController {
    @Autowired
    ConfigurableApplicationContext context;
    @Autowired
    CommonJobLauncher commonJobLauncher;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private BatchContextDao batchContextDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionController.class);

    @RequestMapping(value = "/exe", method = RequestMethod.POST)
    public ExeOutput exe(@RequestBody ExeInput exeInput) {

        JobExeResult jobExeResult = null;
        ExeOutput exeOutput=new ExeOutput();
        try {

            JobParam jobParam=new JobParam();
            BeanCopier beanCopier = BeanCopier.create(ExeInput.class, JobParam.class, false);
            beanCopier.copy(exeInput,jobParam,null);

            BeanCopier beanCopier2 = BeanCopier.create(ExeInput.class, ExeOutput.class, false);
            beanCopier2.copy(exeInput,exeOutput,null);

            jobExeResult=commonJobLauncher.run(exeInput.getStepName(),jobParam);

            if(null!=jobExeResult) {
                exeOutput.setBatchContext(jobExeResult.getBatchContext());
                exeOutput.setCreateTime(jobExeResult.getJobExecution().getCreateTime());
                exeOutput.setEndTime(jobExeResult.getJobExecution().getEndTime());
                exeOutput.setExitStatus(jobExeResult.getJobExecution().getExitStatus());
                exeOutput.setStartTime(jobExeResult.getJobExecution().getStartTime());
                exeOutput.setLastUpdated(jobExeResult.getJobExecution().getLastUpdated());
                exeOutput.setStatus(jobExeResult.getJobExecution().getStatus());
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new BatchServiceException(e.getMessage(),e);
        }
        exeOutput.setServiceStatus(BatchServiceConstant.SERVICE_STATUS_SUCCESS);
        return exeOutput;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public QueryOutput query(@RequestBody QueryInput queryInput) {
        String stepName=queryInput.getStepName();
        String exeId=queryInput.getExeId();
        JobParameters jobParameter= new JobParametersBuilder()
//                    .addDate("date", new Date())
                .addString("exeId", exeId)
                .addString("stepName", stepName)
                .toJobParameters()
        ;
        JobExecution jobExecution = jobRepository.getLastJobExecution("job_" + stepName, jobParameter);
        if(jobExecution==null){
            throw new BatchServiceException("step执行信息不存在");
        }

        QueryOutput queryOutput=new QueryOutput();

        BeanCopier beanCopier2 = BeanCopier.create(JobExecution.class, QueryOutput.class, false);
        beanCopier2.copy(jobExecution,queryOutput,null);
        BatchContext batchContext=batchContextDao.getBatchContext(exeId);
        //不可能step执行完成而batchContext为null，除非中断或事务问题；事务一致需要时间，所以需要重新查询
        if(jobExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())&&null==batchContext){
            //更新batchContext和jobExecution不在同一个事务，所以后可能交易完成了，但上下文没有更新
            throw new BatchServiceException("请重新查询");
        }
        queryOutput.setExeId(exeId);
        queryOutput.setStepName(queryInput.getStepName());
        queryOutput.setBatchContext(batchContext);
        return queryOutput;


    }

}
