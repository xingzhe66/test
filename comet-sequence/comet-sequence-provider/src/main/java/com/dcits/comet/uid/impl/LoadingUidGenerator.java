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
import java.util.concurrent.atomic.LongAdder;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/29 14:39
 **/
@Slf4j
public class LoadingUidGenerator extends DefaultUidGenerator {

    private Map<String, WorkerNodePo> cache = new ConcurrentHashMap<String, WorkerNodePo>();

    private Map<String, Segment> manager = new ConcurrentHashMap();

    private ExecutorService service = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new NamedThreadFactory("thread-segment-update-"));

    private volatile boolean initOK = false;

    private DisposableWorkerIdAssigner disposableWorkerIdAssigner;

    //初始化操作
    public void init() {
        log.info("Init ...{}", this.getClass().getSimpleName());
        // 确保加载到kv后才初始化成功
        updateCacheFromDb();
        initOK = true;
        updateCacheToDbAtEveryMinute();
        log.info("Init End...{}", this.getClass().getSimpleName());
    }

    //将当前序列更新到数据库
    private void updateCacheToDbAtEveryMinute() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("check-idCache-thread", true));
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                updateCacheToDb();
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy() {
        log.info("程序终止更新到数据库");
        initOK = false;
        updateCacheToDb();
    }

    public void updateCacheToDb() {
        manager.forEach((k, v) -> {
            WorkerNodePo workerNodePo = cache.get(k);
            if (Long.compare(workerNodePo.getCurrSeq(), v.getValue().longValue()) != 0) {
                workerNodePo.setCurrSeq(v.getValue().longValue());
                workerNodePo.setModified(LocalDateTime.now());
                workerNodePo = disposableWorkerIdAssigner.update(workerNodePo);
                cache.put(k, workerNodePo);
            }
        });
    }

    //从数据库获取cache
    public void updateCacheFromDb() {
        log.info("update cache from db");
        StopWatch sw = new Slf4JStopWatch();
        try {
            List<WorkerNodePo> dbTags = disposableWorkerIdAssigner.findByTypeIfNullInsert(UidGeneratorContext.UID_LOAD_DEF);
            //db中新加的tags灌进caches
            dbTags.stream().forEach(tag -> {
                if (tag.getHostName().equalsIgnoreCase(NetUtils.getLocalAddress())) {
                    cache.put(tag.getBizTag(), tag);
                    Segment segment = new Segment();
                    LongAdder longAdder = new LongAdder();
                    longAdder.add(tag.getCurrSeq());
                    segment.setOk(true);
                    segment.setBizTag(tag.getBizTag());
                    segment.setValue(longAdder);
                    segment.setMax(tag.getMaxSeq());
                    segment.setStep(tag.getStep());
                    segment.setUpdateTimestamp(System.currentTimeMillis());
                    manager.put(tag.getBizTag(), segment);
                    log.info("Add tag {} from db to cache, WorkerNodePo {}", tag.getBizTag(), tag);
                    log.info("Add tag {} from db to manager, segment {}", tag.getBizTag(), segment);
                }
            });
        } catch (Exception e) {
            log.warn("update cache from db exception", e);
        } finally {
            sw.stop("updateCacheFromDb");
        }
    }

    @Override
    protected synchronized long nextId(final String bizTag) {
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

        long nextId = getIdFromSegmentBuffer(manager.get(bizTag));
        log.info("end nextId({})={}", bizTag, nextId);
        if (log.isDebugEnabled()) {
            sw.stop("nextId", bizTag + ":" + nextId);
        }
        return nextId;

    }


    public void updateSegmentFromDb(final String bizTag) {
        StopWatch sw = new Slf4JStopWatch();
        Segment newSegment = new Segment();
        //先从DB for update获取锁，各个节点有可能同时更新值
        WorkerNodePo workerNodePo = disposableWorkerIdAssigner.selectByBizTagAndTypeTodoForUpdateOrInsert(UidGeneratorContext.UID_LOAD_DEF, bizTag);
        newSegment.setOk(true);
        newSegment.setMax(workerNodePo.getMaxSeq());
        LongAdder longAdder = new LongAdder();
        longAdder.add(workerNodePo.getMinSeq());
        newSegment.setValue(longAdder);
        newSegment.setStep(workerNodePo.getStep());
        newSegment.setBizTag(bizTag);
        newSegment.setUpdateTimestamp(System.currentTimeMillis());
        cache.put(bizTag, workerNodePo);
        manager.put(bizTag, newSegment);
        sw.stop("updateSegmentFromDb", bizTag + " " + newSegment);
    }

    public long getIdFromSegmentBuffer(final Segment segment) {
        while (true) {
            try {
                segment.rLock().lock();
                if (!segment.isOk() && segment.getThreadRunning().compareAndSet(false, true)) {
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            boolean updateOk = false;
                            try {
                                updateSegmentFromDb(segment.getBizTag());
                                updateOk = true;
                                log.info("update segment {} from db", segment.getBizTag());
                            } catch (Exception e) {
                                log.warn(segment + " updateSegmentFromDb exception", e);
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

                segment.getValue().add(Long.valueOf(segment.getStep()));
                long value = segment.getValue().longValue();
                if (value <= segment.getMax()) {
                    manager.put(segment.getBizTag(), segment);
                    return value;
                }
                {
                    boolean SeqCycle = "Y".equalsIgnoreCase(cache.get(segment.getBizTag()).getSeqCycle()) ? true : false;
                    if (SeqCycle) {
                        LongAdder longAdder = new LongAdder();
                        longAdder.add(cache.get(segment.getBizTag()).getMinSeq());
                        segment.setValue(longAdder);
                        manager.put(segment.getBizTag(), segment);
                        return longAdder.longValue();
                    }
                    segment.setOk(false);
                }
            } finally {
                segment.rLock().unlock();
            }
            waitAndSleep(segment);
            try {
                segment.wLock().lock();
                final Segment segment2 = manager.get(segment.getBizTag());
                segment2.getValue().add(Long.valueOf(segment.getStep()));
                long value = segment2.getValue().longValue();
                if (value < segment2.getMax()) {
                    manager.put(segment.getBizTag(), segment2);
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

    @Override
    public void setDisposableWorkerIdAssigner(DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        this.disposableWorkerIdAssigner = disposableWorkerIdAssigner;
    }

    @Override
    public DisposableWorkerIdAssigner getDisposableWorkerIdAssigner() {
        return disposableWorkerIdAssigner;
    }
}
