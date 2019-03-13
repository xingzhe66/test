package com.dcits.comet.commons.data.head;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 面向三方的结果集集合
 *
 * @author Chengliang
 */
public class Results implements Serializable {

    private static final long serialVersionUID = -2921211860791873384L;


    List<Result> rets = new ArrayList<Result>();

    public void setRet(List<Result> rets) {
        this.rets = rets;
    }

    public List<Result> getRet() {
        return rets;
    }

    public Results() {
    }

    public Results(String retCode, String retMsg) {
        this.addResult(new Result(retCode, retMsg));
    }

    public Results(Result ret) {
        this.addResult(ret);
    }

    public Results(List<Result> rets) {
        this.rets = rets;
    }

    public List<Result> getRets() {
        return rets;
    }

    public void addResult(Result ret) {
        this.rets.add(ret);
    }

    public boolean isEmpty() {
        if (this.rets.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void addResults(List<Result> rets) {
        this.rets.addAll(rets);
    }

    @Override
    public String toString() {
        return JSONArray.toJSONString(rets);
    }
}
