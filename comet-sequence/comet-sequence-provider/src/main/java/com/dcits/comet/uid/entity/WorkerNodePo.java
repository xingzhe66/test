package com.dcits.comet.uid.entity;


import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/25 17:39
 **/
@Entity
@Table(name = "worker_node", schema = "workflow", catalog = "")
@ToString
public class WorkerNodePo implements Serializable {

    private Long id;
    private String hostName;
    private String port;
    private String type;
    private LocalDateTime launchDate;
    private LocalDateTime modified;
    private LocalDateTime created;
    private String bizTag;
    private long minSeq;
    private long maxSeq;
    private int step;
    private long currSeq;
    private long countSeq;
    private String middleId;
    private String seqCycle;
    private long seqCache;
    private long cacheCount;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "HOST_NAME")
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Basic
    @Column(name = "PORT")
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Basic
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "LAUNCH_DATE")
    public LocalDateTime getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDateTime launchDate) {
        this.launchDate = launchDate;
    }

    @Basic
    @Column(name = "MODIFIED")
    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    @Basic
    @Column(name = "CREATED")
    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Basic
    @Column(name = "BIZ_TAG")
    public String getBizTag() {
        return bizTag;
    }

    public void setBizTag(String bizTag) {
        this.bizTag = bizTag;
    }

    @Basic
    @Column(name = "MIN_SEQ")
    public long getMinSeq() {
        return minSeq;
    }

    public void setMinSeq(long minSeq) {
        this.minSeq = minSeq;
    }

    @Basic
    @Column(name = "MAX_SEQ")
    public long getMaxSeq() {
        return maxSeq;
    }

    public void setMaxSeq(long maxSeq) {
        this.maxSeq = maxSeq;
    }

    @Basic
    @Column(name = "STEP")
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Basic
    @Column(name = "CURR_SEQ")
    public long getCurrSeq() {
        return currSeq;
    }

    public void setCurrSeq(long currSeq) {
        this.currSeq = currSeq;
    }

    @Basic
    @Column(name = "COUNT_SEQ")
    public long getCountSeq() {
        return countSeq;
    }

    public void setCountSeq(long countSeq) {
        this.countSeq = countSeq;
    }

    @Basic
    @Column(name = "MIDDLE_ID")
    public String getMiddleId() {
        return middleId;
    }

    public void setMiddleId(String middleId) {
        this.middleId = middleId;
    }

    @Basic
    @Column(name = "SEQ_CYCLE")
    public String getSeqCycle() {
        return seqCycle;
    }

    public void setSeqCycle(String seqCycle) {
        this.seqCycle = seqCycle;
    }

    @Basic
    @Column(name = "SEQ_CACHE")
    public long getSeqCache() {
        return seqCache;
    }

    public void setSeqCache(long seqCache) {
        this.seqCache = seqCache;
    }

    @Basic
    @Column(name = "CACHE_COUNT")
    public long getCacheCount() {
        return cacheCount;
    }

    public void setCacheCount(long cacheCount) {
        this.cacheCount = cacheCount;
    }

}
