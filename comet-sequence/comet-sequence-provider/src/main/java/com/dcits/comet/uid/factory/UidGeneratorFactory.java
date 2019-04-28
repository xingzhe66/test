package com.dcits.comet.uid.factory;

import com.dcits.comet.commons.utils.SpringContextUtil;
import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.proxy.UidGeneratorProxy;

import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/2 10:21
 **/
@Deprecated
public class UidGeneratorFactory {

    UidGeneratorProxy uidGeneratorProxy;

    /**
     * @param name
     * @return long
     * @author leijian
     * @date 2019/4/2 16:49
     **/
    public synchronized long getKey(String name) {
        return uidGeneratorProxy.getProxy().getUID(name);
    }

    public synchronized long getKey() {
        return getKey(null);
    }

    public synchronized List<Long> getKeyList(String bizTag, long size) {
        return uidGeneratorProxy.getProxy().getUIDList(bizTag, size);
    }

    public synchronized List<Long> getKeyList(long size) {
        return getKeyList(null, size);
    }


    public static UidGenerator getUidGenerator(Class<?> clazz) {
        return getUidGenerator(clazz.getSimpleName());
    }

    public static UidGenerator getUidGenerator(String name) {
        return createUidGenerator(name);
    }

    public static UidGenerator createUidGenerator(String name) {
        switch (name) {
            case "DefaultUidGenerator":
                return SpringContextUtil.getBean("defaultUidGenerator");
            case "RedisUidGenerator":
                return SpringContextUtil.getBean("redisUidGenerator");
            case "LoadingUidGenerator":
                return SpringContextUtil.getBean("loadingUidGenerator");
            default:
                return SpringContextUtil.getBean("defaultUidGenerator");
        }
    }

}
