package com.dcits.comet.mvc.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcits.comet.commons.Context;
import com.dcits.comet.mvc.filter.RequestReaderHttpServletRequestWrapper;
import com.dcits.comet.mvc.interceptor.AbstractBusinessInterceptor;
import com.dcits.comet.mvc.interceptor.request.IBusinessRequestPreInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-26 14:49
 * @Version 1.0
 **/
@Slf4j
public class BusinessRequestInterceptorImpl extends AbstractBusinessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        //只有返回true才会往下执行，返回FALSE的话就会取消当前请求
        log.info("<=====Start BusinessRequestPreInterceptor preHandle =====> ");
        List<IBusinessRequestPreInterceptor> preInterceptorList = getBusinessInterceptor(IBusinessRequestPreInterceptor.class);

        JSONObject json = JSON.parseObject(new RequestReaderHttpServletRequestWrapper(request).getBody());

        preInterceptorList.forEach(preInterceptor -> preInterceptor.businessPreHandle(json));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("<=====Start BusinessRequestPreInterceptor postHandle=====> ");
        Context.getInstance().cleanResource();
    }
}
