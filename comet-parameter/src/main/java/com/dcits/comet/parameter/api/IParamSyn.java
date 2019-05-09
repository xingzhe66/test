package com.dcits.comet.parameter.api;

/**
 * @ClassName IParamSyn
 * @Author huangjjg
 * @Date 2019/5/8 13:56
 * @Description 参数同步功能接口
 * @Version 1.0
 **/
public interface IParamSyn<T> {

    boolean checkResourceIsOk();

    void execute() throws Throwable;
}
