package com.dcits.comet.mvc.handler;

import com.dcits.comet.commons.exception.BusinessException;
import com.dcits.comet.rpc.api.constants.ResponseEnum;
import com.dcits.comet.rpc.api.model.BaseResponse;
import com.dcits.comet.rpc.api.model.BusinessResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/***
 * @description 全局捕捉异常
 * @version V1.0
 * @author ChengLiang
 * @date 2019/3/13
 */
@RestControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public BaseResponse businessExceptionHandler(HttpServletRequest request, Exception e) {
        Throwable throwable = getBusinessException(e);
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            return BusinessResult.error(businessException.getErrorCode(), businessException.getErrorMessage());
        }
        return BusinessResult.error(ResponseEnum.FAILED.getCode(), ResponseEnum.FAILED.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResponse exceptionHandler(HttpServletRequest request, Exception e) {
        Throwable throwable = getBusinessException(e);
        return BusinessResult.error(ResponseEnum.FAILED.getCode(), ResponseEnum.FAILED.getMessage());
    }


    /**
     * 若有异常进行嵌套，打印出每个异常的堆栈信息，若包含自定义异常，返回最内部的BusinessException异常。
     *
     * @param e
     * @return
     */
    private Throwable getBusinessException(Throwable e) {
        if (e == null) {
            return null;
        } else if (e instanceof BusinessException) {
            e.printStackTrace();
            Throwable temp = getBusinessException(e.getCause());
            if (temp == null) {
                return e;
            } else {
                return temp;
            }
        } else {
            e.printStackTrace();
            return getBusinessException(e.getCause());
        }
    }
}
