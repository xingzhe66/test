DROP TABLE IF EXISTS WORKER_NODE;
CREATE TABLE WORKER_NODE
(
  ID          BIGINT      NOT NULL AUTO_INCREMENT  COMMENT 'auto increment id',
  HOST_NAME   VARCHAR(64) NOT NULL  COMMENT 'host name',
  PORT        VARCHAR(64) NOT NULL  COMMENT 'port',
  TYPE        VARCHAR(30) NOT NULL  COMMENT 'node type: ACTUAL or CONTAINER',
  LAUNCH_DATE DATE        NOT NULL  COMMENT 'launch date',
  MODIFIED    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modified time',
  CREATED     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created time',
  BIZ_TAG     VARCHAR(30) COMMENT '业务标识',
  MIN_SEQ     VARCHAR(30) NOT NULL COMMENT '最小流水号信息',
  MAX_SEQ     VARCHAR(30) NOT NULL COMMENT '最大流水号信息',
  STEP        INT  NOT NULL COMMENT '流水号步长',
  CURR_SEQ    VARCHAR(30) NOT NULL COMMENT '当前流水号值',
  COUNT_SEQ   VARCHAR(30) NOT NULL COMMENT '流水循环次数',
  MIDDLE_ID   VARCHAR(30) NOT NULL COMMENT '阈值',
  SEQ_CYCLE   VARCHAR(30) NOT NULL COMMENT '是否循环序列 Y-是；N-不是',
  SEQ_CACHE   VARCHAR(30) NOT NULL COMMENT '缓存序列数',
  CACHE_COUNT VARCHAR(30) NOT NULL COMMENT '缓存序列计数',
  PRIMARY KEY (ID)
)
  COMMENT ='DB WorkerID Assigner for UID Generator', ENGINE = INNODB;

#//HOST_NAME和BIZ_TAG是唯一约束条件，映射为ID