package com.dcits.comet.uid.provider.config;

import com.dcits.comet.uid.impl.DefaultUidGenerator;
import com.dcits.comet.uid.impl.LoadingUidGenerator;
import com.dcits.comet.uid.impl.RedisUidGenerator;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

    /**
     * Spring容器关闭时更新到数据库
     *
     * @param []
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
    @ConfigurationProperties(prefix = "spring.datasource.ds-0.hikari")
    public  DataSource dataSource(){
        return  new HikariDataSource();
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
    @ConditionalOnBean({RedisTemplate.class, DisposableWorkerIdAssigner.class})
    public RedisUidGenerator redisUidGenerator(@Qualifier("workerIdAssigner") DisposableWorkerIdAssigner workerIdAssigner, @Autowired RedisTemplate redisTemplate) {
        return new RedisUidGenerator(workerIdAssigner, redisTemplate);
    }

    @Bean
    @DependsOn("workerIdAssigner")
    @ConditionalOnBean({DisposableWorkerIdAssigner.class})
    public LoadingUidGenerator loadingUidGenerator(@Qualifier("workerIdAssigner") DisposableWorkerIdAssigner workerIdAssigner) {
        return new LoadingUidGenerator(workerIdAssigner);
    }

    @Bean
    @DependsOn("workerIdAssigner")
    @ConditionalOnBean({DisposableWorkerIdAssigner.class})
    public DefaultUidGenerator defaultUidGenerator(@Qualifier("workerIdAssigner") DisposableWorkerIdAssigner workerIdAssigner) {
        workerIdAssigner.buildWorkerNode(DefaultUidGenerator.class.getSimpleName().toLowerCase());
        return new DefaultUidGenerator(workerIdAssigner);
    }

}
