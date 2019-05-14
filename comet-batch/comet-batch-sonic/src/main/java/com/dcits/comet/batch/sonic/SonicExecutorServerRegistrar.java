package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.sonic.config.SonicClientProfileConfig;
import com.dcits.comet.batch.sonic.config.SonicSimpleDBLockFactory;
import com.dcits.sonic.executor.SonicExecutorServer;
import com.dcits.sonic.executor.lock.db.SimpleDBLockFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 16:18
 **/
public class SonicExecutorServerRegistrar {

    @Bean(destroyMethod = "shutdown")
    public SonicExecutorServer sonicExecutorServer(SonicClientProfileConfig sonicClientProfileConfig, SonicSimpleDBLockFactory sonicSimpleDBLockFactory) {
        //获取实例
        SonicExecutorServer executorServer = SonicExecutorServer.getInstance();
        //初始化参数
        executorServer.setClientProfile(sonicClientProfileConfig);
        if (sonicSimpleDBLockFactory.isEnable()) {
            //设置分布式锁工程
            SimpleDBLockFactory lockFactory = new SimpleDBLockFactory();
            lockFactory.setDbType(sonicSimpleDBLockFactory.getDbType());
            lockFactory.setDriverName(sonicSimpleDBLockFactory.getDriverName());
            lockFactory.setUrl(sonicSimpleDBLockFactory.getUrl());
            lockFactory.setUser(sonicSimpleDBLockFactory.getUser());
            lockFactory.setPassword(sonicSimpleDBLockFactory.getPassword());
            executorServer.setLockFactory(lockFactory);
        }
        //启动
        executorServer.startup();
        return executorServer;
    }
}
