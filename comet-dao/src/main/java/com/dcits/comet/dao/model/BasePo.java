package com.dcits.comet.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 持久化对象基类
 * 
 */
public abstract class BasePo implements Serializable {
    private static final long serialVersionUID = 1406072669738004984L;
    /** 租户Id */
    private Long tendId;

    /** 排序 */
    private List<Order> orderBy;
    /**
     *日期查询封装查询  ,  更换操作符
     */
    private  Map<String, Object>  additionalParameter;

    public Long getTendId() {
        return tendId;
    }

    public void setTendId(Long tendId) {
        this.tendId = tendId;
    }

    public final List<Order> getOrderBy() {
        return orderBy;
    }

    final void setOrderBy(List<Order> orderBy) {
        this.orderBy = orderBy;
    }

    public void addOrder(Order order) {
        if (orderBy == null) {
            orderBy = new ArrayList<Order>();
        }
        orderBy.add(order);
    }

    public Map<String, Object> getAdditionalParameter() {
        return additionalParameter;
    }

    public void putAdditionalParameter(String key, Object parameterValue) {
        if (additionalParameter == null) {
            additionalParameter = new HashMap<>(1);
        }
        additionalParameter.put(key, parameterValue);
    }


}
