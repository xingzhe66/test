

package com.dcits.comet.dao.interceptor;


import com.dcits.comet.dao.model.BasePo;
import com.dcits.comet.dao.mybatis.DaoSupportImpl;
import com.dcits.comet.dao.model.Order;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description 抽象拦截器
 */
public abstract class AbstractInterceptor implements Interceptor {
    static final Logger LOGGER = LoggerFactory.getLogger(AbstractInterceptor.class);
    private static final String ROW_BOUNDS = "rowBounds";
    private static final String SQL = "sql";
    private static final String DELEGATE = "delegate";
    private static final int CONNECTION_INDEX = 0;

    public AbstractInterceptor() {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();
        PreparedStatementHandler preparedStatHandler = (PreparedStatementHandler)FieldUtils.readField(statementHandler, "delegate", true);
        RowBounds rowBounds = (RowBounds)FieldUtils.readField(preparedStatHandler, "rowBounds", true);
        Connection connection = (Connection)invocation.getArgs()[0];
        BoundSql boundSql = preparedStatHandler.getBoundSql();
        String originalSql = compress(boundSql.getSql());
        Object parameter = preparedStatHandler.getParameterHandler().getParameterObject();
        if (parameter instanceof BasePo) {
            List<Order> orders = ((BasePo)parameter).getOrderBy();
            if (orders != null) {
                StringBuilder orderSql = (new StringBuilder(originalSql)).append(" ORDER BY ");
                Map<String, String> columnMapping = (Map) DaoSupportImpl.getPropertyColumnMapper().get(parameter.getClass().getName() + ".BaseResultMap");

                Order order;
                String columnName;
                for(Iterator i$ = orders.iterator(); i$.hasNext(); orderSql.append(columnName).append(order.getSort()).append(",")) {
                    order = (Order)i$.next();
                    columnName = order.getColumnName();
                    if (columnMapping != null) {
                        String mappingColumn = (String)columnMapping.get(order.getPropertyName());
                        if (!StringUtils.isEmpty(mappingColumn)) {
                            columnName = mappingColumn;
                        }
                    }
                }

                originalSql = orderSql.deleteCharAt(orderSql.length() - 1).toString();
            }
        }

        if (rowBounds != null && rowBounds != RowBounds.DEFAULT) {
            String pagingSql;
            if (TotalrecordHelper.isNeadTotalRowCount()) {
                pagingSql = this.getCountSql(originalSql);
                TotalrecordHelper.setTotalrecord(pagingSql, preparedStatHandler, connection);
            }

            pagingSql = this.getPagingSql(originalSql, rowBounds.getOffset(), rowBounds.getLimit());
            FieldUtils.writeField(boundSql, "sql", pagingSql, true);
            FieldUtils.writeField(rowBounds, "offset", Integer.valueOf(0), true);
            FieldUtils.writeField(rowBounds, "limit", 2147483647, true);
            return invocation.proceed();
        } else {
            if (SelectForUpdateHelper.isSelectForUpdate()) {
                originalSql = originalSql + SelectForUpdateHelper.getUpdateSql();
            }

            FieldUtils.writeField(boundSql, "sql", originalSql, true);
            return invocation.proceed();
        }
    }

    private String getCountSql(String querySelect) {
        querySelect = compress(querySelect);
        String upperQuerySelect = querySelect.toUpperCase();
        int orderIndex = this.getLastOrderInsertPoint(upperQuerySelect);
        int formIndex = getAfterFormInsertPoint(upperQuerySelect);
        String select = upperQuerySelect.substring(0, formIndex);
        return select.indexOf("SELECT DISTINCT") == -1 && upperQuerySelect.indexOf("GROUP BY") == -1 ? (new StringBuffer(querySelect.length())).append("select count(1) count ").append(querySelect.substring(formIndex, orderIndex)).toString() : (new StringBuffer(querySelect.length())).append("select count(1) count from (").append(querySelect.substring(0, orderIndex)).append(" ) t").toString();
    }

    private int getLastOrderInsertPoint(String querySelect) {
        int orderIndex = querySelect.lastIndexOf("ORDER BY");
        if (orderIndex == -1) {
            orderIndex = querySelect.length();
        } else if (!isBracketCanPartnership(querySelect.substring(orderIndex, querySelect.length()))) {
            throw new RuntimeException("SQL 分页必须要有Order by 语句!");
        }

        return orderIndex;
    }

    protected abstract String getPagingSql(String querySelect, int pageIndex, int pageSize);

    private static String compress(String sql) {
        return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
    }

    private static int getAfterFormInsertPoint(String querySelect) {
        String regex = "\\s+FROM\\s+";
        Pattern pattern = Pattern.compile(regex, 2);
        Matcher matcher = pattern.matcher(querySelect);

        int fromStartIndex;
        String text;
        do {
            if (!matcher.find()) {
                return 0;
            }

            fromStartIndex = matcher.start(0);
            text = querySelect.substring(0, fromStartIndex);
        } while(!isBracketCanPartnership(text));

        return fromStartIndex;
    }

    private static boolean isBracketCanPartnership(String text) {
        return text != null && getIndexOfCount(text, '(') == getIndexOfCount(text, ')');
    }

    private static int getIndexOfCount(String text, char ch) {
        int count = 0;

        for(int i = 0; i < text.length(); ++i) {
            count = text.charAt(i) == ch ? count + 1 : count;
        }

        return count;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }
}
