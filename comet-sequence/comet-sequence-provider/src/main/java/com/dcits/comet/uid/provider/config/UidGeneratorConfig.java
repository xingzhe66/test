package com.dcits.comet.uid.provider.config;

import com.dcits.comet.uid.impl.DefaultUidGenerator;
import com.dcits.comet.uid.impl.LoadingUidGenerator;
import com.dcits.comet.uid.impl.RedisUidGenerator;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
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
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnBean(DisposableWorkerIdAssigner.class)
    public RedisTemplate<String, Object> redisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
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
