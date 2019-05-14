package com.dcits.comet.parameter.api;

/**
 * @ClassName IParamSyn
 * @Author huangjjg
 * @Date 2019/5/8 13:56
 * @Description 参数同步功能接口
 * @Version 1.0
 **/
public interface IParamSyn<T> {
    /**
     * 检查资源是否可用
     * @return true 是 false 否
     */
    boolean checkResourceIsOk();

    /**
     * 参数同步方法
     * @throws Throwable
     */
    void execute() throws Throwable;
}
