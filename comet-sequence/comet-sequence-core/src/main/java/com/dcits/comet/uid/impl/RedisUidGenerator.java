package com.dcits.comet.uid.impl;

import com.dcits.comet.commons.exception.UidGenerateException;
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

    private String className = getClass().getSimpleName().toLowerCase();

    public RedisUidGenerator() {

    }

    public RedisUidGenerator(WorkerIdAssigner workerIdAssigner, RedisTemplate<String, Object> redisTemplate) {
        setWorkerIdAssigner(workerIdAssigner);
        setRedisTemplate(redisTemplate);
    }

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected long nextId(String bizTag) {
        String seqName = null == bizTag ? WorkerIdAssigner.DEF : bizTag;
        String key = "";
        String hashKey = "";
        String delta = "";
        if (!WorkerIdAssigner.keys.containsKey(seqName)) {
            //获取TYPE是redis实现的节点信息
            workerIdAssigner.assignWorkerId(seqName, className);
            if (!className.equals(WorkerIdAssigner.keys.get(bizTag).getType())) {
                throw new UidGenerateException("999999", "流水类型[" + bizTag + "]配置的实现type是[" + WorkerIdAssigner.keys.get(bizTag).getType() + "]");
            }
            key = WorkerIdAssigner.keys.get(bizTag).getHostName();
            hashKey = WorkerIdAssigner.keys.get(bizTag).getBizTag();
            Integer minSeq = Integer.parseInt(WorkerIdAssigner.keys.get(bizTag).getMinSeq());
            Integer currSeq = Integer.parseInt(WorkerIdAssigner.keys.get(bizTag).getCurrSeq());
            redisTemplate.opsForHash().putIfAbsent(key, hashKey, minSeq < currSeq ? currSeq : minSeq);
        }

        key = WorkerIdAssigner.keys.get(bizTag).getHostName();
        hashKey = WorkerIdAssigner.keys.get(bizTag).getBizTag();
        delta = WorkerIdAssigner.keys.get(bizTag).getStep();
        long uid = redisTemplate.opsForHash().increment(key, hashKey, Integer.valueOf(delta));
        return uid;
    }

    @Override
    public String parseUID(long uid) {
        //TODO
        return "redis";
    }

    @Override
    public void keepWithDB() {
        WorkerIdAssigner.keys.forEach((k, v) -> updateDB(k, v));
    }

    void updateDB(String bizTag, WorkerNodePo workerNodePo) {
        log.info("{}序列节点同步到数据库", this.className);
        long updateUid = Long.valueOf("" + redisTemplate.opsForHash().get(workerNodePo.getHostName(), workerNodePo.getBizTag()));
        workerIdAssigner.doUpdateNextSegment(bizTag, updateUid, className);
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //启动定时同步任务
    static {
        //scheduledExecutorService.scheduleWithFixedDelay(this::connect, RECONNECT_PERIOD_DEFAULT, RECONNECT_PERIOD_DEFAULT, TimeUnit.MILLISECONDS);
    }
}
