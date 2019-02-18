
package com.dcits.comet.dao;

import com.dcits.comet.dao.model.BasePo;
import com.dcits.comet.dao.model.QueryResult;

import java.util.List;
import java.util.Map;

public interface DaoSupport {
    String POSTFIX_COUNT = ".count";
    String POSTFIX_SELECTONE = ".selectOne";
    String POSTFIX_INSERT = ".insert";
    String POSTFIX_UPDATE = ".update";
    String POSTFIX_UPDATE_BY_ENTITY = ".updateByEntity";
    String POSTFIX_DELETE = ".delete";
    String POSTFIX_SELECTLIST = ".selectList";


    <T extends BasePo> Integer count(T entity);

    <T extends BasePo> Integer count(String statementPostfix, T object);

    Integer count(String statementPostfix, Map<String, Object> parameter);

    <T extends BasePo> T selectOne(T parameter);

    <T extends BasePo> T selectForUpdate(T parameter);

    <T extends BasePo> T selectForUpdate(String statementPostfix, T parameter);

    <T extends BasePo> T selectOne(String statementPostfix, T parameter);

    <T extends BasePo> T selectOne(String statementPostfix, Map<String, Object> parameter);

    <T extends BasePo> int insert(T entity);

    <T extends BasePo> int insert(List<T> list);

    <T extends BasePo> int update(T entity);

    <T extends BasePo> int update(T setParameter, T whereParameter);

    <T extends BasePo> int update(String statementPostfix, T setParameter, T whereParameter);

    <T extends BasePo> int update(String statementPostfix, T entity);

    int update(String statementPostfix, Map<String, Object> parameter);

    <T extends BasePo> int delete(T entity);

    <T extends BasePo> int delete(String statementPostfix, T entity);

    int delete(String statementPostfix, Map<String, Object> parameter);

    <T extends BasePo> List<T> selectList(T entity);

    <T extends BasePo> List<T> selectList(String statementPostfix, T entity);

    <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter);

    <T extends BasePo> List<T> selectList(T entity, int pageIndex, int pageSize);

    <T extends BasePo> List<T> selectList(String statementPostfix, T entity, int pageIndex, int pageSize);

    <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize);

    <T extends BasePo> QueryResult<T> selectQueryResult(T entity, int pageIndex, int pageSize);

    <T extends BasePo> QueryResult<T> selectQueryResult(String statementPostfix, T entity, int pageIndex, int pageSize);

    <T extends BasePo> QueryResult<T> selectQueryResult(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize);
}
