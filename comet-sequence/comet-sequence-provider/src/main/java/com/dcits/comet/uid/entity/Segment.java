package com.dcits.comet.uid.entity;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Segment {
    private LongAdder value = new LongAdder();
    private volatile long max;
    private String bizTag;
    private volatile int step;
    private final AtomicBoolean threadRunning = new AtomicBoolean(false);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile boolean isOk;

    private volatile long updateTimestamp;

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setBizTag(String bizTag) {
        this.bizTag = bizTag;
    }

    public String getBizTag() {
        return bizTag;
    }

    public AtomicBoolean getThreadRunning() {
        return threadRunning;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }


    public LongAdder getValue() {
        return value;
    }

    public void setValue(LongAdder value) {
        this.value = value;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public long getIdle() {
        return this.getMax() - getValue().longValue();
    }

    @Override
    public String toString() {
        return "Segment(value=" + this.getValue().longValue() + ", max=" + this.getMax() + ", bizTag=" + this.getBizTag() + ", step=" + this.getStep() + ", threadRunning=" + this.getThreadRunning() + ", lock=" + this.lock + ", isOk=" + this.isOk() + ", updateTimestamp=" + this.getUpdateTimestamp() + ")";
    }
}