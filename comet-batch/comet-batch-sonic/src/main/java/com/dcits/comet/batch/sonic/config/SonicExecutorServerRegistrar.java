package com.dcits.comet.batch.sonic.config;

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
    public SonicExecutorServer sonicExecutorServer(SonicRemoteProfileConfig sonicRemoteProfileConfig, SonicClientProfileConfig sonicClientProfileConfig, SonicSimpleDbLockFactory sonicSimpleDbLockFactory) {
        //获取实例
        SonicExecutorServer executorServer = SonicExecutorServer.getInstance();
        //初始化参数
        executorServer.setClientProfile(sonicClientProfileConfig);
        executorServer.setRemoteProfile(sonicRemoteProfileConfig);
        if (sonicSimpleDbLockFactory.isEnable()) {
            //设置分布式锁工程
            SimpleDBLockFactory lockFactory = new SimpleDBLockFactory();
            lockFactory.setDbType(sonicSimpleDbLockFactory.getDbType());
            lockFactory.setDriverName(sonicSimpleDbLockFactory.getDriverName());
            lockFactory.setUrl(sonicSimpleDbLockFactory.getUrl());
            lockFactory.setUser(sonicSimpleDbLockFactory.getUser());
            lockFactory.setPassword(sonicSimpleDbLockFactory.getPassword());
            executorServer.setLockFactory(lockFactory);
        }
        //启动
        executorServer.startup();
        return executorServer;
    }
}
