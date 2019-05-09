package com.dcits.comet.parameter.model;

/**
 * @ClassName ParamInSQL
 * @Author huangjjg
 * @Date 2019/5/8 17:13
 * @Description ParamInSQL
 * @Version 1.0
 **/
public class ParamInSQL extends ParamInModule {

    private String statement;

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    private String sqlType;

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSqlType() {
        return sqlType;
    }
}
