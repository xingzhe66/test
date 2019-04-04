package com.dcits.comet.mvc.interceptor;


import com.dcits.comet.mvc.util.SpringContextUtil;

import java.util.*;

/**
 * 获取指定类型拦截器
 *
 * @Author Chengliang
 */
public abstract class AbstractBusinessInterceptor {

    public <E extends IBusinessInterceptor> List<E> getBusinessInterceptor(Class<E> e) {
        Map<String, E> interceptorMap = SpringContextUtil.getBeansOfType(e);
        //转换为List
        List<E> interceptorList = new ArrayList(interceptorMap.values());
        // 按照getOrder 排序  升序
        Collections.sort(interceptorList, Comparator.comparing(E::getOrder));
        return interceptorList;
    }
}
