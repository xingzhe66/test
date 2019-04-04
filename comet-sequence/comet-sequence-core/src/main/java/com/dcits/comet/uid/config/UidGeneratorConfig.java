package com.dcits.comet.uid.config;

import com.dcits.comet.uid.impl.DefaultUidGenerator;
import com.dcits.comet.uid.impl.LoadingUidGenerator;
import com.dcits.comet.uid.impl.RedisUidGenerator;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/2 15:52
 **/
@Slf4j
@Component
public class UidGeneratorConfig {

    @Value("${ds.uid.datasource.connection-timeout}")
    volatile long connectionTimeout;
    @Value("${ds.uid.datasource.idle-timeout}")
    volatile long idleTimeout;
    @Value("${ds.uid.datasource.max-lifetime}")
    volatile long maxLifetime;
    @Value("${ds.uid.datasource.maximum-pool-size}")
    volatile int maxPoolSize;
    @Value("${ds.uid.datasource.minimum-idle}")
    volatile int minIdle;
    @Value("${ds.uid.datasource.username}")
    volatile String username;
    @Value("${ds.uid.datasource.password}")
    volatile String password;
    @Value("${ds.uid.datasource.connection-test-query}")
    String connectionTestQuery;
    @Value("${ds.uid.datasource.driver-class-name}")
    String driverClassName;
    @Value("${ds.uid.datasource.jdbc-url}")
    String jdbcUrl;
    @Value("${ds.uid.datasource.pool-name}")
    String poolName;
    @Value("${ds.uid.datasource.auto-commit:true}")
    boolean isAutoCommit;
    /**
     * Spring容器关闭时更新到数据库
     *
     * @param
     * @return void
     * @author leijian
     * @Description //TODO
     * @date 2019/4/2 16:53
     **/
    @PreDestroy
    public void keepWithDB() {
        log.info("Spring容器关闭时更新到数据库");
    }

    @Bean("ds_uid")
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(jdbcUrl);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setDriverClassName(driverClassName);
        hikariDataSource.setPoolName(poolName);
        hikariDataSource.setMinimumIdle(minIdle);
        hikariDataSource.setMaximumPoolSize(maxPoolSize);
        hikariDataSource.setAutoCommit(isAutoCommit);
        hikariDataSource.setIdleTimeout(idleTimeout);
        hikariDataSource.setMaxLifetime(maxLifetime);
        hikariDataSource.setConnectionTimeout(connectionTimeout);
        hikariDataSource.setConnectionTestQuery(connectionTestQuery);
        return hikariDataSource;
    }

    @Bean("workerIdAssigner")
    @DependsOn("ds_uid")
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner(@Qualifier("ds_uid") DataSource dataSource) {
        DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
        disposableWorkerIdAssigner.setDataSource(dataSource);
        return disposableWorkerIdAssigner;
    }


    @Bean
    @DependsOn("workerIdAssigner")
    public RedisUidGenerator redisUidGenerator(@Qualifier("workerIdAssigner") DisposableWorkerIdAssigner workerIdAssigner, @Autowired RedisTemplate redisTemplate) {
        return new RedisUidGenerator(workerIdAssigner, redisTemplate);
    }

    @Bean
    @DependsOn("workerIdAssigner")
    public LoadingUidGenerator loadingUidGenerator(@Qualifier("workerIdAssigner") DisposableWorkerIdAssigner workerIdAssigner) {
        return new LoadingUidGenerator(workerIdAssigner);
    }

    @Bean
    @DependsOn("workerIdAssigner")
    public DefaultUidGenerator defaultUidGenerator(@Qualifier("workerIdAssigner") DisposableWorkerIdAssigner workerIdAssigner) {
        return new DefaultUidGenerator(workerIdAssigner);
    }

}
