package com.dcits.comet.uid.worker;

import com.dcits.comet.commons.exception.UidGenerateException;
import com.dcits.comet.commons.utils.NetUtils;
import com.dcits.comet.uid.entity.WorkerNodePo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    public long assignWorkerId(final String bizType) {
        buildWorkerNode();
        if (keys.get(bizType) == null) {
            throw new UidGenerateException("流水号生成参数异常[" + bizType + "]");
        }
        return keys.get(bizType).getId();
    }

    /**
     * @return long
     * @Author leijian
     * @Description //TODO 按照节点初始化分配信息
     * @Date 2019/3/27 10:26
     * @Param []
     **/
    public void buildWorkerNode() {
        log.info("初始化节点信息开始");
        if (!CollectionUtils.isEmpty(keys)) {
            log.info("缓存中信息结束");
            return;
        }

        //查询出当前节点的workid序列号
        String sql = "SELECT ID, HOST_NAME, PORT, TYPE, LAUNCH_DATE, MODIFIED, CREATED, BIZ_TAG, STEP, " +
                "MIN_SEQ, MIDDLE_ID, MAX_SEQ, CURR_SEQ, COUNT_SEQ FROM WORKER_NODE WHERE HOST_NAME = \'" + NetUtils.getLocalAddress() + "\'";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            if (resultSet.wasNull()) {

            }
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
                        .countSeq(resultSet.getString("COUNT_SEQ")).build();
                log.info("{}", workerNodePo);
                keys.putIfAbsent(workerNodePo.getBizTag() == null ? workerNodePo.getHostName() : workerNodePo.getBizTag(), workerNodePo);
            }
        } catch (Exception e) {
            log.error("{},{}", e, e);
        } finally {

        }


        //workerNodePoList.stream().filter(Objects::nonNull).forEach(v -> keys.putIfAbsent(v.getBizTag() == null ? v.getHostName() : v.getBizTag(), v));
        log.info("初始化节点信息结束");
        return;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
