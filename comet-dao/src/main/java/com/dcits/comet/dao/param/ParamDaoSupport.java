package com.dcits.comet.dao.param;

import com.dcits.comet.dao.model.BasePo;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dcits.comet.dao.BaseDaoSupport.POSTFIX_COUNT;
import static com.dcits.comet.dao.BaseDaoSupport.POSTFIX_DELETE;
import static com.dcits.comet.dao.BaseDaoSupport.POSTFIX_INSERT;
import static com.dcits.comet.dao.BaseDaoSupport.POSTFIX_SELECTLIST;
import static com.dcits.comet.dao.BaseDaoSupport.POSTFIX_SELECTONE;
import static com.dcits.comet.dao.BaseDaoSupport.POSTFIX_UPDATE;
import static com.dcits.comet.dao.BaseDaoSupport.POSTFIX_UPDATE_BY_ENTITY;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-03-06 8:57
 * @Version 1.0
 **/

public class ParamDaoSupport extends SqlSessionDaoSupport{

    @Cacheable(value = "param", key = "#cacheKey",unless="#result == null")
    public <T extends BasePo> Integer count(T entity, String cacheKey) {
        String className = entity.getClass().getName();
        return (Integer) this.getSqlSession().selectOne(className + POSTFIX_COUNT, entity);
    }

    @Cacheable(value = "param", key = "#cacheKey",unless="#result == null")
    public <T extends BasePo> Integer count( String statementPostfix,T entity, String cacheKey) {
        return (Integer) this.getSqlSession().selectOne(statementPostfix, entity);
    }
    @Cacheable(value = "param", key = "#cacheKey",unless="#result == null")
    public <T extends BasePo> T selectOne(T entity, String cacheKey) {
        String className = entity.getClass().getName();
        return (T) this.getSqlSession().selectOne(className + POSTFIX_SELECTONE, entity);
    }
    @Cacheable(value = "param", key = "#cacheKey",unless="#result == null")
    public <T extends BasePo> T selectOne(String statementPostfix,T entity, String cacheKey) {
        return (T) this.getSqlSession().selectOne(statementPostfix, entity);
    }
    @Cacheable(value = "param", key = "#cacheKey",unless="#result == null")
    public <T extends BasePo> List<T> selectList(String statementPostfix, T entity, String cacheKey) {
        return this.getSqlSession().selectList(statementPostfix, entity);
    }
    @Cacheable(value = "param", key = "#cacheKey",unless="#result == null")
    public <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter) {
        return this.getSqlSession().selectList(statementPostfix, parameter);
    }

    @Cacheable(value = "param", key = "#cacheKey",unless="#result == null")
    public <T extends BasePo> List<T> selectList(T entity, String cacheKey) {
        String statementPostfix = entity.getClass().getName() + POSTFIX_SELECTLIST;
        return this.getSqlSession().selectList(statementPostfix, entity);
    }
    @Cacheable(value = "param", key = "#cacheKey",unless="#result == null")
    public <T extends BasePo> List<T> selectAll(T entity, String cacheKey) {
        String statementPostfix = entity.getClass().getName() + POSTFIX_SELECTLIST;
        return this.getSqlSession().selectList(statementPostfix, entity);
    }

    @CacheEvict(value = "param", key = "#cacheKey",beforeInvocation=true)
    public <T extends BasePo> int insert(T entity, String cacheKey) {
        String className = entity.getClass().getName();
        return this.getSqlSession().insert(className + POSTFIX_INSERT, entity);
    }

    @CacheEvict(value = "param", key = "#cacheKey",beforeInvocation=true)
    public <T extends BasePo> int insert(List<T> list, String cacheKey) {
        String className = list.get(0).getClass().getName();
        return this.getSqlSession().insert(className + POSTFIX_INSERT, list);
    }

    @CacheEvict(value = "param", key = "#cacheKey",beforeInvocation=true)
    public <T extends BasePo> int update(T entity,String cacheKey) {
        String className = entity.getClass().getName();
        return this.getSqlSession().update(className + POSTFIX_UPDATE, entity);
    }
    @CacheEvict(value = "param", key = "#cacheKey",beforeInvocation=true)
    public <T extends BasePo> int update(String statementPostfix,T entity,String cacheKey) {
        return this.getSqlSession().update(statementPostfix, entity);
    }
    @CacheEvict(value = "param", key = "#cacheKey",beforeInvocation=true)
    public <T extends BasePo> int delete(T entity, String cacheKey) {
        String className = entity.getClass().getName();
        return this.getSqlSession().delete(className + POSTFIX_DELETE, entity);
    }

    @CacheEvict(value = "param", key = "#cacheKey",beforeInvocation=true)
    public <T extends BasePo> int update(T setParameter, T whereParameter,String cacheKey) {
        Map<String, Object> parameter = new HashMap(2);
        parameter.put("s", setParameter);
        parameter.put("w", whereParameter);
        String className = setParameter.getClass().getName();
        return this.getSqlSession().update(className + POSTFIX_UPDATE_BY_ENTITY, parameter);
    }
    @CacheEvict(value = "param", key = "#cacheKey",beforeInvocation=true)
    public <T extends BasePo> int update(String statementPostfix,T setParameter, T whereParameter,String cacheKey) {
        Map<String, Object> parameter = new HashMap(2);
        parameter.put("s", setParameter);
        parameter.put("w", whereParameter);
        return this.getSqlSession().update(statementPostfix, parameter);
    }
}

