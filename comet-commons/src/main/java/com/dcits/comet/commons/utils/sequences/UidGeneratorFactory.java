package com.dcits.comet.commons.utils.sequences;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/6/6 17:00
 **/
public class UidGeneratorFactory {
    private static UidGeneratorFactory inst = null;
    private Map<String, SnowflakeUidGenerator> keys = new ConcurrentHashMap<>();
    private long workerId;

    public UidGeneratorFactory(long workerId) {
        this.workerId = workerId;
    }

    public static UidGeneratorFactory getInstance(long workerId) {
        if (inst == null) {
            synchronized (UidGeneratorFactory.class) {
                if (inst == null) {
                    inst = new UidGeneratorFactory(workerId);
                }
            }
        }
        return inst;
    }

    public synchronized long getKey(String name) {
        SnowflakeUidGenerator key = keys.get(name);
        if (key == null) {
            key = new SnowflakeUidGenerator(workerId);
            keys.put(name, key);
        }
        return key.nextId();
    }

    public synchronized long getKey() {
        return getKey("default");
    }

    public static void main(String[] args) {
        UidGeneratorFactory.getInstance(3).getKey();
        for (int i = 0; i < 1000; i++) {
            long id = UidGeneratorFactory.getInstance(3).getKey();
            String binaryStr = Long.toBinaryString(id);
            System.out.println(binaryStr);
            System.out.println(id);
        }
    }
}

