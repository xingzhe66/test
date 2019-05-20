package com.dcits.comet.uid.impl;

import com.dcits.comet.commons.exception.UidGenerateException;
import com.dcits.comet.commons.utils.NetUtils;
import com.dcits.comet.uid.context.UidGeneratorContext;
import com.dcits.comet.uid.entity.Segment;
import com.dcits.comet.uid.entity.WorkerNodePo;
import com.dcits.comet.uid.thread.NamedThreadFactory;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 13:17
 * @see RedisUidGenerator
 **/
@Slf4j
public class RedisUidGenerator extends DefaultUidGenerator {

    private volatile boolean isOk = false;

    private Map<String, Segment> manager = new ConcurrentHashMap();

    private volatile boolean initOK = false;

    private Map<String, WorkerNodePo> cache = new ConcurrentHashMap<String, WorkerNodePo>();

    private DisposableWorkerIdAssigner disposableWorkerIdAssigner;

    private RedisTemplate<String, Object> redisTemplate;

    private ExecutorService service = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new NamedThreadFactory("thread-segment-update-"));

    public void init() {
        log.info("Init ...{}", this.getClass().getSimpleName());
        // 确保加载到kv后才初始化成功
        updateCacheFromDb();
        updateToRedis();
        updateCacheToDbAtEveryMinute();
        initOK = true;
        log.info("Init End...{}", this.getClass().getSimpleName());
    }

    private void updateCacheToDbAtEveryMinute() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("check-idCache-thread", true));
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                updateCacheToDb();
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    private void updateToRedis() {
        cache.forEach((k, v) -> {
            String key = v.getHostName();
            String hashKey = v.getBizTag();
            long minSeq = v.getMinSeq();
            long currSeq = v.getCurrSeq();
            //仅仅当不存在时才会设置值
            redisTemplate.opsForHash().putIfAbsent(key, hashKey, minSeq < currSeq ? currSeq : minSeq);
        });
    }

    public void updateCacheFromDb() {
        log.info("update cache from db");
        StopWatch sw = new Slf4JStopWatch();
        try {
            List<WorkerNodePo> dbTags = disposableWorkerIdAssigner.findByTypeIfNullInsert(UidGeneratorContext.UID_REDIS_DEF);
            //db中新加的tags灌进caches
            dbTags.stream().forEach(tag -> {
                if (tag.getHostName().equalsIgnoreCase(NetUtils.getLocalAddress())) {
                    Segment segment = new Segment();
                    segment.setOk(true);
                    segment.setBizTag(tag.getBizTag());
                    segment.setStep(tag.getStep());
                    segment.setMax(tag.getMaxSeq());
                    redisTemplate.opsForHash().putIfAbsent(NetUtils.getLocalAddress(), tag.getBizTag(), tag.getMinSeq() < tag.getCurrSeq() ? tag.getCurrSeq() : tag.getMinSeq());
                    cache.put(tag.getBizTag(), tag);
                    manager.put(tag.getBizTag(), segment);
                    log.info("Add tag {} from db to cache, WorkerNodePo {}", tag.getBizTag(), tag);
                    log.info("Add tag {} from db to manager, Segment {}", tag.getBizTag(), segment.toString());
                }
            });
        } catch (Exception e) {
            log.warn("update cache from db exception", e);
        } finally {
            sw.stop("updateCacheFromDb");
        }
    }


    @Override
    protected long nextId(String bizTag) {
        StopWatch sw = null;
        if (log.isDebugEnabled()) {
            sw = new Slf4JStopWatch();
        }
        log.info("begin nextId({})", bizTag);
        if (!initOK) {
            throw new UidGenerateException("999999", "流水号生成异常");
        }
        if (StringUtils.isEmpty(bizTag)) {
            throw new UidGenerateException("999999", "顺序流水号bizTag不能为空");
        }
        if (cache.containsKey(bizTag) && manager.containsKey(bizTag)) {
            //Segment segment = manager.get(bizTag);
        } else {
            updateSegmentFromDb(bizTag);
        }
        long nextId = getIdFromRedis(manager.get(bizTag), bizTag, manager.get(bizTag).getStep());
        log.info("end nextId({})={}", bizTag, nextId);
        if (log.isDebugEnabled()) {
            sw.stop("nextId", bizTag + ":" + nextId);
        }
        return nextId;
    }

    public long getIdFromRedis(final Segment segment, final String bizTag, final Integer delta) {
        while (true) {
            try {
                segment.rLock().lock();
                if (!segment.isOk() && segment.getThreadRunning().compareAndSet(false, true)) {
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            boolean updateOk = false;
                            try {
                                updateSegmentFromDb(bizTag);
                                updateOk = true;
                                log.info("update segment {} from db", bizTag);
                            } catch (Exception e) {
                                log.warn(bizTag + " updateSegmentFromDb exception", e);
                            } finally {
                                if (updateOk) {
                                    segment.wLock().lock();
                                    segment.setOk(true);
                                    segment.getThreadRunning().set(false);
                                    segment.wLock().unlock();
                                } else {
                                    segment.getThreadRunning().set(false);
                                }
                            }
                        }
                    });
                }
                long value = redisTemplate.opsForHash().increment(NetUtils.getLocalAddress(), bizTag, delta);
                if (value <= cache.get(bizTag).getMaxSeq()) {
                    return value;
                }
                {
                    boolean seqCycle = "Y".equalsIgnoreCase(cache.get(segment.getBizTag()).getSeqCycle())&&cache.get(segment.getBizTag()).getSeqCache()!=0L;
                    if (seqCycle&&value>cache.get(segment.getBizTag()).getSeqCache()) {
                        redisTemplate.opsForHash().put(NetUtils.getLocalAddress(), bizTag,cache.get(bizTag).getMinSeq());
                        return redisTemplate.opsForHash().increment(NetUtils.getLocalAddress(), bizTag, delta);
                    }
                    isOk = false;
                }

            } finally {
                segment.rLock().unlock();
            }
            waitAndSleep(segment);
            try {
                segment.wLock().lock();
                redisTemplate.opsForHash().put(NetUtils.getLocalAddress(), bizTag, cache.get(bizTag).getMinSeq());
                long value = redisTemplate.opsForHash().increment(NetUtils.getLocalAddress(), bizTag, delta);
                if (value < Long.valueOf(cache.get(bizTag).getMaxSeq())) {
                    return value;
                }
            } finally {
                segment.wLock().unlock();
            }
        }
    }

    private void waitAndSleep(Segment segment) {
        int roll = 0;
        while (segment.getThreadRunning().get()) {
            roll += 1;
            if (roll > 10000) {
                try {
                    Thread.currentThread().sleep(10);
                    break;
                } catch (InterruptedException e) {
                    log.warn("Thread {} Interrupted", Thread.currentThread().getName());
                    break;
                }
            }
        }
    }

    public void updateSegmentFromDb(final String bizTag) {
        StopWatch sw = new Slf4JStopWatch();
        //先从DB for update获取锁，各个节点有可能同时更新值
        WorkerNodePo workerNodePo = disposableWorkerIdAssigner.selectByBizTagAndTypeTodoForUpdateOrInsert(UidGeneratorContext.UID_REDIS_DEF, bizTag);
        cache.put(bizTag, workerNodePo);
        Segment newSegment = new Segment();
        newSegment.setOk(true);
        newSegment.setMax(workerNodePo.getMaxSeq());
        newSegment.setStep(workerNodePo.getStep());
        newSegment.setBizTag(bizTag);
        newSegment.setUpdateTimestamp(System.currentTimeMillis());
        manager.put(bizTag, newSegment);
        redisTemplate.opsForHash().put(NetUtils.getLocalAddress(), bizTag, cache.get(bizTag).getMinSeq());
        sw.stop("updateSegmentFromDb", bizTag + " " + workerNodePo);
    }

    @Override
    public String parseUID(long uid) {
        return "redis";
    }

    @PreDestroy
    public void destroy() {
        log.info("程序终止更新到数据库");
        initOK = false;
        updateCacheToDb();
    }

    public void updateCacheToDb() {
        for (Map.Entry<String, WorkerNodePo> vo : cache.entrySet()) {
            WorkerNodePo workerNodePo = vo.getValue();
            Object object = redisTemplate.opsForHash().get(workerNodePo.getHostName(), workerNodePo.getBizTag());
            long updateUid = Long.valueOf("" + object);
            if (Long.compare(workerNodePo.getCurrSeq(), updateUid) != 0) {
                workerNodePo.setCurrSeq(updateUid);
                workerNodePo.setModified(LocalDateTime.now());
                disposableWorkerIdAssigner.update(workerNodePo);
            }
        }
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setDisposableWorkerIdAssigner(DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        this.disposableWorkerIdAssigner = disposableWorkerIdAssigner;
    }
}
