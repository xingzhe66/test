package com.dcits.comet.flow;

import com.dcits.comet.commons.data.BaseRequest;

/**
 * <p>Title: IExtraBusiness.java</p>
 * <p>Description: 业务扩展接口</p>
 * <p>Copyright: Copyright (c) 2014-2019</p>
 * <p>Company: dcits</p>
 * <p>2019/4/26 10:39</p>
 *
 * @author cuimh
 * @version v1.0
 */
public interface IExtraBusiness extends IExtraFlow {

    /**
     * @Author cuimh
     * @Description //业务前置扩展处理
     * @Date 2019/4/26 10:42
     * @Param [baseRequest]
     * @Return void
     **/
    void before(String beanName, BaseRequest baseRequest);

    /**
     * @Author cuimh
     * @Description //业务后置扩展处理
     * @Date 2019/4/26 10:43
     * @Param [baseRequest]
     * @Return void
     **/
    default void after(String beanName, BaseRequest baseRequest) {

    }
}
