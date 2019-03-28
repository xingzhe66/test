package com.dcits.comet.uid;

import com.dcits.comet.uid.provider.CachedUidGenerator;
import com.dcits.comet.uid.provider.DefaultUidGenerator;
import com.dcits.comet.uid.provider.RedisUidGenerator;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import javax.sql.DataSource;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 10:26
 * @see Application
 **/
//@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    /**
     * @return com.dcits.comet.uid.worker.DisposableWorkerIdAssigner
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 22:19
     * @Param []
     **/
    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner(@Autowired DataSource dataSource) {
        DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
        disposableWorkerIdAssigner.setDataSource(dataSource);
        return disposableWorkerIdAssigner;
    }

    /**
     * @return com.dcits.comet.uid.provider.DefaultUidGenerator
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 22:19
     * @Param [disposableWorkerIdAssigner]
     **/
    @Bean
    @Lazy(false)
    public DefaultUidGenerator defaultUidGenerator(@Autowired DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
        defaultUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        defaultUidGenerator.setTimeBits(29);
        defaultUidGenerator.setWorkerBits(21);
        defaultUidGenerator.setSeqBits(13);
        defaultUidGenerator.setEpochStr("2016-09-20");
        return defaultUidGenerator;
    }

    /**
     * @return com.dcits.comet.uid.provider.CachedUidGenerator
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 22:19
     * @Param [disposableWorkerIdAssigner]
     **/
    @Bean
    public CachedUidGenerator cachedUidGenerator(@Autowired DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        return cachedUidGenerator;
    }

    /**
     * @return com.dcits.comet.uid.provider.RedisUidGenerator
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 22:19
     * @Param [disposableWorkerIdAssigner]
     **/
    @Bean
    public RedisUidGenerator redisUidGenerator(@Autowired DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        RedisUidGenerator redisUidGenerator = new RedisUidGenerator();
        redisUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        return redisUidGenerator;
    }


}
