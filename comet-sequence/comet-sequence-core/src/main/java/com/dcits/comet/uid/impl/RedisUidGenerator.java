package com.dcits.comet.uid.impl;

import com.dcits.comet.uid.entity.WorkerNodePo;
import com.dcits.comet.uid.worker.WorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 13:17
 * @see RedisUidGenerator
 **/
@Slf4j
public class RedisUidGenerator extends DefaultUidGenerator {


    public RedisUidGenerator() {

    }

    public RedisUidGenerator(WorkerIdAssigner workerIdAssigner) {
        setWorkerIdAssigner(workerIdAssigner);
    }

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected long nextId(String bizTag) {
        String key = WorkerIdAssigner.keys.get(bizTag).getHostName();
        String hashKey = WorkerIdAssigner.keys.get(bizTag).getBizTag();
        String delta = WorkerIdAssigner.keys.get(bizTag).getStep();
        long uid = redisTemplate.opsForHash().increment(key, hashKey, Integer.valueOf(delta));
        return uid;
    }

    @Override
    public String parseUID(long uid) {
        //TODO
        return "redis";
    }

   /* @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        WorkerIdAssigner.keys.forEach((k, v) -> checkPoint(k, v));

    }*/

    void checkPoint(String string, final WorkerNodePo workerNodePo) {
        log.info("InitializingBean[{},机器号{},当前业务类型{},步长{}]", workerNodePo.getHostName(), workerNodePo.getId(), workerNodePo.getBizTag(), workerNodePo.getStep());
        redisTemplate.opsForHash().putIfAbsent(workerNodePo.getHostName(), workerNodePo.getBizTag(), Integer.valueOf(workerNodePo.getMinSeq()));
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
