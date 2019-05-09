package com.dcits.comet.flow;

import com.dcits.comet.commons.Context;
import com.dcits.comet.commons.data.BaseRequest;
import com.dcits.comet.commons.data.BaseResponse;
import com.dcits.comet.commons.data.BusinessResult;
import com.dcits.comet.commons.data.head.SysHead;
import com.dcits.comet.commons.data.head.SysHeadOut;
import com.dcits.comet.commons.exception.BusinessException;
import com.dcits.comet.commons.utils.BusiUtil;
import com.dcits.comet.commons.utils.StringUtil;
import com.dcits.comet.flow.service.FlowService;
import com.dcits.comet.flow.service.MqService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
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

    @Autowired
    FlowService flowService;
    @Autowired
    MqService mqService;

    @Override
    public OUT handle(String beanName, IN input) {

        // preHandle
        preHandler(input);

        //preBusinessHandler
        preBusinessHandler(beanName, input);

        Stopwatch stopwatch = Stopwatch.createStarted();
        OUT output = null;
        log.info("The [{}] flow elapsedTime is [{}]", beanName, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        try {
            output = this.execute(input);

            if (BusiUtil.isNull(output)) {
                log.warn("The [{}] flow returned no result! ", beanName);

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

            //postBusinessHandler
            postBusinessHandler(beanName, input);
            // endHandle
            postHandler(input, output);
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                BusinessResult.error(businessException.getErrorCode(), businessException.getErrorMessage());
            }
            //更新流程和消息状态
            try {
                flowService.updateFlowException(e);
                mqService.updateExceptionStatus();
            } catch (Exception e1) {
                log.info("update  Status  Exception  fail------------------");
                e1.printStackTrace();
            }
            throw e;
        } finally {

        }
        return output;
    }

    private void preHandler(IN input) {
        log.info("<===== execute preHandler =====>");
        //生成flowId  //TODO 需要调用sequence
        String flowId = StringUtil.getUUID();
        //将flowId赋值给上下文Context
        Context.getInstance().setFlowId(flowId);
        //在流程开始时，将流程信息存入表中
        try {
            flowService.saveFlowInFo(input, this.getClass().getSimpleName());
        } catch (Exception e) {
            log.info("save flow inFo fail-----------------");
            e.printStackTrace();
        }
        //getEffectiveTrace
        List<IExtraTrace> traceList = ExtraFactory.getEffectiveExtra(IExtraTrace.class);

        //loop trace
        if (BusiUtil.isNotNull(traceList)) {
            log.info("<===== loop trace =====>");
            traceList.forEach(iExtraTrace -> iExtraTrace.trace(input));
        }
    }

    private void postHandler(IN input, OUT out) {
        log.info("<===== execute postHandler =====>");
        setOriginalSysHead(out.getSysHead(), input.getSysHead());
        //流程执行成功，发送mq
        try {
            //更新流程执行状态  3
            flowService.updateFlowSusscee(out);
            //更新消息发送状态  2 并真实发送消息
             mqService.mqHandler();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
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


    public void preBusinessHandler(String beanName, IN input) {
        //getEffectiveExtra
        List<IExtraBusiness> businessList = ExtraFactory.getEffectiveExtra(IExtraBusiness.class);
        //loop IExtraBusiness
        if (BusiUtil.isNotNull(businessList)) {
            log.info("<===== loop IExtraBusiness =====>");
            businessList.forEach(business -> business.before(beanName, input));
        }
    }

    public void postBusinessHandler(String beanName, IN input) {
        //getEffectiveExtra
        List<IExtraBusiness> businessList = ExtraFactory.getEffectiveExtra(IExtraBusiness.class);
        //loop IExtraBusiness
        if (BusiUtil.isNotNull(businessList)) {
            log.info("<===== loop IExtraBusiness =====>");
            businessList.forEach(business -> business.after(beanName, input));
        }
    }

}
