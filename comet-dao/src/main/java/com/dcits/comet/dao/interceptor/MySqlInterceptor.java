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
 * @description MySql分页
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
public class MySqlInterceptor extends AbstractInterceptor {
    public MySqlInterceptor() {
    }

    @Override
    protected String getPagingSql(String querySelect, int pageIndex, int pageSize) {
        StringBuffer pageSql = new StringBuffer();
        pageSql.append(querySelect);
        pageSql.append(" limit ");
        pageSql.append((pageIndex - 1) * pageSize);
        pageSql.append(",").append(pageSize);
        return pageSql.toString();
    }
}
