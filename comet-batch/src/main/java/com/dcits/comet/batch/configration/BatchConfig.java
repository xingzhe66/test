package com.dcits.comet.batch.configration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig implements ApplicationContextAware {

    private static ApplicationContext applicationContext=null;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }


    public  static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}