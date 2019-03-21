package com.dcits.comet.dao.model;


import java.io.Serializable;
import java.util.List;
/**
 * @author wangyun
 * @date 2019/3/21
 * @description QueryResult返回对象
 */
public class QueryResult<T> implements Serializable {
    private static final long serialVersionUID = 676226419039421871L;
    private List<T> resultlist;
    private long totalrecord;

    public QueryResult() {
    }

    public QueryResult(List<T> resultlist, long totalrecord) {
        this.resultlist = resultlist;
        this.totalrecord = totalrecord;
    }

    public List<T> getResultlist() {
        return this.resultlist;
    }

    public void setResultlist(List<T> resultlist) {
        this.resultlist = resultlist;
    }

    public long getTotalrecord() {
        return this.totalrecord;
    }

    public void setTotalrecord(long totalrecord) {
        this.totalrecord = totalrecord;
    }
}
