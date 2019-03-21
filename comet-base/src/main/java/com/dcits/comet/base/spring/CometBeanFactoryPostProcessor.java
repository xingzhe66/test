package com.dcits.comet.base.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description BeanFactoryPostProcessor
 */
public class CometBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    public CometBeanFactoryPostProcessor() {
//        System.out.println("【BeanFactoryPostProcessor接口】调用BeanFactoryPostProcessor实现类构造方法");
    }

    /**
     * 重写BeanFactoryPostProcessor接口的postProcessBeanFactory方法，可通过该方法对beanFactory进行设置
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
//        System.out.println("【BeanFactoryPostProcessor接口】调用BeanFactoryPostProcessor接口的postProcessBeanFactory方法");
//        beanFactory.
//
//
//        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("studentBean");
//        beanDefinition.getPropertyValues().addPropertyValue("age", "21");
//        beanDefinition.getPropertyValues().addPropertyValue("sex", "21");

//        System.out.println("————————————————————————————");

    }
}
