package com.dcits.comet.uid.impl;

import com.dcits.comet.uid.entity.WorkerNodePo;
import com.dcits.comet.uid.worker.WorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;

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

    public LoadingUidGenerator(WorkerIdAssigner workerIdAssigner) {
        this();
        setWorkerIdAssigner(workerIdAssigner);
    }

    @Override
    protected synchronized long nextId(final String bizTag) {
        String seqName = null == bizTag ? WorkerIdAssigner.DEF : bizTag;
        if (!WorkerIdAssigner.keys.containsKey(seqName)) {
            workerIdAssigner.assignWorkerId(seqName);
            currentId = new LongAdder();
            currentId.add(Long.valueOf(WorkerIdAssigner.keys.get(seqName).getCurrSeq()));
        }

        WorkerNodePo workerNodePo = WorkerIdAssigner.keys.get(seqName);
        boolean cycle = workerNodePo.getSeqCycle().equals("Y") ? true : false;
        long nextid = currentId.longValue() + Long.valueOf(workerNodePo.getStep());
        //序列生成已经达到最大值
        if (nextid > Long.valueOf(workerNodePo.getMaxSeq())) {
            if (cycle) {
                currentId.reset();
                currentId.add(Long.valueOf(workerNodePo.getMinSeq()));
                nextid = currentId.longValue();
                asynLoadingSegment = false;
            } else {
                log.error("{}序列已经达到最大值且不可重复生成", bizTag);
                return -1L;
            }
        } else {
            currentId.add(Long.valueOf(workerNodePo.getStep()));
            nextid = currentId.longValue();
            asynLoadingSegment = true;
        }
        thresholdHandler(bizTag);
        return nextid;
    }



    private void thresholdHandler(String bizTag) {
        log.info("数据库同步操作");
        // 异步处理-启动线程更新DB，有线程池执行
        if (asynLoadingSegment) {
            asynLoadSegmentTask = new FutureTask<>(() -> {
                workerIdAssigner.doUpdateNextSegment(bizTag, currentId.longValue());
                return true;
            });
            taskExecutor.submit(asynLoadSegmentTask);
        } else {// 同步处理，直接更新DB
            workerIdAssigner.doUpdateNextSegment(bizTag, currentId.longValue());
        }
    }

}
