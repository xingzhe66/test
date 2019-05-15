package com.dcits.comet.flow.service;

import com.alibaba.fastjson.JSON;
import com.dcits.comet.commons.Context;
import com.dcits.comet.commons.data.BaseRequest;
import com.dcits.comet.commons.data.BaseResponse;
import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.flow.constant.FlowStatus;
import com.dcits.comet.flow.model.FlowInfoPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @ClassName FlowService
 * @Author guihj
 * @Date 2019/4/15 13:45
 * @Description TODO
 * @Version 1.0
 **/
@Slf4j
@Component
public class FlowService {
    @Autowired
    DaoSupport daoSupport;


    /**
     * @Author guihj
     * @Description 在flow开始保存流程执行相关信息
     * @Date 2019/4/15 14:08
     * @Param [in]
     * @return void
     **/
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void  saveFlowInFo(BaseRequest in,String flowClassName){
        FlowInfoPo flowInfoPo =new FlowInfoPo();
        flowInfoPo.setFlowId(Context.getInstance().getFlowId());
        flowInfoPo.setFlowClassName(flowClassName);
        String inStr=JSON.toJSONString(in);
        //如果报文入参长度大于2000，则进行处理
        if(inStr!=null && inStr.length()>2000){
            flowInfoPo.setFlowIn(inStr.substring(0,2000));
        }else{
            flowInfoPo.setFlowIn(inStr);
        }
        flowInfoPo.setFlowStatus(FlowStatus.STATUS_START);
        flowInfoPo.setStartTime(DateUtil.getCurrentDate());
        daoSupport.insert(flowInfoPo);
    }





    /**
     * @Author guihj
     * @Description 在flow执行成功结束后，修改flow的相关信息
     * @Date 2019/4/15 14:08
     * @Param [in]
     * @return void
     **/
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void  updateFlowSusscee(BaseResponse out) {
        FlowInfoPo flowInfoPo =new FlowInfoPo();
        String outStr=JSON.toJSONString(out);
        if(outStr!=null && outStr.length()>2000){
            flowInfoPo.setFlowOut(outStr.substring(0,2000));
        }else{
            flowInfoPo.setFlowOut(outStr);
        }
        flowInfoPo.setFlowStatus(FlowStatus.STATUS_SUCCESS);
        flowInfoPo.setEndTime(DateUtil.getCurrentDate());
        flowInfoPo.setFlowId(Context.getInstance().getFlowId());
        daoSupport.update(flowInfoPo);
    }



    /**
     * @Author guihj
     * @Description 在flow执行出现异常后，修改flow的相关信息
     * @Date 2019/4/15 14:08
     * @Param [in]
     * @return void
     **/
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void  updateFlowException(Exception out){
        FlowInfoPo flowInfoPo =new FlowInfoPo();
        String outStr=JSON.toJSONString(out);
        if(outStr!=null && outStr.length()>2000){
            flowInfoPo.setFlowOut(outStr.substring(0,2000));
        }else{
            flowInfoPo.setFlowOut(outStr);
        }
        flowInfoPo.setFlowStatus(FlowStatus.STATUS_EXCEPTION);
        flowInfoPo.setEndTime(DateUtil.getCurrentDate());
        flowInfoPo.setFlowId(Context.getInstance().getFlowId());
        daoSupport.update(flowInfoPo);
    }
}

