package com.dctis.comet.autoconfigure.common.exception;

import com.dcits.comet.commons.exception.ExceptionContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionContainerConfig {

    @Bean
    public ExceptionContainer exceptionContainer(){
        return new ExceptionContainer();
    }
}
