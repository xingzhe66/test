package com.dcits.comet.dao.param;


import com.dcits.comet.dao.ParamSupport;
import com.dcits.comet.dao.model.BasePo;
import com.dcits.comet.dao.model.QueryResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @Author chengliang
 * @Description 参数操作
 * @Date 2019-03-05 18:37
 * @Version 1.0
 **/
@Slf4j
public class ParamSupportImpl implements ParamSupport {

    private ParamDaoSupport paramDaoSupport;

    public ParamSupportImpl(ParamDaoSupport paramDaoSupport) {
        this.paramDaoSupport = paramDaoSupport;
    }

    @Override
    public <T extends BasePo> Integer count(T entity) {
        String cacheKey = CacheUtil.getCacheKey(entity);
        return paramDaoSupport.count(entity, cacheKey);
    }

    @Override
    public <T extends BasePo> T selectOne(T entity) {
        String cacheKey = CacheUtil.getCacheKey(entity);
        return paramDaoSupport.selectOne(entity, cacheKey);
    }

    @Override
    public <T extends BasePo> List<T> selectList(T entity) {
        String cacheKey = CacheUtil.getCacheKey(entity);
        return paramDaoSupport.selectList(entity, cacheKey);
    }

    @Override
    public <T extends BasePo> List<T> selectAll(T entity) {
        String cacheKey = CacheUtil.getCacheKeyAll(entity);
        return paramDaoSupport.selectAll(entity, cacheKey);
    }

    @Override
    public <T extends BasePo> int insert(T entity) {
        String cacheKey = CacheUtil.getCacheKey(entity);
        return paramDaoSupport.insert(entity, cacheKey);
    }

    @Override
    public <T extends BasePo> int insert(List<T> list) {
        String cacheKey = CacheUtil.getCacheKey(list.get(0));
        return paramDaoSupport.insert(list, cacheKey);
    }

    @Override
    public <T extends BasePo> int update(T entity) {
        String cacheKey = CacheUtil.getCacheKey(entity);
        return paramDaoSupport.update(entity, cacheKey);
    }

    @Override
    public <T extends BasePo> int delete(T entity) {
        String cacheKey = CacheUtil.getCacheKey(entity);
        return paramDaoSupport.delete(entity, cacheKey);
    }

    @Override
    public <T extends BasePo> int update(T setParameter, T whereParameter) {
        String cacheKey = CacheUtil.getCacheKey(whereParameter);
        return paramDaoSupport.update(setParameter, whereParameter, cacheKey);
    }

    @Override
    public <T extends BasePo> Integer count(String statementPostfix, T object) {

        return null;
    }

    @Override
    public Integer count(String statementPostfix, Map<String, Object> parameter) {
        return null;
    }

    @Override
    public <T extends BasePo> T selectForUpdate(T parameter) {
        return null;
    }

    @Override
    public <T extends BasePo> T selectForUpdate(String statementPostfix, T parameter) {
        return null;
    }

    @Override
    public <T extends BasePo> T selectOne(String statementPostfix, T parameter) {
        String cacheKey = CacheUtil.getCacheKey(parameter);
        return paramDaoSupport.selectOne(statementPostfix, parameter, cacheKey);
    }

    @Override
    public <T extends BasePo> T selectOne(String statementPostfix, Map<String, Object> parameter) {
        return null;
    }

    @Override
    public <T extends BasePo> int update(String statementPostfix, T setParameter, T whereParameter) {
        return 0;
    }

    @Override
    public <T extends BasePo> int update(String statementPostfix, T entity) {
        return 0;
    }

    @Override
    public <T extends BasePo> int update(String statementPostfix, List<T> list) {
        return 0;
    }

    @Override
    public int update(String statementPostfix, Map<String, Object> parameter) {
        return 0;
    }

    @Override
    public <T extends BasePo> int delete(String statementPostfix, T entity) {
        return 0;
    }

    @Override
    public int delete(String statementPostfix, Map<String, Object> parameter) {
        return 0;
    }

    @Override
    public <T extends BasePo> List<T> selectList(String statementPostfix, T entity) {
        String cacheKey = CacheUtil.getCacheKey(entity);
        return paramDaoSupport.selectList(statementPostfix, entity, cacheKey);
    }

    @Override
    public <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter, T entity) {
        return null;
    }

    @Override
    public List selectList(String statementPostfix, Map<String, Object> parameter) {
        return null;
    }

    @Override
    public <T extends BasePo> List<T> selectByPage(T entity) {
        return null;
    }

    @Override
    public <T extends BasePo> List<T> selectList(T entity, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public <T extends BasePo> List<T> selectList(String statementPostfix, T entity, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public <T extends BasePo> QueryResult<T> selectQueryResult(T entity, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public <T extends BasePo> QueryResult<T> selectQueryResult(String statementPostfix, T entity, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public <T extends BasePo> QueryResult<T> selectQueryResult(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public <T extends BasePo> List<T> selectListForUpdate(String statementPostfix, T entity) {
        return null;
    }

    @Override
    public <T extends BasePo> List<T> selectListForUpdate(T entity) {
        return null;
    }

    @Override
    public <T extends BasePo> int insertBatch(List<T> list) {
        return 0;
    }

    @Override
    public List selectSegmentList(String statementPostfix, Map<String, Object> parameter, int pageSize) {
        return null;
    }
}
