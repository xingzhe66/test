package com.dcits.comet.uid.provider.service;

import com.dcits.comet.commons.utils.SpringContextUtil;
import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/2 10:21
 **/
public class UidGeneratorFactory {

    static DisposableWorkerIdAssigner workerIdAssigner;

    public static UidGenerator getUidGenerator(Class<?> clazz) {
        return getUidGenerator(clazz.getSimpleName());
    }

    public static UidGenerator getUidGenerator(String name) {
        return createUidGenerator(name);
    }

    public static UidGenerator createUidGenerator(String name) {
        switch (name) {
            case "DefaultUidGenerator":
                workerIdAssigner.buildWorkerNode(name.toLowerCase());
                return SpringContextUtil.getBean("defaultUidGenerator");
            case "RedisUidGenerator":
                return SpringContextUtil.getBean("redisUidGenerator");
            case "LoadingUidGenerator":
                return SpringContextUtil.getBean("loadingUidGenerator");
            default:
                workerIdAssigner.buildWorkerNode(name.toLowerCase());
                return SpringContextUtil.getBean("defaultUidGenerator");
        }
    }


}
