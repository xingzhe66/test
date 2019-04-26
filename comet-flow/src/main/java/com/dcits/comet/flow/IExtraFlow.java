package com.dcits.comet.flow;

import com.dcits.comet.commons.data.BaseRequest;

/**
 * <p>Title: IExtraFlow.java</p>
 * <p>Description: 扩展流程接口</p>
 * <p>Copyright: Copyright (c) 2014-2019</p>
 * <p>Company: dcits</p>
 * <p>2019/4/26 10:27</p>
 *
 * @author cuimh
 * @version v1.0
 */
public interface IExtraFlow {
    /**
     * @Author cuimh
     * @Description //业务执行顺序定义
     * @Date 2019/4/17 13:24
     * @Param []
     * @Return int
     **/
    int getOrder();

    /**
     * @Author cuimh
     * @Description //是否生效
     * @Date 2019/4/17 13:24
     * @Param []
     * @Return java.lang.Boolean
     **/
    Boolean isEffective();
}
