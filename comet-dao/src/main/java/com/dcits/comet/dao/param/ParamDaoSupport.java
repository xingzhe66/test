package com.dcits.comet.dao.param;

import com.dcits.comet.dao.model.BasePo;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

import static com.dcits.comet.dao.BaseDaoSupport.*;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-03-06 8:57
 * @Version 1.0
 **/

public class ParamDaoSupport extends SqlSessionDaoSupport {

    @Cacheable(value = "param", key = "#cacheKey")
    public <T extends BasePo> Integer count(T entity, String cacheKey) {
        String className = entity.getClass().getName();
        return (Integer) this.getSqlSession().selectOne(className + POSTFIX_COUNT, entity);
    }

    @Cacheable(value = "param", key = "#cacheKey")
    public <T extends BasePo> T selectOne(T entity, String cacheKey) {
        String className = entity.getClass().getName();
        return (T) this.getSqlSession().selectOne(className + POSTFIX_SELECTONE, entity);
    }

    @Cacheable(value = "param", key = "#cacheKey")
    public <T extends BasePo> List<T> selectList(T entity, String cacheKey) {
        String statementPostfix = entity.getClass().getName() + POSTFIX_SELECTLIST;
        return this.getSqlSession().selectList(statementPostfix, entity);
    }

    @Cacheable(value = "param", key = "#cacheKey")
    public <T extends BasePo> List<T> selectAll(T entity, String cacheKey) {
        String statementPostfix = entity.getClass().getName() + POSTFIX_SELECTLIST;
        return this.getSqlSession().selectList(statementPostfix, entity);
    }

    @CacheEvict(value = "param", key = "#cacheKey")
    public <T extends BasePo> int insert(T entity, String cacheKey) {
        String className = entity.getClass().getName();
        return this.getSqlSession().insert(className + POSTFIX_INSERT, entity);
    }

    @CacheEvict(value = "param", key = "#cacheKey")
    public <T extends BasePo> int insert(List<T> list, String cacheKey) {
        String className = list.get(0).getClass().getName();
        return this.getSqlSession().insert(className + POSTFIX_INSERT, list);
    }

    @CacheEvict(value = "param", key = "#cacheKey")
    public <T extends BasePo> int update(T entity,String cacheKey) {
        String className = entity.getClass().getName();
        return this.getSqlSession().update(className + POSTFIX_UPDATE, entity);
    }

    @CacheEvict(value = "param", key = "#cacheKey")
    public <T extends BasePo> int delete(T entity, String cacheKey) {
        String className = entity.getClass().getName();
        return this.getSqlSession().delete(className + POSTFIX_DELETE, entity);
    }

}

