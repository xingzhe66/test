package com.dcits.comet.uid.impl;

import com.dcits.comet.commons.utils.NetUtils;
import com.dcits.comet.uid.entity.WorkerNodePo;
import com.dcits.comet.uid.worker.WorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/29 14:39
 **/
@Slf4j
public class LoadingUidGenerator extends DefaultUidGenerator {

    private static ReentrantLock lock = new ReentrantLock();

    private Map<String, WorkerNodePo> manager = new ConcurrentHashMap<>();

    private volatile WorkerNodePo workerNodePo = WorkerNodePo.builder().build();
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
     * 异步线程任务
     */
    FutureTask<Boolean> asynLoadSegmentTask = null;

    public LoadingUidGenerator() {
        if (taskExecutor == null) {
            taskExecutor = Executors.newSingleThreadExecutor();
        }

    }

    @Override
    protected long nextId(final String bizTag) {
        String seqName = null == bizTag ? NetUtils.getLocalAddress() : bizTag;
        if (!WorkerIdAssigner.keys.containsKey(seqName)) {
            workerIdAssigner.assignWorkerId(seqName);
            currentId = new LongAdder();
            currentId.add(Long.valueOf(WorkerIdAssigner.keys.get(seqName).getCurrSeq()));
        }

        WorkerNodePo workerNodePo = WorkerIdAssigner.keys.get(seqName);
        boolean cycle = workerNodePo.getSeqCycle().equals("Y") ? true : false;

        long nextid = currentId.longValue() + Long.valueOf(workerNodePo.getStep());
        //序列生成已经达到最大值
        if (workerNodePo.getMaxSeq().equals(currentId.sum()) || nextid > Long.valueOf(workerNodePo.getMaxSeq())) {
            if (cycle) {
                try {
                    lock.lock();
                    currentId.reset();
                    currentId.add(Long.valueOf(workerNodePo.getMinSeq()));

                    nextid = currentId.longValue();
                    thresholdHandler(bizTag, nextid);
                } finally {
                    lock.unlock();
                }
            } else {
                log.error("{}序列已经达到最大值且不可重复生成", bizTag);
                nextid = -1L;
            }
        } else {
            currentId.add(Long.valueOf(workerNodePo.getStep()));
            nextid = currentId.longValue();
            thresholdHandler(bizTag, nextid);
        }

        return nextid;
    }


    private void thresholdHandler(String bizTag, final long nextid) {
        log.info("数据库同步操作");
        // 异步处理-启动线程更新DB，有线程池执行
        if (asynLoadingSegment) {
            asynLoadSegmentTask = new FutureTask<>(() -> {
                workerIdAssigner.doUpdateNextSegment(bizTag, nextid);
                return true;
            });
            taskExecutor.submit(asynLoadSegmentTask);
        } else {// 同步处理，直接更新DB
            workerIdAssigner.doUpdateNextSegment(bizTag, nextid);
        }
    }

}
