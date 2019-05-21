//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dcits.comet.dao.interceptor;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * @author wangyun
 * @date 2019/3/21
 * @description 获取全部记录条数
 */
public class TotalrecordHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TotalrecordHelper.class);
    private static ThreadLocal<Long> totalRowCountHolder = new ThreadLocal();
    private static ThreadLocal<Boolean> isNeedTotalRowCountHolder = new ThreadLocal<Boolean>() {
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    public TotalrecordHelper() {
    }

    public static boolean isNeadTotalRowCount() {
        return ((Boolean)isNeedTotalRowCountHolder.get()).booleanValue();
    }

    public static void setNeedTotalRowCount(Boolean isNeedTotalRowCount) {
        isNeedTotalRowCountHolder.set(isNeedTotalRowCount);
    }

    static void setTotalrecord(String sql, StatementHandler statementHandler, Connection connection) throws Throwable {
        PreparedStatement countStmt = null;
        ResultSet rs = null;

        try {
            countStmt = connection.prepareStatement(sql);
            ParameterHandler parameterHandler = statementHandler.getParameterHandler();
            parameterHandler.setParameters(countStmt);
            rs = countStmt.executeQuery();
            long count = 0L;
            if (rs.next()) {
                count = rs.getLong(1);
            }

            LOGGER.debug("count result : [{}]", count);
            totalRowCountHolder.set(count);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } finally {
                if (countStmt != null) {
                    countStmt.close();
                }

            }

        }

    }

    public static Long getTotalrecord() {
        return (Long)totalRowCountHolder.get();
    }
}
