package com.dcits.comet.mvc.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcits.comet.commons.Context;
import com.dcits.comet.commons.ThreadLocalManager;
import com.dcits.comet.commons.data.head.AppHead;
import com.dcits.comet.commons.data.head.SysHead;
import com.dcits.comet.mvc.interceptor.request.IBusinessRequestPreInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author chengliang
 * @Description 业务上下文初始化
 * @Date 2019-02-26 14:49
 * @Version 1.0
 **/
@Slf4j
@Component
public class InitContextInterceptorImpl implements IBusinessRequestPreInterceptor {

    /**
     * 系统头
     */
    public final static String SYS_HEAD = "sysHead";
    /**
     * 系统头
     */
    public final static String APP_HEAD = "appHead";
    /**
     * 业务执行顺序定义
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * http请求前置拦截器
     * 业务上下文初始化
     *
     * @param jsonObject
     */
    @Override
    public void businessPreHandle(JSONObject jsonObject) {
        log.info("<===== start InitContextInterceptor ===== >");
        log.info(jsonObject.toJSONString());

        JSONObject appHeadJSON = jsonObject.getJSONObject(APP_HEAD);

        AppHead appHead = JSON.toJavaObject(appHeadJSON, AppHead.class);

        JSONObject sysHeadJSON = jsonObject.getJSONObject(SYS_HEAD);

        SysHead sysHead = JSON.toJavaObject(sysHeadJSON, SysHead.class);

        Context context = Context.builder()
                .platformId(ThreadLocalManager.getUUID())
                .reference(ThreadLocalManager.getUUID())
//                .tranDate(sysHead.getTranDate())
//                .userId(sysHead.getUserId())
//                .userLang(sysHead.getUserLang())
//                .tranBranch(sysHead.getBranchId())
//                .sourceType(sysHead.getSourceType())
//                .seqNo(sysHead.getSeqNo())
//                .programId(sysHead.getProgramId())
//                .company(sysHead.getCompany())
                .isBatch(false)
                .sysHead(sysHead)
                .appHead(appHead)
                .build();

        context.init(context);

        log.info("context is: {}", Context.getInstance().toString());

        log.info("<===== end InitContextInterceptor ===== >");

    }
}
