package com.dcits.comet.dao;

import com.dcits.comet.dao.model.BasePo;

import java.util.List;

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

    /**
     * 数量查询方法
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends BasePo> Integer count(T entity);

    /**
     * 单笔查询方法
     *
     * @param parameter
     * @param <T>
     * @return
     */
    <T extends BasePo> T selectOne(T parameter);

    /**
     * 多笔查询方法
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends BasePo> List<T> selectList(T entity);

    /**
     * 查询全部数据方法
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends BasePo> List<T> selectAll(T entity);

    /**
     * 插入方法
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends BasePo> int insert(T entity);

    /**
     * 数组插入方法
     *
     * @param list
     * @param <T>
     * @return
     */
    <T extends BasePo> int insert(List<T> list);

    /**
     * 更新方法
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends BasePo> int update(T entity);

    /**
     * 删除方法
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends BasePo> int delete(T entity);
    /**
     * 更新方法
     *
     * @param setParameter,whereParameter
     * @param <T>
     * @return
     */
    <T extends BasePo> int update(T setParameter, T whereParameter);


    <T extends BasePo> List<T> selectList(String statementPostfix, T entity);
}
