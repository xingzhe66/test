package com.dcits.comet.flow;

import com.dcits.comet.commons.utils.SpringContextUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Title: TraceFactory.java</p>
 * <p>Description: TODO</p>
 * <p>Copyright: Copyright (c) 2014-2019</p>
 * <p>Company: dcits</p>
 * <p>2019/4/17 13:27</p>
 *
 * @author cuimh
 * @version v1.0
 */
public class TraceFactory {

    public static <E extends ITrace> List<E> getEffectiveTrace(Class<E> e) {
        Map<String, E> traceMap = SpringContextUtil.getBeansOfType(e);
        //转换为List
        List<E> traceList = new ArrayList(traceMap.values());
        // 按照getOrder 排序  升序
        Collections.sort(traceList, Comparator.comparing(E::getOrder));
        //将未生效的前置服务排除
        return traceList.stream().filter(E::isEffective).collect(Collectors.toList());
    }
}
