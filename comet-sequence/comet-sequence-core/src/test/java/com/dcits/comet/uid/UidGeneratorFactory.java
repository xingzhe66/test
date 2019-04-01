package com.dcits.comet.uid;

import com.dcits.comet.uid.impl.DefaultUidGenerator;
import com.dcits.comet.uid.impl.LoadingUidGenerator;
import com.dcits.comet.uid.impl.RedisUidGenerator;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/30 21:59
 **/
@Slf4j
public class UidGeneratorFactory {

    private static UidGeneratorFactory inst = null;

    private UidGeneratorProxy uidGeneratorProxy;

    private static DisposableWorkerIdAssigner workerIdAssigner;

    public synchronized long getKey(String name) {
        return uidGeneratorProxy.getProxy().getUID(name);
    }

    public synchronized long getKey() {
        return getKey(null);
    }

    private UidGeneratorFactory() {
        DataSource dataSource = new HikariDataSource();
        ((HikariDataSource) dataSource).setJdbcUrl("jdbc:mysql://127.0.0.1:3306/workflow?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8");
        ((HikariDataSource) dataSource).setUsername("root");
        ((HikariDataSource) dataSource).setPassword("root");
        ((HikariDataSource) dataSource).setDriverClassName("com.mysql.jdbc.Driver");

        workerIdAssigner = new DisposableWorkerIdAssigner();
        workerIdAssigner.setDataSource(dataSource);

        UidGenerator uidGenerator = getUidGenerator(LoadingUidGenerator.class);
        this.uidGeneratorProxy = new UidGeneratorProxy(uidGenerator);

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
                workerIdAssigner.buildWorkerNode();
                return new DefaultUidGenerator(workerIdAssigner);
            case "RedisUidGenerator":
                workerIdAssigner.buildWorkerNode();
                return new RedisUidGenerator(workerIdAssigner);
            case "LoadingUidGenerator":
                return new LoadingUidGenerator(workerIdAssigner);
            default:
                workerIdAssigner.buildWorkerNode();
                return new DefaultUidGenerator(workerIdAssigner);
        }
    }


    public static UidGeneratorFactory getInstance() {
        if (inst == null) {
            synchronized (UidGeneratorFactory.class) {
                if (inst == null) {
                    inst = new UidGeneratorFactory();
                }
            }
        }
        return inst;
    }


}
