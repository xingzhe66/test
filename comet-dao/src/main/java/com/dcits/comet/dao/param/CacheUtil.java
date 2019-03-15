package com.dcits.comet.dao.param;

import com.dcits.comet.dao.annotation.TablePkScanner;
import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;
import com.dcits.comet.dao.model.BasePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-03-05 15:35
 * @Version 1.0
 **/
public class CacheUtil {
    static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    private static final String DELIMITER = "_";
    private static final String ALL = "ALL";


    public static boolean isNotParamTable(BasePo basePo) {

        boolean ret = false;

        boolean hasAnnotation = basePo.getClass().isAnnotationPresent(TableType.class);

        if (hasAnnotation) {
            TableType[] tableTypes = basePo.getClass().getAnnotationsByType(TableType.class);
            if (tableTypes != null) {
                for (TableType tableType : tableTypes) {
                    if (tableType.value().equals(TableTypeEnum.PARAM)) {
                        ret = true;
                    }
                }
            }
        }
        return !ret;
    }

    public static String getCacheKeyAll(BasePo basePo) {
        //className_ALL
        StringBuilder sb = new StringBuilder();
        sb.append(basePo.getClass().getSimpleName()).append(DELIMITER).append(ALL);
        return sb.toString();
    }

    public static String getCacheKey(BasePo basePo) {
        String[] pks = TablePkScanner.pkColsScanner(basePo);
        List<Object> pkValue = new ArrayList<>();

        for (int i = 0; i < pks.length; i++) {
            try {
                pkValue.add(getField(basePo, pks[i]));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //className_pk1_pk_2
        StringBuilder sb = new StringBuilder();
        sb.append(basePo.getClass().getSimpleName());

        if (null != pkValue) {
            for (int i = 0; i < pkValue.size(); i++) {
                sb.append(DELIMITER).append(pkValue.get(i));
            }
        }
        String key = sb.toString();
        if (logger.isDebugEnabled()) {
            logger.debug("缓存Key[{}]", key);
        }
        return key;
    }

    public static Object getField(Object filter, String field)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        String firstLetter = field.substring(0, 1).toUpperCase();
        String getMethodName = "get" + firstLetter + field.substring(1);
        Method getMethod = filter.getClass().getMethod(getMethodName);
        Object returnValue = getMethod.invoke(filter);
        return returnValue;
    }
}
