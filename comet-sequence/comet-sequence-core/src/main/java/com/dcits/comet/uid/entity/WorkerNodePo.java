package com.dcits.comet.uid.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author leijian
 * @version 1.0
 * @date 2019-03-27 17:09:00
 * @see WorkerNodePo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ToString
public class WorkerNodePo {
    /**
     * This field corresponds to the database column WORKER_NODE.ID     *     * @Description auto increment id
     */
    private Long id;
    /**
     * This field corresponds to the database column WORKER_NODE.HOST_NAME     *     * @Description host name
     */
    private String hostName;
    /**
     * This field corresponds to the database column WORKER_NODE.PORT     *     * @Description port
     */
    private String port;
    /**
     * This field corresponds to the database column WORKER_NODE.TYPE     *     * @Description node type: ACTUAL or CONTAINER
     */
    private String type;
    /**
     * This field corresponds to the database column WORKER_NODE.LAUNCH_DATE     *     * @Description launch date
     */
    private java.sql.Date launchDate;
    /**
     * This field corresponds to the database column WORKER_NODE.MODIFIED     *     * @Description modified time
     */
    private String modified;
    /**
     * This field corresponds to the database column WORKER_NODE.CREATED     *     * @Description created time
     */
    private String created;
    /**
     * This field corresponds to the database column WORKER_NODE.BIZ_TAG     *     * @Description 业务标识
     */
    private String bizTag;
    /**
     * This field corresponds to the database column WORKER_NODE.STEP     *     * @Description 流水号步长
     */
    private String step;
    /**
     * This field corresponds to the database column WORKER_NODE.MIN_SEQ     *     * @Description 最小流水号信息
     */
    private String minSeq;
    /**
     * This field corresponds to the database column WORKER_NODE.MIDDLE_ID     *     * @Description 阈值
     */
    private String middleId;
    /**
     * This field corresponds to the database column WORKER_NODE.MAX_SEQ     *     * @Description 最大流水号信息
     */
    private String maxSeq;
    /**
     * This field corresponds to the database column WORKER_NODE.CURR_SEQ     *     * @Description 当前流水号值
     */
    private String currSeq;
    /**
     * This field corresponds to the database column WORKER_NODE.COUNT_SEQ     *     * @Description 流水号累计次数
     */
    private String countSeq;

    /**
     * This field corresponds to the database column WORKER_NODE.SEQ_CYCLE     *     * @Description 是否循环序列 Y-是；N-不是
     */
    private String seqCycle;

    /**
     * This field corresponds to the database column WORKER_NODE.SEQ_CACHE     *     * @Description 流水号累计次数
     */
    private String seqCache;

    /**
     * This field corresponds to the database column WORKER_NODE.CACHE_COUNT     *     * @Description 流水号累计次数
     */
    private String cacheCount;
}
