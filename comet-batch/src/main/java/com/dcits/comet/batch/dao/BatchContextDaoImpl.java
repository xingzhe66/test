package com.dcits.comet.batch.dao;

import com.alibaba.fastjson.JSON;
import com.dcits.comet.batch.param.BatchContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BatchContextDaoImpl implements BatchContextDao {

    private JdbcOperations jdbcTemplate;

    @Override
    public BatchContext getBatchContext(String exeId) {
        return jdbcTemplate.queryForObject("select params from batch_context where exe_id=?",new Object[] { exeId},
                (resultSet, i) -> {
                    if(null==resultSet.getString(1)) return null;
                    BatchContext batchContext=new BatchContext();
                    batchContext.setParams(JSON.parseObject(resultSet.getString(1)));
                    return batchContext;
                });
    }

    @Override
    public void saveBatchContext(String exeId,String jobExecutionId, BatchContext batchContext) {
        jdbcTemplate.update("delete from batch_context where job_execution_id = ?", new Object[] { jobExecutionId },
                new int[] { java.sql.Types.INTEGER });
        jdbcTemplate.update("insert into batch_context(exe_id,job_execution_id,params) values(?,?,?)",
                new Object[] { exeId,jobExecutionId,JSON.toJSONString(batchContext.getParams()) });

    }

    public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
