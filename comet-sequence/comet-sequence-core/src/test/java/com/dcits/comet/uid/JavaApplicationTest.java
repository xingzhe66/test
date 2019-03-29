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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.sql.DataSource;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 10:26
 * @see JavaApplicationTest
 **/
//@SpringBootApplication
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JavaApplicationTest {

    DataSource dataSource;

    @BeforeAll
    void DataSource() {
        DataSource dataSource = new HikariDataSource();
        ((HikariDataSource) dataSource).setJdbcUrl("jdbc:mysql://10.7.25.205:3306/ensemble16-cif1?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8");
        ((HikariDataSource) dataSource).setUsername("root");
        ((HikariDataSource) dataSource).setPassword("123456");
        ((HikariDataSource) dataSource).setDriverClassName("com.mysql.jdbc.Driver");
        this.dataSource = dataSource;
    }

    @Test
    void LoadingUidGenerator() {
        DisposableWorkerIdAssigner workerIdAssigner = new DisposableWorkerIdAssigner();
        workerIdAssigner.setDataSource(dataSource);
        //workerIdAssigner.buildWorkerNode();
        //实现类
       LoadingUidGenerator loadingUidGenerator = new LoadingUidGenerator();
       loadingUidGenerator.setWorkerIdAssigner(workerIdAssigner);

       log.info("{}",loadingUidGenerator.getUID());
    }


    @Test
    void RedisUidGeneratorTest() throws InterruptedException {
        //设置数据源
        DisposableWorkerIdAssigner workerIdAssigner = new DisposableWorkerIdAssigner();
        workerIdAssigner.setDataSource(dataSource);
        workerIdAssigner.buildWorkerNode();
        RedisUidGenerator redisUidGenerator = new RedisUidGenerator();
        redisUidGenerator.setWorkerIdAssigner(workerIdAssigner);
        //配置redis信息
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("127.0.0.1");
        jedisConnectionFactory.setPort(6379);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
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
        redisUidGenerator.setRedisTemplate(redisTemplate);
        //获取节点序列号
        long uid = redisUidGenerator.getUID();
        log.info("{},原始信息{}", uid, redisUidGenerator.parseUID(uid));
        //设置节点信息
        //3.获取节点号
    }

    @Test
    void DefaultUidGeneratorTest() {
        //设置数据源
        DisposableWorkerIdAssigner workerIdAssigner = new DisposableWorkerIdAssigner();
        workerIdAssigner.setDataSource(dataSource);
        workerIdAssigner.buildWorkerNode();
        DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
        defaultUidGenerator.setWorkerIdAssigner(workerIdAssigner);
        long uid = defaultUidGenerator.getUID();
        log.info("{},原始信息{}", uid, defaultUidGenerator.parseUID(uid));
        //设置节点信息
        //3.获取节点号
    }


}
