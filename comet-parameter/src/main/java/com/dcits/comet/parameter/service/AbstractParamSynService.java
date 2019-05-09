package com.dcits.comet.parameter.service;

import com.dcits.comet.parameter.api.IParamSyn;
import com.dcits.comet.parameter.config.ParamSynConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName AbstractParamSynService
 * @Author huangjjg
 * @Date 2019/5/8 16:26
 * @Description AbstractParamSynService
 * @Version 1.0
 **/
@Slf4j
public abstract class AbstractParamSynService<T> implements IParamSyn<T> {

    protected ParamSynConfig config;

    @Override
    public void execute() throws Throwable {
        if(log.isDebugEnabled()){
            log.debug("Begin execute synchronized parameters");
        }
        List<T> list = readParams();
        if(list!=null&&list.size()>0) {
            for (T t : list) {
                try {
                    doSynParam(t);
                } catch (Throwable throwable) {
                    if (log.isErrorEnabled()) {
                        log.error("参数同步失败，查看日志", throwable);
                        logError(t,throwable);
                        throw throwable;
                    }
                } finally {
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("End execute synchronized parameters");
            }
        }
    }

    private void logError(T t,Throwable throwable) {
    }

    /**
     * 参数同步
     * @param t
     * @throws Throwable
     */
    protected abstract void doSynParam(T t) throws Throwable;

    /**
     * 读取参数源
     * @return List<T>
     */
    protected abstract List<T> readParams()throws Throwable;


    public ParamSynConfig getConfig() {
        return config;
    }

    public void setConfig(ParamSynConfig config) {
        this.config = config;
    }
}
