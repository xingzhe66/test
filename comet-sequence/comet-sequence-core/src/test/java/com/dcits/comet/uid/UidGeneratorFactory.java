package com.dcits.comet.uid;

import com.dcits.comet.uid.impl.DefaultUidGenerator;
import com.dcits.comet.uid.impl.LoadingUidGenerator;
import com.dcits.comet.uid.impl.RedisUidGenerator;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.sql.DataSource;
import java.util.List;

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

    private static RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();

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


    private UidGeneratorFactory() {
        //配置数据源
        DataSource dataSource = new HikariDataSource();
        ((HikariDataSource) dataSource).setJdbcUrl("jdbc:mysql://10.7.25.205:3306/workflow?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8");
        ((HikariDataSource) dataSource).setUsername("root");
        ((HikariDataSource) dataSource).setPassword("123456");
        ((HikariDataSource) dataSource).setDriverClassName("com.mysql.jdbc.Driver");

        workerIdAssigner = new DisposableWorkerIdAssigner();
        workerIdAssigner.setDataSource(dataSource);

        UidGenerator uidGenerator = getUidGenerator(RedisUidGenerator.class);
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
                workerIdAssigner.buildWorkerNode(name.toLowerCase());
                return new DefaultUidGenerator(workerIdAssigner);
            case "RedisUidGenerator":
                //配置redis信息
                JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
                jedisConnectionFactory.setHostName("127.0.0.1");
                jedisConnectionFactory.setPort(6379);
                Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
                ObjectMapper om = new ObjectMapper();
                om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
                jackson2JsonRedisSerializer.setObjectMapper(om);
                redisTemplate.setConnectionFactory(jedisConnectionFactory);
                redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
                redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
                redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
                redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
                redisTemplate.afterPropertiesSet();
                return new RedisUidGenerator(workerIdAssigner, redisTemplate);
            case "LoadingUidGenerator":
                return new LoadingUidGenerator(workerIdAssigner);
            default:
                workerIdAssigner.buildWorkerNode(name.toLowerCase());
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
