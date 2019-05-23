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
 * @description Oracle分页
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
public class OracleInterceptor extends AbstractInterceptor {
    public OracleInterceptor() {
    }

    @Override
    protected String getPagingSql(String querySelect, int pageIndex, int pageSize) {
        StringBuffer pageSql = new StringBuffer();
        int i = querySelect.indexOf("*/ ");
        if (i >= 0) {
            pageSql.append(querySelect.substring(0, i + 3));
            querySelect = querySelect.substring(i + 3, querySelect.length());
        }

        pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
        pageSql.append(querySelect);
        pageSql.append(") tmp_tb where ROWNUM<=");
        pageSql.append(pageIndex * pageSize);
        pageSql.append(") where row_id>");
        pageSql.append((pageIndex - 1) * pageSize);
        return pageSql.toString();
    }
}
