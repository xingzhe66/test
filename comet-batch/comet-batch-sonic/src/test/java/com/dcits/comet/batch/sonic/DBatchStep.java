package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.AbstractBStep;
import com.dcits.comet.batch.helper.JobParameterHelper;
import com.dcits.comet.batch.param.BatchContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/14 16:32
 **/
@Service("dBatchStep")
@Slf4j
public class DBatchStep extends AbstractBStep<WorkerNodePo, WorkerNodePo> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void preBatchStep(BatchContext batchContext) {
        log.info("GenPostStep.......preBatchStep");
        log.info("GenPostStep.......JobId=" + JobParameterHelper.get("jobId"));//从启动参数获取jobId
    }

    @Override
    public List getPageList(BatchContext batchContext, int offset, int pageSize, String node) {
        log.info("getPageList() begin ");
        final List<WorkerNodePo> forumList = new ArrayList<WorkerNodePo>();
        Connection connection = null;
        try {
            String sql = "SELECT ID,HOST_NAME,PORT FROM worker_node WHERE PORT = ?";
            DataSource dataSource = jdbcTemplate.getDataSource();
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "2");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                WorkerNodePo forum = new WorkerNodePo();
                forum.setId(resultSet.getLong("ID"));
                forum.setHostName(resultSet.getString("HOST_NAME"));
                forum.setPort(resultSet.getString("PORT"));
                forumList.add(forum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return forumList;
    }

    @Override
    public void writeChunk(BatchContext batchContext, List<WorkerNodePo> workerNodePoList) {
        log.info("writeChunk() begin, stockList.size=" + workerNodePoList.size());
        int[] updatedCountArray = jdbcTemplate.batchUpdate("update worker_node set PORT = ? where ID=?", new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, "3");//要注意，下标从1开始
                ps.setLong(2, workerNodePoList.get(i).getId());
            }

            public int getBatchSize() {

                return workerNodePoList.size();
            }
        });
        int sumInsertedCount = 0;
        for (int a : updatedCountArray) {
            sumInsertedCount += a;
        }
        log.info("batchInsert() end, stockList.size=" + workerNodePoList.size() + ",success inserted " + sumInsertedCount + " records");
    }


    @Override
    public void writeOne(BatchContext batchContext, WorkerNodePo item) {
        log.info("writezOne");
    }
}
