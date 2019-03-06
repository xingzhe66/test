package com.dcits.comet.dao.param;

import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dao.model.BasePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-03-06 8:57
 * @Version 1.0
 **/
@Component
public class ParamDaoSupport {
    @Autowired
    private DaoSupport daoSupport;

    @Cacheable(value = "param", key = "#cacheKey")
    public <T extends BasePo> Integer count(T entity, String cacheKey) {
        return daoSupport.count(entity);
    }

    @Cacheable(value = "param", key = "#cacheKey")
    public <T extends BasePo> T selectOne(T parameter, String cacheKey) {
        return daoSupport.selectOne(parameter);
    }

    @Cacheable(value = "param", key = "#cacheKey")
    public <T extends BasePo> List<T> selectList(T entity, String cacheKey) {
        return daoSupport.selectList(entity);
    }

    @Cacheable(value = "param", key = "#cacheKey")
    public <T extends BasePo> List<T> selectAll(T entity, String cacheKey) {
        return daoSupport.selectList(entity);
    }

    @CacheEvict(value = "param", key = "#cacheKey")
    public <T extends BasePo> int insert(T entity, String cacheKey) {
        return daoSupport.insert(entity);
    }

    @CacheEvict(value = "param", key = "#cacheKey")
    public <T extends BasePo> int insert(List<T> list, String cacheKey) {
        return daoSupport.insert(list);
    }

    @CacheEvict(value = "param", key = "#cacheKey")
    public <T extends BasePo> int update(T setParameter, T whereParameter, String cacheKey) {
        return daoSupport.update(setParameter, whereParameter);
    }

    @CacheEvict(value = "param", key = "#cacheKey")
    public <T extends BasePo> int delete(T entity, String cacheKey) {
        return daoSupport.delete(entity);
    }

}
