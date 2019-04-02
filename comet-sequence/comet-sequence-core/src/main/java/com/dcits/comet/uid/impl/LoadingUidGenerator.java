package com.dcits.comet.uid.impl;

import com.dcits.comet.commons.exception.UidGenerateException;
import com.dcits.comet.uid.entity.WorkerNodePo;
import com.dcits.comet.uid.thread.NamedThreadFactory;
import com.dcits.comet.uid.worker.WorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/29 14:39
 **/
@Slf4j
public class LoadingUidGenerator extends DefaultUidGenerator {

    private static ReentrantLock lock = new ReentrantLock();

    private String className = getClass().getSimpleName().toLowerCase();
    // 创建线程池
    private ExecutorService taskExecutor;
    /**
     * 异步标识(true-异步，false-同步)
     */
    private boolean asynLoadingSegment = false;
    /**
     * 当前生成的序列值
     */
    private LongAdder currentId;
    /**
     * 同步时间(单位毫秒)
     */
    private static final int DELAY = 3 * 1000;

    /**
     *
     */
    private int seqCacheStep = 100;
    /**
     * 定时任务延迟指定时间执行
     */
    private static final int INITIALDELAY = 3 * 1000;
    /**
     * 定时任务执行器
     */
    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("keep-loadinguidgenerator-uid-sync", true));
    /**
     * 异步线程任务
     */
    FutureTask<Boolean> asynLoadSegmentTask = null;

    private Map<String, LongAdder> manager = new ConcurrentHashMap();

    public LoadingUidGenerator() {
        if (taskExecutor == null) {
            taskExecutor = Executors.newSingleThreadExecutor();
        }
        scheduledExecutorService.scheduleWithFixedDelay(this::keepWithDB, INITIALDELAY, DELAY, TimeUnit.MILLISECONDS);
    }

    public LoadingUidGenerator(WorkerIdAssigner workerIdAssigner) {
        this();
        setWorkerIdAssigner(workerIdAssigner);
    }

    @Override
    protected synchronized long nextId(final String bizTag) {
        String seqName = null == bizTag ? WorkerIdAssigner.DEF : bizTag;
        if (!WorkerIdAssigner.keys.containsKey(seqName)) {
            workerIdAssigner.assignWorkerId(seqName, className);
            currentId = new LongAdder();
            currentId.add(Long.valueOf(WorkerIdAssigner.keys.get(seqName).getCurrSeq()));
            manager.put(seqName, currentId);
        }
        currentId = manager.get(seqName);
        WorkerNodePo workerNodePo = WorkerIdAssigner.keys.get(seqName);
        if (!this.getClass().getSimpleName().toLowerCase().equals(workerNodePo.getType())) {
            throw new UidGenerateException("999999", "流水类型[" + bizTag + "]配置的实现type是[" + workerNodePo.getType() + "]");
        }
        boolean cycle = workerNodePo.getSeqCycle().equals("Y") ? true : false;
        long nextid = currentId.longValue() + Long.valueOf(workerNodePo.getStep());
        //序列生成已经达到最大值
        if (nextid > Long.valueOf(workerNodePo.getMaxSeq())) {
            if (cycle) {
                currentId.reset();
                currentId.add(Long.valueOf(workerNodePo.getMinSeq()));
                nextid = currentId.longValue();
                asynLoadingSegment = false;
                thresholdHandler(bizTag);
            } else {
                log.error("{}序列已经达到最大值且不可重复生成", bizTag);
                return -1L;
            }
        } else if (!cycle && nextid > Long.valueOf(workerNodePo.getSeqCache()) && Long.valueOf(workerNodePo.getSeqCache()) > 0L) {
            //不是循环的流水序列类型支持动态调整数据类型
            currentId.reset();
            List<WorkerNodePo> workerNodePoList = workerIdAssigner.getWorkNodePoList(bizTag);
            //获取分配节点的最大值，从最大值开始分配下一个cacheseq
            String seqCache = workerNodePoList.stream().sorted(Comparator.comparing(WorkerNodePo::getSeqCache)).collect(Collectors.toList()).get(0).getSeqCache();
            String nextSeqCache = String.valueOf(Integer.valueOf(seqCache) + seqCacheStep);
            currentId.add(Long.valueOf(nextSeqCache));
            nextid = currentId.longValue();
            //更新并且重置keys缓存
            workerIdAssigner.doUpdatenextSeqCache(workerNodePo, seqCache, nextid);
            asynLoadingSegment = false;
            thresholdHandler(bizTag);
        } else {
            currentId.add(Long.valueOf(workerNodePo.getStep()));
            nextid = currentId.longValue();
            asynLoadingSegment = true;
        }
        manager.put(seqName, currentId);
        return nextid;
    }


    public void setSeqCacheStep(int seqCacheStep) {
        this.seqCacheStep = seqCacheStep;
    }

    private void thresholdHandler(String bizTag) {
        // 异步处理-启动线程更新DB，有线程池执行
        if (asynLoadingSegment) {
            log.info("异步处理-启动线程更新DB");
            asynLoadSegmentTask = new FutureTask<>(() -> {
                workerIdAssigner.doUpdateNextSegment(bizTag, manager.get(bizTag).longValue(), className);
                return true;
            });
            taskExecutor.submit(asynLoadSegmentTask);
        } else {// 同步处理，直接更新DB
            log.info("同步处理直接更新DB");
            workerIdAssigner.doUpdateNextSegment(bizTag, manager.get(bizTag).longValue(), className);
        }
    }

    @Override
    public void keepWithDB() {
        WorkerIdAssigner.keys.forEach((bizTag, workerNodePo) -> {
            long updateUid = manager.get(bizTag).longValue();
            workerIdAssigner.doUpdateNextSegment(bizTag, updateUid, className);
        });
    }
}
