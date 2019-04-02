package com.dcits.comet.uid.provider.service;

import com.dcits.comet.commons.utils.SpringContextUtil;
import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.UidGeneratorProxy;
import com.dcits.comet.uid.impl.DefaultUidGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/2 10:07
 **/
@Slf4j
public class SnowflakeUidGeneratorFactory extends UidGeneratorFactory {


    private static SnowflakeUidGeneratorFactory inst = null;

    private UidGeneratorProxy uidGeneratorProxy;


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

    private SnowflakeUidGeneratorFactory() {
        //配置数据源
        workerIdAssigner = SpringContextUtil.getBean("workerIdAssigner");
        UidGenerator uidGenerator = getUidGenerator(DefaultUidGenerator.class);
        this.uidGeneratorProxy = new UidGeneratorProxy(uidGenerator);
    }


    public static SnowflakeUidGeneratorFactory getInstance() {
        if (inst == null) {
            synchronized (SnowflakeUidGeneratorFactory.class) {
                if (inst == null) {
                    inst = new SnowflakeUidGeneratorFactory();
                }
            }
        }
        return inst;
    }


}
