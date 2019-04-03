package com.dcits.comet.uid.factory;

import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.UidGeneratorProxy;
import com.dcits.comet.uid.impl.DefaultUidGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/2 10:07
 **/
@Slf4j
public class SnowflakeUidGeneratorFactory extends UidGeneratorFactory {

    private static SnowflakeUidGeneratorFactory inst = null;


    private SnowflakeUidGeneratorFactory() {
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
