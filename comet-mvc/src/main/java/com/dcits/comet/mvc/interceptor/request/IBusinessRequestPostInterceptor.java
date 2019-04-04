package com.dcits.comet.mvc.interceptor.request;

import com.alibaba.fastjson.JSONObject;
import com.dcits.comet.mvc.interceptor.IBusinessInterceptor;

/**
 * http请求后置拦截器
 *
 * @Author Chengliang
 */
public interface IBusinessRequestPostInterceptor extends IBusinessInterceptor {

    /**
     * http请求后置拦截器
     *
     * @param jsonObject
     */
    void businessPostHandle(JSONObject jsonObject);
}
