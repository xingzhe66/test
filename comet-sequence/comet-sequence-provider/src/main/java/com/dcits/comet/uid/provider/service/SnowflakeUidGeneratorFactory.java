package com.dcits.comet.uid.provider.service;

import com.dcits.comet.commons.utils.SpringContextUtil;
import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.UidGeneratorProxy;
import com.dcits.comet.uid.impl.DefaultUidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static com.dcits.comet.uid.worker.WorkerIdAssigner.keys;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/2 10:07
 **/
@Slf4j
public class SnowflakeUidGeneratorFactory extends UidGeneratorFactory {


    private static SnowflakeUidGeneratorFactory inst = null;

    private UidGeneratorProxy uidGeneratorProxy;

    private static RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();

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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("程序关闭的时候同步更新到数据库");
            log.info("{}", keys);
            try {
                uidGenerator.keepWithDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
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
