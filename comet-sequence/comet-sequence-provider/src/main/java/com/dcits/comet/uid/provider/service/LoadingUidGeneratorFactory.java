package com.dcits.comet.uid.provider.service;

import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.UidGeneratorProxy;
import com.dcits.comet.uid.impl.LoadingUidGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/2 9:53
 **/
@Slf4j
public class LoadingUidGeneratorFactory extends UidGeneratorFactory {
    private static LoadingUidGeneratorFactory inst = null;

    private UidGeneratorProxy uidGeneratorProxy;

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


    private LoadingUidGeneratorFactory() {
        UidGenerator uidGenerator = getUidGenerator(LoadingUidGenerator.class);
        this.uidGeneratorProxy = new UidGeneratorProxy(uidGenerator);
    }


    public static LoadingUidGeneratorFactory getInstance() {
        if (inst == null) {
            synchronized (LoadingUidGeneratorFactory.class) {
                if (inst == null) {
                    inst = new LoadingUidGeneratorFactory();
                }
            }
        }
        return inst;
    }


}
