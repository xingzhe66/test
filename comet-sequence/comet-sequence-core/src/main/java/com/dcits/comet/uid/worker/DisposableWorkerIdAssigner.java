package com.dcits.comet.uid.worker;

import com.dcits.comet.commons.exception.BusinessException;
import com.dcits.comet.commons.utils.NetUtils;
import com.dcits.comet.commons.utils.StringUtil;
import com.dcits.comet.uid.entity.WorkerNodePo;
import com.dcits.comet.uid.impl.DefaultUidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 1.获取机器序列号，支持直接去网卡IP地址或者从配置文件加载；2.获取流水号生成的步长
 *
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 10:26
 * @see DisposableWorkerIdAssigner
 **/
@Slf4j
public class DisposableWorkerIdAssigner implements WorkerIdAssigner {

    DataSource dataSource;

    /**
     * @return long
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 10:26
     * @Param []
     **/
    @Override
    public long assignWorkerId(final String bizType, final String type) {
        buildWorkerNode(type);
        if (keys.get(bizType) == null) {
            //默认创建的是雪花算法（雪花算法不需要人为的按照节点分配步长）
            createWorkerNode(bizType);
            buildWorkerNode(type);
            //throw new UidGenerateException("999999", "流水号参数未配置");
        }
        return keys.get(bizType).getId();
    }

    public void createWorkerNode(final String bizType) {
        String querySql = " INSERT  INTO WORKER_NODE (HOST_NAME, PORT, TYPE, LAUNCH_DATE,BIZ_TAG, STEP,MIN_SEQ, MIDDLE_ID, MAX_SEQ, CURR_SEQ, COUNT_SEQ,SEQ_CYCLE,SEQ_CACHE,CACHE_COUNT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        final WorkerNodePo currentSegment = WorkerNodePo.builder().build();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Date Sqldate = java.sql.Date.valueOf(LocalDate.now());
            connection = getConnection(dataSource);
            preparedStatement = connection.prepareStatement(querySql);
            preparedStatement.setString(1, NetUtils.getLocalAddress());
            preparedStatement.setString(2, System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
            preparedStatement.setString(3, DefaultUidGenerator.class.getSimpleName().toLowerCase());
            preparedStatement.setDate(4, Sqldate);
            preparedStatement.setString(5, bizType);
            preparedStatement.setString(6, "1");//STEP
            preparedStatement.setString(7, "0");
            preparedStatement.setString(8, String.valueOf(Long.MAX_VALUE / 2));
            preparedStatement.setString(9, String.valueOf(Long.MAX_VALUE));
            preparedStatement.setString(10, "1");
            preparedStatement.setString(11, "1");
            preparedStatement.setString(12, "Y");
            preparedStatement.setString(13, "1");
            preparedStatement.setString(14, "1");
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            log.error("数据库操作异常{}", e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                log.error("数据库操作异常{}", e);
            }
        } finally {
            try {
                if (Objects.nonNull(resultSet)) {
                    resultSet.close();
                }
                if (Objects.nonNull(preparedStatement)) {
                    preparedStatement.close();
                }
                if (Objects.nonNull(connection)) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("数据库操作异常{}", e);
            }
        }
    }

    /**
     * @return long
     * @Author leijian
     * @Description //TODO 按照节点初始化分配信息
     * @Date 2019/3/27 10:26
     * @Param []
     **/
    public void buildWorkerNode(String type) {
        log.info("初始化节点信息开始{}", type);
        if (keys.containsKey(type)) {
            log.info("缓存中已存在{}的值，直接从缓存取数，初始化节点信息结束", keys);
            return;
        }
        //查询出当前节点的workid序列号
        String sql = "SELECT ID, HOST_NAME, PORT, TYPE, LAUNCH_DATE, MODIFIED, CREATED, BIZ_TAG, STEP,MIN_SEQ, MIDDLE_ID, MAX_SEQ, CURR_SEQ, COUNT_SEQ,SEQ_CYCLE,SEQ_CACHE,CACHE_COUNT FROM WORKER_NODE WHERE HOST_NAME = \'" + NetUtils.getLocalAddress() + "\' AND TYPE = \'" + type + "\'";
        try (Connection connection = getConnection(dataSource);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {

            while (resultSet.next()) {
                WorkerNodePo workerNodePo = WorkerNodePo.builder()
                        .id(resultSet.getLong("ID"))
                        .hostName(resultSet.getString("HOST_NAME"))
                        .port(resultSet.getString("PORT"))
                        .type(resultSet.getString("TYPE"))
                        .launchDate(resultSet.getDate("LAUNCH_DATE"))
                        .modified(resultSet.getString("MODIFIED"))
                        .created(resultSet.getString("CREATED"))
                        .bizTag(resultSet.getString("BIZ_TAG"))
                        .step(resultSet.getString("STEP"))
                        .minSeq(resultSet.getString("MIN_SEQ"))
                        .middleId(resultSet.getString("MIDDLE_ID"))
                        .maxSeq(resultSet.getString("MAX_SEQ"))
                        .currSeq(resultSet.getString("CURR_SEQ"))
                        .countSeq(resultSet.getString("COUNT_SEQ"))
                        .seqCycle(resultSet.getString("SEQ_CYCLE"))
                        .seqCache(resultSet.getString("SEQ_CACHE"))
                        .cacheCount(resultSet.getString("CACHE_COUNT")).build();
                log.info("{}", workerNodePo);
                keys.putIfAbsent(workerNodePo.getBizTag(), workerNodePo);
            }
            connection.commit();

        } catch (Exception e) {
            log.error("{}", e);
        } finally {

        }

        log.info("初始化节点信息结束");
        return;
    }

    @Override
    public void doUpdateNextSegment(final String bizTag, final long nextid, final String type) {
        log.info("节点信息同步");
        try {
            updateId(bizTag, nextid, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<WorkerNodePo> getWorkNodePoList(String bizTag) {
        //查询出当前节点的workid序列号
        List<WorkerNodePo> list = new LinkedList<WorkerNodePo>();
        String sql = "SELECT ID, HOST_NAME, PORT, TYPE, LAUNCH_DATE, MODIFIED, CREATED, BIZ_TAG, STEP,MIN_SEQ, MIDDLE_ID, MAX_SEQ, CURR_SEQ, COUNT_SEQ,SEQ_CYCLE,SEQ_CACHE,CACHE_COUNT FROM WORKER_NODE WHERE HOST_NAME = \'" + NetUtils.getLocalAddress() + "\' AND BIZ_TAG = \'" + bizTag + "\'";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {

            while (resultSet.next()) {
                WorkerNodePo workerNodePo = WorkerNodePo.builder()
                        .id(resultSet.getLong("ID"))
                        .hostName(resultSet.getString("HOST_NAME"))
                        .port(resultSet.getString("PORT"))
                        .type(resultSet.getString("TYPE"))
                        .launchDate(resultSet.getDate("LAUNCH_DATE"))
                        .modified(resultSet.getString("MODIFIED"))
                        .created(resultSet.getString("CREATED"))
                        .bizTag(resultSet.getString("BIZ_TAG"))
                        .step(resultSet.getString("STEP"))
                        .minSeq(resultSet.getString("MIN_SEQ"))
                        .middleId(resultSet.getString("MIDDLE_ID"))
                        .maxSeq(resultSet.getString("MAX_SEQ"))
                        .currSeq(resultSet.getString("CURR_SEQ"))
                        .countSeq(resultSet.getString("COUNT_SEQ"))
                        .seqCycle(resultSet.getString("SEQ_CYCLE"))
                        .seqCache(resultSet.getString("SEQ_CACHE"))
                        .cacheCount(resultSet.getString("CACHE_COUNT")).build();
                log.info("{}", workerNodePo);
                list.add(workerNodePo);
            }
        } catch (Exception e) {
            log.error("{}", e);
        } finally {

        }
        return list;
    }

    private void updateId(String bizTag, final long nextid, final String type) {
        if (StringUtil.isEmpty(bizTag)) {
            bizTag = WorkerIdAssigner.DEF;
        }
        log.info("bizTag:{},nextid:{},type:{}", bizTag, nextid, type);
        String querySql = "SELECT ID, HOST_NAME, PORT, TYPE, LAUNCH_DATE, MODIFIED, CREATED, BIZ_TAG, STEP,MIN_SEQ, MIDDLE_ID, MAX_SEQ, CURR_SEQ, COUNT_SEQ,SEQ_CYCLE,SEQ_CACHE,CACHE_COUNT FROM WORKER_NODE WHERE HOST_NAME = ? AND BIZ_TAG = ? AND TYPE = ? FOR UPDATE";
        final WorkerNodePo currentSegment = WorkerNodePo.builder().build();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(dataSource);
            preparedStatement = connection.prepareStatement(querySql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, NetUtils.getLocalAddress());
            preparedStatement.setString(2, bizTag);
            preparedStatement.setString(3, type);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Date Sqldate = java.sql.Date.valueOf(LocalDate.now());
                WorkerNodePo workerNodePo = WorkerNodePo.builder()
                        .id(resultSet.getLong("ID"))
                        .hostName(resultSet.getString("HOST_NAME"))
                        .port(resultSet.getString("PORT"))
                        .type(resultSet.getString("TYPE"))
                        .launchDate(Sqldate)
                        .modified(resultSet.getString("MODIFIED"))
                        .created(resultSet.getString("CREATED"))
                        .bizTag(resultSet.getString("BIZ_TAG"))
                        .step(resultSet.getString("STEP"))
                        .minSeq(resultSet.getString("MIN_SEQ"))
                        .middleId(resultSet.getString("MIDDLE_ID"))
                        .maxSeq(resultSet.getString("MAX_SEQ"))
                        .currSeq(String.valueOf(nextid))
                        .countSeq(resultSet.getString("COUNT_SEQ"))
                        .seqCycle(resultSet.getString("SEQ_CYCLE"))
                        .seqCache(resultSet.getString("SEQ_CACHE"))
                        .cacheCount(resultSet.getString("CACHE_COUNT")).build();
                log.info("{}", workerNodePo);
                keys.putIfAbsent(workerNodePo.getBizTag(), workerNodePo);
                resultSet.updateDate("LAUNCH_DATE", Sqldate);
                resultSet.updateString("CURR_SEQ", String.valueOf(nextid));
                resultSet.updateRow();
            }
            connection.commit();
        } catch (Exception e) {
            log.error("数据同步异常{}", e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                log.error("数据库操作异常{}", e);
            }
        } finally {
            try {
                if (Objects.nonNull(resultSet)) {
                    resultSet.close();
                }
                if (Objects.nonNull(preparedStatement)) {
                    preparedStatement.close();
                }
                if (Objects.nonNull(connection)) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("数据库操作异常{}", e);
            }
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection(DataSource dataSource) {
        try {
            Connection con = dataSource.getConnection();
            con.setAutoCommit(false);
            return con;
        } catch (Exception e) {
            log.error("数据库操作异常", e);
            throw new BusinessException("数据库操作异常");

        }
    }

    @Override
    public void doUpdatenextSeqCache(WorkerNodePo workerNodePo, String seqCache, long nextid) {
        log.info("workerNodePo:{},seqCache:{},nextid:{}", workerNodePo, seqCache, nextid);
        String querySql = "SELECT ID, HOST_NAME, PORT, TYPE, LAUNCH_DATE, MODIFIED, CREATED, BIZ_TAG, STEP,MIN_SEQ, MIDDLE_ID, MAX_SEQ, CURR_SEQ, COUNT_SEQ,SEQ_CYCLE,SEQ_CACHE,CACHE_COUNT FROM WORKER_NODE WHERE HOST_NAME = ? AND BIZ_TAG = ? FOR UPDATE";
        final WorkerNodePo currentSegment = WorkerNodePo.builder().build();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(dataSource);
            preparedStatement = connection.prepareStatement(querySql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, NetUtils.getLocalAddress());
            preparedStatement.setString(2, workerNodePo.getBizTag());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                workerNodePo.setSeqCache(seqCache);
                workerNodePo.setCurrSeq(String.valueOf(nextid));
                Date Sqldate = Date.valueOf(LocalDate.now());
                log.info("{}", workerNodePo);
                keys.putIfAbsent(workerNodePo.getBizTag(), workerNodePo);
                resultSet.updateDate("LAUNCH_DATE", Sqldate);
                resultSet.updateString("CURR_SEQ", String.valueOf(nextid));
                if(!StringUtil.isEmpty(seqCache)){
                    resultSet.updateString("SEQ_CACHE", seqCache);
                }
                resultSet.updateRow();
            }
            connection.commit();
        } catch (Exception e) {
            log.error("数据同步异常{}", e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                log.error("数据库操作异常{}", e);
            }
        } finally {
            try {
                if (Objects.nonNull(resultSet)) {
                    resultSet.close();
                }
                if (Objects.nonNull(preparedStatement)) {
                    preparedStatement.close();
                }
                if (Objects.nonNull(connection)) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("数据库操作异常{}", e);
            }
        }
    }
}
