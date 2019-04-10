package com.dcits.comet.flow;

import com.dcits.comet.commons.Context;
import com.dcits.comet.commons.data.BaseRequest;
import com.dcits.comet.commons.data.BaseResponse;
import com.dcits.comet.commons.data.BusinessResult;
import com.dcits.comet.commons.data.head.SysHead;
import com.dcits.comet.commons.data.head.SysHeadOut;
import com.dcits.comet.commons.exception.BusinessException;
import com.dcits.comet.commons.utils.BusiUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-26 18:08
 * @Version 1.0
 **/

@Slf4j
public abstract class AbstractFlow<IN extends BaseRequest, OUT extends BaseResponse> implements IFlow<IN, OUT> {

    public abstract OUT execute(IN input);

    @Override
    public OUT handle(IN input) {

        String className = this.getClass().getSimpleName();

        // preHandle
        preHandler(input);
        Stopwatch stopwatch = Stopwatch.createStarted();
        OUT output = null;
        log.info("The [{}] flow elapsedTime is [{}]", className, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        try {
            output = this.execute(input);

            if (BusiUtil.isNull(output)) {
                log.warn("The [{}] flow returned no result! ", className);

                //1、返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
                Type genType = getClass().getGenericSuperclass();
                //2、泛型参数
                Type[] types = ((ParameterizedType) genType).getActualTypeArguments();
                try {
                    //这里需要强转得到的是实体类类路径
                    output = (OUT) ((Class) types[1]).newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            if (BusiUtil.isNull(input.getAppHead())) {
                BusinessResult.success(output);
            }

            if (BusiUtil.isNotNull(input.getAppHead())) {
                BusinessResult.success(output, input.getAppHead());
            }

            // endHandle
            postHandler(input, output);
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                BusinessResult.error(businessException.getErrorCode(), businessException.getErrorMessage());
            }
            throw e;

        } finally {

        }

        return output;
    }

    private void preHandler(IN input) {
        log.info("<===== execute preHandler =====>");
    }

    private void postHandler(IN input, OUT out) {
        log.info("<===== execute postHandler =====>");
        setOriginalSysHead(out.getSysHead(), input.getSysHead());
    }

    /**
     * 设置返回sysHead
     *
     * @param in
     */
    private void setOriginalSysHead(SysHeadOut out, SysHead in) {
        out.setAuthUserId(in.getAuthUserId());
        out.setTranDate(in.getTranDate());
        out.setTranMode(in.getTranMode());
        out.setSourceBranchNo(in.getSourceBranchNo());
        out.setMessageCode(in.getMessageCode());
        out.setProgramId(in.getProgramId());
        out.setThreadNo(in.getThreadNo());
        out.setDestBranchNo(in.getDestBranchNo());
        out.setMacValue(in.getMacValue());
        out.setSourceType(in.getSourceType());
        out.setFilePath(in.getFilePath());
        out.setSystemId(in.getSystemId());
        out.setCompany(in.getCompany());
        out.setReference(Context.getInstance().getReference());
        out.setRunDate(Context.getInstance().getRunDate());
        out.setSceneId(in.getSceneId());
    }

}
