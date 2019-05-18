package com.dcits.comet.uid.impl;

import com.dcits.comet.commons.exception.UidGenerateException;
import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.context.UidGeneratorContext;
import com.dcits.comet.uid.entity.WorkerNodePo;
import com.dcits.comet.uid.worker.BitsAllocator;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/25 18:31
 **/
@Slf4j
public class DefaultUidGenerator implements UidGenerator {

    private volatile boolean initOK = false;

    /**
     * Bits allocate
     */
    protected int timeBits = 31;
    protected int workerBits = 20;
    protected int seqBits = 12;


    protected String epochStr = "2019-01-01";
    protected long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1546272000000L);

    /**
     * Stable fields after spring bean initializing
     */
    protected BitsAllocator bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);
    protected long workerId;

    /**
     * Volatile fields caused by nextId()
     */
    protected long sequence = 0L;
    protected long lastSecond = -1L;

    private DisposableWorkerIdAssigner disposableWorkerIdAssigner;

    public void init() {
        log.info("Init ...{}", this.getClass().getSimpleName());
        WorkerNodePo workerNodePo = disposableWorkerIdAssigner.findByHostNameAndBizTag(UidGeneratorContext.UID_DEF_DEF);
        if (null == workerNodePo) {
            workerNodePo = disposableWorkerIdAssigner.save(UidGeneratorContext.UID_DEF_DEF, UidGeneratorContext.UID_DEF_DEF);
        }
        this.workerId = workerNodePo.getId();
        initOK = true;
        log.info("Init End...{},WorkerNodePo[{}]", this.getClass().getSimpleName(),workerNodePo);
    }

    @Override
    public long getUID() throws UidGenerateException {
        try {
            return getUID(null);
        } catch (Exception e) {
            log.error("Generate unique id exception.{}", e);
            throw new UidGenerateException("999999", "流水号生成异常");
        }
    }


    @Override
    public long getUID(String bizTag) throws UidGenerateException {
        return nextId(bizTag);
    }

    @Override
    public List<Long> getUIDList(long value) throws UidGenerateException {
        Assert.isTrue(value <= 0L, "获取流水号的个数不能小于或者等于0");
        return getUIDList(null, value);
    }

    @Override
    public List<Long> getUIDList(String bizTag, long value) throws UidGenerateException {
        Assert.isTrue(value <= 0L, "获取流水号的个数不能小于或者等于0");
        List uidList = new LinkedList();
        for (long i = 0L; i < value; i++) {
            uidList.add(nextId(null));
        }

        return uidList;
    }


    @Override
    public String parseUID(long uid) {
        //TODO
        return "";
    }

    /**
     * @return long
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 10:53
     * @Param []
     **/
    protected synchronized long nextId(final String bizTag) {
        StopWatch sw = null;
        if (log.isDebugEnabled()) {
            sw = new Slf4JStopWatch();
        }
        log.info("begin nextId({})", bizTag);
        if (!initOK) {
            throw new UidGenerateException("999999", "流水号生成异常");
        }
        long currentSecond = getCurrentSecond();
        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            log.error("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
            throw new UidGenerateException("999999", "流水号生成异常");
        }
        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
            if (sequence == 0) {
                currentSecond = getNextSecond(lastSecond);
            }
        } else {
            sequence = 0L;
        }
        lastSecond = currentSecond;
        long nextId = bitsAllocator.allocate(currentSecond - epochSeconds, workerId, sequence);
        log.info("end nextId({})={}", bizTag, nextId);
        if (log.isDebugEnabled()) {
            sw.stop("nextId", bizTag + ":" + nextId);
        }
        return nextId;
    }

    /**
     * Get next millisecond
     */
    private long getNextSecond(long lastTimestamp) {
        long timestamp = getCurrentSecond();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentSecond();
        }
        return timestamp;
    }

    /**
     * Get current second
     */
    private long getCurrentSecond() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - epochSeconds > bitsAllocator.getMaxDeltaSeconds()) {
            throw new UidGenerateException("999999", "Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }

        return currentSecond;
    }


    public void setTimeBits(int timeBits) {
        if (timeBits > 0) {
            this.timeBits = timeBits;
        }
    }

    public void setWorkerBits(int workerBits) {
        if (workerBits > 0) {
            this.workerBits = workerBits;
        }
    }

    public void setSeqBits(int seqBits) {
        if (seqBits > 0) {
            this.seqBits = seqBits;
        }
    }

    public void setEpochStr(String epochStr) {
        if (StringUtils.isNotBlank(epochStr)) {
            this.epochStr = epochStr;
            this.epochSeconds = TimeUnit.MILLISECONDS.toSeconds(DateUtil.parseDate(epochStr).getTime());
        }
    }

    public void setDisposableWorkerIdAssigner(DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        this.disposableWorkerIdAssigner = disposableWorkerIdAssigner;
    }

    public DisposableWorkerIdAssigner getDisposableWorkerIdAssigner() {
        return disposableWorkerIdAssigner;
    }
}

