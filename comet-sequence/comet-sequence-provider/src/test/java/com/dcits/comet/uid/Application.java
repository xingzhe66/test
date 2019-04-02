package com.dcits.comet.uid;

import com.dcits.comet.uid.impl.DefaultUidGenerator;
import com.dcits.comet.uid.impl.LoadingUidGenerator;
import com.dcits.comet.uid.impl.RedisUidGenerator;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.sql.DataSource;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/1 17:26
 **/
@SpringBootApplication(scanBasePackages = {"com.dcits.comet"})
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean("workerIdAssigner")
    @DependsOn("dataSource")
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner(@Autowired DataSource dataSource) {
        DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
        disposableWorkerIdAssigner.setDataSource(dataSource);
        return disposableWorkerIdAssigner;
    }

    @Bean
    @DependsOn("workerIdAssigner")
    @ConditionalOnClass(RedisTemplate.class)
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
    @ConditionalOnBean(RedisTemplate.class)
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
        workerIdAssigner.buildWorkerNode(DefaultUidGenerator.class.getSimpleName().toLowerCase());
        return new DefaultUidGenerator(workerIdAssigner);
    }


}
