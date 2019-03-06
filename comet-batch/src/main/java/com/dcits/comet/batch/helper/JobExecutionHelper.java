package com.dcits.comet.batch.helper;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobExecutionHelper {
    @Autowired
    private JobRepository jobRepository;

    
}
