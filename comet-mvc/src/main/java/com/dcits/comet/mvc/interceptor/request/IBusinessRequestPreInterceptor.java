package com.dcits.comet.mvc.interceptor.request;

import com.alibaba.fastjson.JSONObject;
import com.dcits.comet.mvc.interceptor.IBusinessInterceptor;

/**
 * http请求前置拦截器
 *
 * @Author Chengliang
 */
public interface IBusinessRequestPreInterceptor extends IBusinessInterceptor {

    /**
     * http请求前置拦截器
     *
     * @param jsonObject
     */
    void businessPreHandle(JSONObject jsonObject);
}
