//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dcits.comet.dao.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description DB2分页
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
public class DB2Interceptor extends AbstractInterceptor {
    public DB2Interceptor() {
    }

    @Override
    protected String getPagingSql(String querySelect, int pageIndex, int pageSize) {
        StringBuffer pageSql = new StringBuffer();
        pageSql.append("select * from (select tmp_tb.*,ROW_NUMBER() OVER() AS ROWNUM from (");
        pageSql.append(querySelect);
        pageSql.append(") tmp_tb ");
        pageSql.append(") where ROWNUM > ");
        pageSql.append((pageIndex - 1) * pageSize);
        pageSql.append(" AND ROWNUM <= ");
        pageSql.append(pageIndex * pageSize);
        return pageSql.toString();
    }
}
