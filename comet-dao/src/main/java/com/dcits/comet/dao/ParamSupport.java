package com.dcits.comet.dao;

import com.dcits.comet.dao.model.BasePo;
import com.dcits.comet.dao.model.QueryResult;

import java.util.List;
import java.util.Map;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-03-05 18:26
 * @Version 1.0
 **/
public interface ParamSupport extends BaseDaoSupport {
    String POSTFIX_BATCH_INSERT = ".insertBatch";

    <T extends BasePo> Integer count(String statementPostfix, T object);

    Integer count(String statementPostfix, Map<String, Object> parameter);

    <T extends BasePo> T selectForUpdate(T parameter);

    <T extends BasePo> T selectForUpdate(String statementPostfix, T parameter);

    <T extends BasePo> T selectOne(String statementPostfix, T parameter);

    <T extends BasePo> T selectOne(String statementPostfix, Map<String, Object> parameter);

    <T extends BasePo> int update(String statementPostfix, T setParameter, T whereParameter);

    <T extends BasePo> int update(String statementPostfix, T entity);

    <T extends BasePo> int update(String statementPostfix, List<T> list) ;

    int update(String statementPostfix, Map<String, Object> parameter);

    <T extends BasePo> int delete(String statementPostfix, T entity);

    int delete(String statementPostfix, Map<String, Object> parameter);

    <T extends BasePo> List<T> selectList(String statementPostfix, T entity);

    <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter,T entity);

    List selectList(String statementPostfix, Map<String, Object> parameter);

    <T extends BasePo> List<T> selectByPage(T entity);

    <T extends BasePo> List<T> selectList(T entity, int pageIndex, int pageSize);

    <T extends BasePo> List<T> selectList(String statementPostfix, T entity, int pageIndex, int pageSize);

    <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize);

    <T extends BasePo> QueryResult<T> selectQueryResult(T entity, int pageIndex, int pageSize);

    <T extends BasePo> QueryResult<T> selectQueryResult(String statementPostfix, T entity, int pageIndex, int pageSize);

    <T extends BasePo> QueryResult<T> selectQueryResult(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize);

    <T extends BasePo> List<T> selectListForUpdate(String statementPostfix, T entity);

    <T extends BasePo> List<T> selectListForUpdate(T entity);

    <T extends BasePo> int insertBatch(List<T> list);

    List selectSegmentList(String statementPostfix, Map<String, Object> parameter,int pageSize);
}
