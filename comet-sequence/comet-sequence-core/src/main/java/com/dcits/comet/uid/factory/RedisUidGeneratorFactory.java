package com.dcits.comet.uid.factory;

import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.UidGeneratorProxy;
import com.dcits.comet.uid.impl.RedisUidGenerator;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/3 13:00
 **/
public class RedisUidGeneratorFactory extends UidGeneratorFactory {

    private static RedisUidGeneratorFactory inst = null;

    private RedisUidGeneratorFactory() {
        UidGenerator uidGenerator = getUidGenerator(RedisUidGenerator.class);
        this.uidGeneratorProxy = new UidGeneratorProxy(uidGenerator);
    }


    public static RedisUidGeneratorFactory getInstance() {
        if (inst == null) {
            synchronized (RedisUidGeneratorFactory.class) {
                if (inst == null) {
                    inst = new RedisUidGeneratorFactory();
                }
            }
        }
        return inst;
    }


}

