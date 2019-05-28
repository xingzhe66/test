package com.dcits.comet.batch.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author wangyun
 * @Date 2019/5/28
 **/
public class JobRepositoryCondition implements Condition {

    public static final String BATCH_JDBC_DRIVER = "batch.jdbc.driver";

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment env = conditionContext.getEnvironment();
        return env.containsProperty(BATCH_JDBC_DRIVER);
    }
}
