package com.dcits.comet.batch.service.controller;

import com.dcits.comet.batch.launcher.CommonJobLauncher;
import com.dcits.comet.batch.launcher.JobExeResult;
import com.dcits.comet.batch.launcher.JobParam;
import com.dcits.comet.batch.service.constant.BatchServiceConstant;
import com.dcits.comet.batch.service.exception.BatchServiceException;
import com.dcits.comet.batch.service.model.ExeInput;
import com.dcits.comet.batch.service.model.ExeOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;

import static java.lang.String.format;

@RequestMapping("/batch/")
@RestController
public class ExecutionController {
    @Autowired
    ConfigurableApplicationContext context;
    @Autowired
    CommonJobLauncher commonJobLauncher;


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


}
