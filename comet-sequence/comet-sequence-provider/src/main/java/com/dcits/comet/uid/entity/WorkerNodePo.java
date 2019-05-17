package com.dcits.comet.uid.entity;


import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    private String minSeq;
    private String maxSeq;
    private int step;
    private String currSeq;
    private String countSeq;
    private String middleId;
    private String seqCycle;
    private String seqCache;
    private String cacheCount;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue
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
    public String getMinSeq() {
        return minSeq;
    }

    public void setMinSeq(String minSeq) {
        this.minSeq = minSeq;
    }

    @Basic
    @Column(name = "MAX_SEQ")
    public String getMaxSeq() {
        return maxSeq;
    }

    public void setMaxSeq(String maxSeq) {
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
    public String getCurrSeq() {
        return currSeq;
    }

    public void setCurrSeq(String currSeq) {
        this.currSeq = currSeq;
    }

    @Basic
    @Column(name = "COUNT_SEQ")
    public String getCountSeq() {
        return countSeq;
    }

    public void setCountSeq(String countSeq) {
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
    public String getSeqCache() {
        return seqCache;
    }

    public void setSeqCache(String seqCache) {
        this.seqCache = seqCache;
    }

    @Basic
    @Column(name = "CACHE_COUNT")
    public String getCacheCount() {
        return cacheCount;
    }

    public void setCacheCount(String cacheCount) {
        this.cacheCount = cacheCount;
    }

}
