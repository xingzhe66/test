package com.dcits.comet.uid.impl;

import com.dcits.comet.uid.entity.WorkerNodePo;
import com.dcits.comet.uid.thread.NamedThreadFactory;
import com.dcits.comet.uid.worker.WorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    public RedisUidGenerator(WorkerIdAssigner workerIdAssigner, RedisTemplate<String, Object> redisTemplate) {
        setWorkerIdAssigner(workerIdAssigner);
        setRedisTemplate(redisTemplate);
    }

    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 同步时间(单位毫秒)
     */
    private static final int DELAY = 3 * 1000;

    /**
     * 定时任务延迟指定时间执行
     */
    private static final int INITIALDELAY = 3 * 1000;
    /**
     * 定时任务执行器
     */
    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("keep-redisuidgenerator-uid-sync", true));

    ScheduledFuture scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
        keepWithDB();
    }, INITIALDELAY, DELAY, TimeUnit.MILLISECONDS);

    @Override
    protected long nextId(String bizTag) {
        String seqName = null == bizTag ? WorkerIdAssigner.DEF : bizTag;
        String key = "";
        String hashKey = "";
        String delta = "";
        if (!WorkerIdAssigner.keys.containsKey(seqName)) {
            //获取TYPE是redis实现的节点信息
            workerIdAssigner.assignWorkerId(seqName, this.getClass().getSimpleName().toLowerCase());
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
        log.info("序列节点同步到数据库");
        long updateUid = Long.valueOf("" + redisTemplate.opsForHash().get(workerNodePo.getHostName(), workerNodePo.getBizTag()));
        workerIdAssigner.doUpdateNextSegment(bizTag, updateUid, this.getClass().getSimpleName().toLowerCase());
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //启动定时同步任务
    static {
        //scheduledExecutorService.scheduleWithFixedDelay(this::connect, RECONNECT_PERIOD_DEFAULT, RECONNECT_PERIOD_DEFAULT, TimeUnit.MILLISECONDS);
    }
}
