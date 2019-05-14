package com.dcits.comet.parameter.factory;

import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.commons.utils.BusiUtil;
import com.dcits.comet.parameter.api.IParamSyn;
import com.dcits.comet.parameter.config.ParamSynConfig;
import com.dcits.comet.parameter.dao.ParamSynDao;
import com.dcits.comet.parameter.service.DefaultParamSynService;
import com.mysql.fabric.xmlrpc.base.Param;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @ClassName ParamSynFactory
 * @Author huangjjg
 * @Date 2019/5/8 14:47
 * @Description 参数同步服务工厂
 * @Version 1.0
 **/
@Slf4j
public class ParamSynFactory {

    public static <T extends IParamSyn> T build(BatchContext batchContext,ParamSynConfig config) {
        if(config!=null&&
                config.getServiceClassName()!=null&&
                "".equals(config.getServiceClassName())){
            String className = config.getServiceClassName();
            Object object = null;
            try {
                Class c = Class.forName(className);
                object = c.newInstance();
            } catch (Exception e) {
                log.error("创建参数同步服务组件失败",e);
                return null;
            }
            return (T)object;
        }else{
            return (T)buildDefault(batchContext,config);
        }
    }

    public static IParamSyn buildDefault(BatchContext batchContext,ParamSynConfig config) {
        DefaultParamSynService defaultParamSynService = new DefaultParamSynService();
        defaultParamSynService.setConfig(config);
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        ParamSynDao paramSynDao = (ParamSynDao)context.getBean("paramSynDao");
        defaultParamSynService.setParamSynDao(paramSynDao);
        String synType = (String)batchContext.getParams().get("synType");
        if(BusiUtil.isNotNull(synType)) {
            defaultParamSynService.setDefaultSynType(synType);
        }
        return defaultParamSynService;
    }

}
