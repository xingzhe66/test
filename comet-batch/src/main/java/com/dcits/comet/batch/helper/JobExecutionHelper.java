package com.dcits.comet.batch.helper;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description job运行上下文helper
 */
@Component
public class JobExecutionHelper {
    @Autowired
    private JobRepository jobRepository;

    
}
