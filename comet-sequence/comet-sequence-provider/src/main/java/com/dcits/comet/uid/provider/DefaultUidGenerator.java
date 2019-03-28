package com.dcits.comet.uid.provider;

import com.dcits.comet.commons.exception.UidGenerateException;
import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.commons.utils.NetUtils;
import com.dcits.comet.commons.utils.StringUtil;
import com.dcits.comet.uid.BitsAllocator;
import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.worker.WorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 10:16
 * @see DefaultUidGenerator
 **/
@Slf4j
public class DefaultUidGenerator implements UidGenerator, InitializingBean {
    /**
     * Bits allocate
     */
    protected int timeBits = 28;
    protected int workerBits = 22;
    protected int seqBits = 13;

    /**
     * Customer epoch, unit as second. For example 2016-05-20 (ms: 1463673600000)
     */
    protected String epochStr = "2016-05-20";
    protected long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1463673600000L);

    /**
     * Stable fields after spring bean initializing
     */
    protected BitsAllocator bitsAllocator;
    protected long workerId;

    /**
     * Volatile fields caused by nextId()
     */
    protected long sequence = 0L;
    protected long lastSecond = -1L;

    /**
     * Spring property
     */
    protected WorkerIdAssigner workerIdAssigner;

    private long sequenceMask = 99999;

    @Override
    public String getUID() throws UidGenerateException {
        try {
            return getUID(false);
        } catch (Exception e) {
            log.error("Generate unique id exception. ", e);
            throw new UidGenerateException("999999", "流水号生成异常");
        }
    }

    @Override
    public String getUID(boolean preDate) throws UidGenerateException {
        return getUID(null, preDate);
    }

    @Override
    public String getUID(String bizTag, boolean preDate) throws UidGenerateException {
        return nextId(bizTag, preDate);
    }


    @Override
    public String parseUID(String uidd) {
        long uid = Long.valueOf(uidd);
        long totalBits = BitsAllocator.TOTAL_BITS;
        long signBits = bitsAllocator.getSignBits();
        long timestampBits = bitsAllocator.getTimestampBits();
        long workerIdBits = bitsAllocator.getWorkerIdBits();
        long sequenceBits = bitsAllocator.getSequenceBits();

        // parse UID
        long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long workerId = (uid << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
        long deltaSeconds = uid >>> (workerIdBits + sequenceBits);

        Date thatTime = new Date(TimeUnit.SECONDS.toMillis(epochSeconds + deltaSeconds));
        String thatTimeStr = DateUtil.formatDate(thatTime, DateUtil.PATTERN_ISO_DATETIME);

        // format as string
        return String.format("{\"UID\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}",
                uid, thatTimeStr, workerId, sequence);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // initialize bits allocator
        bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);

        // initialize worker id为主机地址
        workerId = workerIdAssigner.assignWorkerId(NetUtils.getLocalAddress());
        if (workerId > bitsAllocator.getMaxWorkerId()) {
            throw new RuntimeException("Worker id " + workerId + " exceeds the max " + bitsAllocator.getMaxWorkerId());
        }
        log.info("InitializingBean{}", this.getClass().getName());
        log.info("Initialized bits(1, {}, {}, {}) for workerID:{}", timeBits, workerBits, seqBits, workerId);
    }

    /**
     * @return long
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 10:53
     * @Param []
     **/
    protected synchronized String nextId(final String bizTag, final boolean preDate) {
        if (!StringUtil.isEmpty(bizTag)) {
            workerId = workerIdAssigner.assignWorkerId(bizTag);
        }
        if (preDate) {
            return preDatenextId(workerId);
        }
        long currentSecond = getCurrentSecond();
        // Clock moved backwards, refuse to generate uid
        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            log.error("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
            throw new UidGenerateException("999999", "流水号生成异常");
        }

        // At the same second, increase sequence
        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
            // Exceed the max sequence, we wait the next second to generate uid
            if (sequence == 0) {
                currentSecond = getNextSecond(lastSecond);
            }
            // At the different second, sequence restart from zero
        } else {
            sequence = 0L;
        }

        lastSecond = currentSecond;

        // Allocate bits for UID
        return String.valueOf(bitsAllocator.allocate(currentSecond - epochSeconds, workerId, sequence));
    }

    /**
     * @param [workerId]
     * @return java.lang.String
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/28 13:14
     **/
    public synchronized String preDatenextId(final long workerId) {
        long currentSecond = getCurrentSecond();
        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            log.error("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
            throw new UidGenerateException("999999", "流水号生成异常");
        }
        // 如果是同一时间生成的，则进行秒内序列
        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) % sequenceMask;
            // Exceed the max sequence, we wait the next second to generate uid
            if (sequence == 0) {
                currentSecond = getNextSecond(lastSecond);
                sequence = 0L;
            }
            // At the different second, sequence restart from zero
        } else {
            sequence = 0L;
        }

        lastSecond = currentSecond;

        String ssequence = String.valueOf(sequence);
        if (sequence < 10000) {
            while (ssequence.length() < 5) {
                ssequence = '0' + ssequence;
            }
        }
        String dateStr = DateUtil.formatDate(new Date(TimeUnit.SECONDS.toMillis(currentSecond)), "yyyyMMddHHmm");
        String sworkerId = String.valueOf(workerId);
        while (sworkerId.length() < 2) {
            sworkerId = '0' + sworkerId;
        }
        return dateStr + sworkerId + ssequence;
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
            throw new UidGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }

        return currentSecond;
    }

    /**
     * Setters for spring property
     */
    public void setWorkerIdAssigner(WorkerIdAssigner workerIdAssigner) {
        this.workerIdAssigner = workerIdAssigner;
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
}

