package com.dcits.comet.flow;


import com.dcits.comet.rpc.api.model.BaseRequest;

/**
 * <p>Title: IExtraTrace.java</p>
 * <p>Description: 登记平台流水接口 只允许在各业务模块的common中实现</p>
 * <p>Copyright: Copyright (c) 2014-2019</p>
 * <p>Company: dcits</p>
 * <p>2019/4/17 13:22</p>
 *
 * @author cuimh
 * @version v1.0
 */
public interface IExtraTrace extends IExtraFlow{


    /**
    * @Author cuimh
    * @Description //登记平台流水
    * @Date 2019/4/17 13:25
    * @Param [baseRequest]
    * @Return void
    **/
    void trace(BaseRequest baseRequest);
}
