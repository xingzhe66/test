package com.dcits.comet.batch.dao;

import com.dcits.comet.batch.param.BatchContext;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description 批量上下文Dao
 */
public interface BatchContextDao {
    public BatchContext getBatchContext(String exeId);

    public void saveBatchContext(String exeId, String jobExecutionId, BatchContext batchContext);
}
