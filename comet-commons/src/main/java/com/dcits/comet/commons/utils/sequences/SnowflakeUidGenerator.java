package com.dcits.comet.commons.utils.sequences;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/6/6 14:58
 **/
public class SnowflakeUidGenerator {
    // ==============================Fields===========================================
    /**
     * 开始时间截 (2019-01-01)
     */
    private final long twepoch = 1546272000000L;


    /**
     * 时间戳所占的位数
     **/
    protected long timeBits = 38L;
    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 12L;

    private final long maxWorkerId = ~(-1L << workerIdBits);
    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 13L;
    private final long maxSequence = -1L ^ (-1L << sequenceBits);

    /**
     * 时间截向左移
     */
    private final long timestampShift = workerIdBits + sequenceBits;
    private final long workerIdShift = sequenceBits;


    /**
     * 工作机器
     */
    private long workerId;

    /**
     * 毫秒内序列
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    //==============================Constructors=====================================

    /**
     * 构造函数
     *
     * @param workerId 工作ID
     */
    public SnowflakeUidGenerator(long workerId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        this.workerId = workerId;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & maxSequence;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    //==============================Test=============================================

    /**
     * 测试
     */
    public static void main(String[] args) {
        SnowflakeUidGenerator idWorker = new SnowflakeUidGenerator(3);
        for (int i = 0; i < 1000; i++) {
            long id = idWorker.nextId();

            String binaryStr = Long.toBinaryString(id);
            System.out.println(binaryStr);
            System.out.println(id);
        }
    }
}
