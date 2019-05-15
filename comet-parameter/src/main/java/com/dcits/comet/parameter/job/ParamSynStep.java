package com.dcits.comet.parameter.job;

import com.dcits.comet.batch.AbstractTStep;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.parameter.config.ParamSynConfig;
import com.dcits.comet.parameter.exception.ParamSynException;
import com.dcits.comet.parameter.factory.ParamSynFactory;
import com.dcits.comet.parameter.api.IParamSyn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ParamSynStep
 * @Author huangjjg
 * @Date 2019/5/8 14:21
 * @Description 参数同步step
 * @Version 1.0
 **/
@Service("paramSynStep")
@Slf4j
public class ParamSynStep extends AbstractTStep {

    @Resource
    ParamSynConfig paramSynConfig;

    @Override
    public void exe(BatchContext batchContext) throws Throwable {
        IParamSyn paramSynService = ParamSynFactory.buildDefault(batchContext,paramSynConfig);
        if(paramSynService==null){
            throw new ParamSynException("参数同步组件未生成！");
        }
        if(paramSynService.checkResourceIsOk()){
            paramSynService.execute();
        }else{
            throw new ParamSynException("参数同步获取数据未准备！");
        }

    }
}
