package com.dcits.comet.uid.provider.service;

import com.dcits.comet.commons.utils.SpringContextUtil;
import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.UidGeneratorProxy;
import com.dcits.comet.uid.impl.LoadingUidGenerator;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.List;

import static com.dcits.comet.uid.worker.WorkerIdAssigner.keys;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/2 9:53
 **/
@Slf4j
public class LoadingUidGeneratorFactory extends UidGeneratorFactory {
    private static LoadingUidGeneratorFactory inst = null;

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


    private LoadingUidGeneratorFactory() {
        //配置数据源
        DataSource dataSource = SpringContextUtil.getBean("dataSource");

        workerIdAssigner = SpringContextUtil.getBean("workerIdAssigner");

        workerIdAssigner.setDataSource(dataSource);

        UidGenerator uidGenerator = getUidGenerator(LoadingUidGenerator.class);
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
