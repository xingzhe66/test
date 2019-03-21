package com.dcits.comet.batch.service.handler;

import com.dcits.comet.batch.service.constant.BatchServiceConstant;
import com.dcits.comet.batch.service.exception.BatchServiceException;
import com.dcits.comet.batch.service.model.ExeOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description BatchServiceExceptionHandler
 */
@ControllerAdvice
public class BatchServiceExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    public
    @ResponseBody
    ExeOutput UnknownExceptionHandler(Exception exception) {
        ExeOutput exeOutput = new ExeOutput();
        exeOutput.setServiceStatus(BatchServiceConstant.SERVICE_STATUS_ERROR);
        exeOutput.setExceptionMsg(exception.getMessage());
        exeOutput.setExceptionCode(BatchServiceConstant.SERVICE_EXCEPTION_UNKNOWN);

        logger.error("error", exception);
        return exeOutput;
    }

    @ExceptionHandler(value = BatchServiceException.class)
    public
    @ResponseBody
    ExeOutput ServiceExceptionHandler(BatchServiceException exception) {
        ExeOutput exeOutput = new ExeOutput();
        exeOutput.setServiceStatus(BatchServiceConstant.SERVICE_STATUS_ERROR);
        exeOutput.setExceptionMsg(exception.getMessage());
        exeOutput.setExceptionCode(exception.getExceptionCode());

        logger.error("error", exception);
        return exeOutput;
    }
}
