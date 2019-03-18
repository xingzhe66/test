package com.dcits.comet.batch;

import com.dcits.comet.batch.dao.BatchContextDao;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.support.SimpleJobRepository;

public class CometJobRepository extends SimpleJobRepository {
    private BatchContextDao batchContextDao;

    public CometJobRepository(JobInstanceDao jobInstanceDao, JobExecutionDao jobExecutionDao, StepExecutionDao stepExecutionDao, ExecutionContextDao ecDao,BatchContextDao batchContextDao) {

        super(jobInstanceDao, jobExecutionDao, stepExecutionDao, ecDao);
        this.batchContextDao=batchContextDao;

    }

    public CometJobRepository(JobInstanceDao jobInstanceDao, JobExecutionDao jobExecutionDao, StepExecutionDao stepExecutionDao, ExecutionContextDao ecDao) {
        super(jobInstanceDao, jobExecutionDao, stepExecutionDao, ecDao);
    }

}
