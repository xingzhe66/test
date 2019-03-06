package com.dcits.comet.dao;

import com.dcits.comet.dao.model.BasePo;
import com.dcits.comet.dao.model.QueryResult;

import java.util.List;
import java.util.Map;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-03-05 18:28
 * @Version 1.0
 **/
public interface BaseDaoSupport {

    String POSTFIX_COUNT = ".count";
    String POSTFIX_SELECTONE = ".selectOne";
    String POSTFIX_INSERT = ".insert";
    String POSTFIX_UPDATE = ".update";
    String POSTFIX_UPDATE_BY_ENTITY = ".updateByEntity";
    String POSTFIX_DELETE = ".delete";
    String POSTFIX_SELECTLIST = ".selectList";


    <T extends BasePo> Integer count(T entity);

    <T extends BasePo> T selectOne(T parameter);

    <T extends BasePo> int insert(T entity);

    <T extends BasePo> int insert(List<T> list);

    <T extends BasePo> int update(T setParameter, T whereParameter);

    <T extends BasePo> int delete(T entity);

    <T extends BasePo> List<T> selectList(T entity);

}
