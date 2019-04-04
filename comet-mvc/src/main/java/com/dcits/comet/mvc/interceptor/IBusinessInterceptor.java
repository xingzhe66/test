package com.dcits.comet.mvc.interceptor;

/**
 * 业务流程拦截器
 */
public interface IBusinessInterceptor {

    /**
     * 业务执行顺序定义
     *
     * @return
     */
    int getOrder();
}
