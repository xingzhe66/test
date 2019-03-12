package com.dcits.comet.dao.mybatis;

import com.dcits.comet.commons.utils.BeanUtil;
import com.dcits.comet.commons.utils.BusiUtil;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dao.annotation.TablePkScanner;
import com.dcits.comet.dao.interceptor.SelectForUpdateHelper;
import com.dcits.comet.dao.interceptor.TotalrecordHelper;
import com.dcits.comet.dao.model.BasePo;
import com.dcits.comet.dao.model.QueryResult;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author wangyun
 */
public class DaoSupportImpl extends SqlSessionDaoSupport implements DaoSupport {
    static final Logger LOGGER = LoggerFactory.getLogger(DaoSupportImpl.class);
    private static Map<String, Map<String, String>> propertyColumnMapper = new HashMap();

    public DaoSupportImpl() {
    }

    private void initPropertyColumnMapper() {
        Collection<ResultMap> rms = this.getSqlSession().getConfiguration().getResultMaps();
        Iterator iter = rms.iterator();

        while (true) {
            Object object;
            do {
                if (!iter.hasNext()) {
                    return;
                }

                object = iter.next();
            } while (!(object instanceof ResultMap));

            ResultMap rm = (ResultMap) object;
            List<ResultMapping> list = rm.getResultMappings();
            Map<String, String> map = new HashMap(10);
            Iterator it = list.iterator();

            while (it.hasNext()) {
                ResultMapping r = (ResultMapping) it.next();
                map.put(r.getProperty(), r.getColumn());
            }

            propertyColumnMapper.put(rm.getId(), map);
        }
    }

    public static Map<String, Map<String, String>> getPropertyColumnMapper() {
        return propertyColumnMapper;
    }


    @Override
    public <T extends BasePo> Integer count(T entity) {
        String className = entity.getClass().getName();
        return (Integer) this.getSqlSession().selectOne(className + ".count", entity);
    }

    @Override
    public <T extends BasePo> Integer count(String statementPostfix, T object) {
        return (Integer) this.getSqlSession().selectOne(statementPostfix, object);
    }

    @Override
    public Integer count(String statementPostfix, Map<String, Object> parameter) {
        return (Integer) this.getSqlSession().selectOne(statementPostfix, parameter);
    }

    @Override
    public <T extends BasePo> T selectOne(T entity) {
        String className = entity.getClass().getName();
        return (T) this.getSqlSession().selectOne(className + ".selectOne", entity);
    }

    @Override
    public <T extends BasePo> T selectOne(String statementPostfix, T object) {
        return (T) this.getSqlSession().selectOne(statementPostfix, object);
    }

    @Override
    public <T extends BasePo> T selectOne(String statementPostfix, Map<String, Object> parameter) {
        return (T) this.getSqlSession().selectOne(statementPostfix, parameter);
    }

    @Override
    public <T extends BasePo> T selectForUpdate(T parameter) {
        BasePo result;
        try {
            SelectForUpdateHelper.setSelectForUpdate();
            result = this.selectOne(parameter);
        } finally {
            SelectForUpdateHelper.cancelSelectForUpdate();
        }

        return (T) result;
    }

    @Override
    public <T extends BasePo> T selectForUpdate(String statementPostfix, T parameter) {
        BasePo result;
        try {
            SelectForUpdateHelper.setSelectForUpdate();
            result = this.selectOne(statementPostfix, parameter);
        } finally {
            SelectForUpdateHelper.cancelSelectForUpdate();
        }

        return (T) result;
    }

    @Override
    public <T extends BasePo> int insert(T entity) {
        String className = entity.getClass().getName();
        return this.getSqlSession().insert(className + ".insert", entity);
    }

    @Override
    public <T extends BasePo> int insert(List<T> list) {
        int i = 0;

        BasePo bp;
        for (Iterator it = list.iterator(); it.hasNext(); i += this.insert(bp)) {
            bp = (BasePo) it.next();
        }

        return i;
    }

    //@Override
    public <T extends BasePo> int update(T entity) {
        String className = entity.getClass().getName();
        return this.getSqlSession().update(className + ".update", entity);
    }

   // @Override
    public <T extends BasePo> int update(T setParameter, T whereParameter) {
        String className = setParameter.getClass().getName();
        return this.update(className + ".updateByEntity", setParameter, whereParameter);
    }

    //@Override
    public <T extends BasePo> int update(String statementPostfix, T setParameter, T whereParameter) {
        Map<String, Object> parameter = new HashMap(2);
        parameter.put("s", setParameter);
        parameter.put("w", whereParameter);
        return this.update(statementPostfix, parameter);
    }

    @Override
    public <T extends BasePo> int update(String statementPostfix, T entity) {
        return this.getSqlSession().update(statementPostfix, entity);
    }

    @Override
    public int update(String statementPostfix, Map<String, Object> parameter) {
        return this.getSqlSession().update(statementPostfix, parameter);
    }

    //@Override
    public <T extends BasePo> int delete(T entity) {
        String className = entity.getClass().getName();
        return this.getSqlSession().delete(className + ".delete", entity);
    }

    @Override
    public <T extends BasePo> int delete(String statementPostfix, T entity) {
        return this.getSqlSession().update(statementPostfix, entity);
    }

    @Override
    public int delete(String statementPostfix, Map<String, Object> parameter) {
        return this.getSqlSession().update(statementPostfix, parameter);
    }

    @Override
    public <T extends BasePo> List<T> selectList(T entity) {
        String statementPostfix = entity.getClass().getName() + ".selectList";
        return this.selectList(statementPostfix, entity);
    }

    /**
     * 主键通用查询
     *
     * @param entity
     * @param pkValue
     * @return
     */
    @Override
    public <T extends BasePo> T selectByPrimaryKey(T entity, Object... pkValue) {
        String className = entity.getClass().getName();
        T po = getPkObject(entity, false, pkValue);
        return (T) this.getSqlSession().selectOne(className + SELECT_BY_PRIMARYKEY, po);
    }

    /**
     * 主键通用动态更新
     *
     * @param entity
     * @return
     */
    @Override
    public <T extends BasePo> int updateByPrimaryKey(T entity) {
        String className = entity.getClass().getName();
        return this.getSqlSession().update(className + UPDATE_BY_PRIMARYKEY, entity);
    }

    /**
     * 根据主键删除记录
     *
     * @param entity
     * @return
     */
    @Override
    public <T extends BasePo> int deleteByPrimaryKey(T entity) {
        String className = entity.getClass().getName();
        return this.getSqlSession().delete(className + DELETE_BY_PRIMARYKEY, entity);
    }

    @Override
    public <T extends BasePo> List<T> selectList(String statementPostfix, T entity) {
        return this.getSqlSession().selectList(statementPostfix, entity);
    }

    @Override
    public <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter) {
        return this.getSqlSession().selectList(statementPostfix, parameter);
    }

    @Override
    public <T extends BasePo> List<T> selectList(T entity, int pageIndex, int pageSize) {
        String statementPostfix = entity.getClass().getName() + ".selectList";
        return this.selectList(statementPostfix, entity, pageIndex, pageSize);
    }

    @Override
    public <T extends BasePo> List<T> selectList(String statementPostfix, T entity, int pageIndex, int pageSize) {
        Boolean needTotalFlag = TotalrecordHelper.isNeadTotalRowCount();

        List result;
        try {
            TotalrecordHelper.setNeedTotalRowCount(false);
            result = this.getSqlSession().selectList(statementPostfix, entity, new RowBounds(pageIndex, pageSize));
        } finally {
            TotalrecordHelper.setNeedTotalRowCount(needTotalFlag);
        }

        return result;
    }

    @Override
    public <T extends BasePo> List<T> selectList(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize) {
        Boolean needTotalFlag = TotalrecordHelper.isNeadTotalRowCount();

        List result;
        try {
            TotalrecordHelper.setNeedTotalRowCount(false);
            result = this.getSqlSession().selectList(statementPostfix, parameter, new RowBounds(pageIndex, pageSize));
        } finally {
            TotalrecordHelper.setNeedTotalRowCount(needTotalFlag);
        }

        return result;
    }

    @Override
    public <T extends BasePo> QueryResult<T> selectQueryResult(T entity, int pageIndex, int pageSize) {
        String statementPostfix = entity.getClass().getName() + ".selectList";
        return this.selectQueryResult(statementPostfix, entity, pageIndex, pageSize);
    }

    @Override
    public <T extends BasePo> QueryResult<T> selectQueryResult(String statementPostfix, T entity, int pageIndex, int pageSize) {
        Boolean needTotalFlag = TotalrecordHelper.isNeadTotalRowCount();

        QueryResult result;
        try {
            TotalrecordHelper.setNeedTotalRowCount(true);
            List<T> resultList = this.getSqlSession().selectList(statementPostfix, entity, new RowBounds(pageIndex, pageSize));
            Long totalrecord = TotalrecordHelper.getTotalrecord();
            result = new QueryResult(resultList, totalrecord.longValue());
        } finally {
            TotalrecordHelper.setNeedTotalRowCount(needTotalFlag);
        }

        return result;
    }

    @Override
    public <T extends BasePo> QueryResult<T> selectQueryResult(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize) {
        Boolean needTotalFlag = TotalrecordHelper.isNeadTotalRowCount();

        QueryResult result;
        try {
            TotalrecordHelper.setNeedTotalRowCount(true);
            List<T> resultList = this.getSqlSession().selectList(statementPostfix, parameter, new RowBounds(pageIndex, pageSize));
            Long totalrecord = TotalrecordHelper.getTotalrecord();
            result = new QueryResult(resultList, totalrecord.longValue());
        } finally {
            TotalrecordHelper.setNeedTotalRowCount(needTotalFlag);
        }

        return result;
    }

    final private <T extends BasePo> T getPkObject(T param, boolean ignoreNullValue, Object... pkValue) {
        String[] pks = TablePkScanner.pkColsScanner(param);
        // 表无主键
        if (pks.length == 0) {
            return null;
        }
        // 主键值为Null
        if (null == pkValue || pkValue.length == 0) {
            throw BusiUtil.createBusinessException("100502");
        }
        // 有效value
        boolean effectiveValue = false;
        for (Object v : pkValue) {
            if (null != v) {
                effectiveValue = true;
                break;
            }
        }
        // 无有效value
        if (!effectiveValue) {
            throw BusiUtil.createBusinessException("100503");
        }
        int i = 0;
        for (String pk : pks) {
            if (null != pkValue[i]) {
                try {
                    BeanUtil.setValue(param, pk, pks[i]);
                } catch (Exception e) {
                    throw BusiUtil.createBusinessException("100504", new String[]{pk});
                }
            } else if (!ignoreNullValue) {
                throw BusiUtil.createBusinessException("100501", new String[]{pk});
            }
            i++;
        }
        return param;
    }
}
