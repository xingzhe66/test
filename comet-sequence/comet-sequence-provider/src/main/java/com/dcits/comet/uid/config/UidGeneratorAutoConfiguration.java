package com.dcits.comet.uid.config;

import com.dcits.comet.uid.config.convert.UidGeneratorConfigurationConvert;
import com.dcits.comet.uid.impl.LoadingUidGenerator;
import com.dcits.comet.uid.impl.RedisUidGenerator;
import com.dcits.comet.uid.repository.WorkerNodePoRepository;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.sql.DataSource;

/**
 * @param
 * @author leijian
 * @Description //TODO
 * @date 2019/4/9 15:40
 * @return
 **/
@Configuration
@EnableConfigurationProperties({UidGeneratorProperties.class})
@Slf4j
public class UidGeneratorAutoConfiguration {

    @Bean
    @Primary
    public DataSource dataSource(final UidGeneratorProperties uidGeneratorProperties) {
        DataSource hikariDataSource = UidGeneratorConfigurationConvert.INSTANCE.to(uidGeneratorProperties);
        return hikariDataSource;
    }

    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner(@Autowired WorkerNodePoRepository workerNodePoRepository) {
        DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
        disposableWorkerIdAssigner.setWorkerNodePoRepository(workerNodePoRepository);
        return disposableWorkerIdAssigner;
    }

    /**
     * redisTemplate 序列化使用的jdkSerializeable, 存储二进制字节码, 所以自定义序列化类
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean(initMethod = "init")
    @ConditionalOnBean(RedisTemplate.class)
    public RedisUidGenerator redisUidGenerator(@Autowired RedisTemplate redisTemplate, @Autowired DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        RedisUidGenerator redisUidGenerator = new RedisUidGenerator();
        redisUidGenerator.setDisposableWorkerIdAssigner(disposableWorkerIdAssigner);
        redisUidGenerator.setRedisTemplate(redisTemplate);
        return redisUidGenerator;
    }

    @Bean(initMethod = "init")
    public LoadingUidGenerator loadingUidGenerator(@Autowired DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        LoadingUidGenerator loadingUidGenerator = new LoadingUidGenerator();
        loadingUidGenerator.setDisposableWorkerIdAssigner(disposableWorkerIdAssigner);
        return loadingUidGenerator;
    }

}
