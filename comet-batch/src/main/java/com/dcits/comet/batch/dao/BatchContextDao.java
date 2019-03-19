package com.dcits.comet.batch.dao;

import com.dcits.comet.batch.param.BatchContext;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;

public interface BatchContextDao {
    public BatchContext getBatchContext(String exeId);

    public void saveBatchContext(String exeId, String jobExecutionId, BatchContext batchContext);
}
