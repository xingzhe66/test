//package com.dcits.comet.batch.sonic;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanClassLoaderAware;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.beans.factory.BeanFactoryAware;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.ResourceLoaderAware;
//import org.springframework.context.annotation.DeferredImportSelector;
//import org.springframework.core.Ordered;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.core.type.AnnotationMetadata;
//import org.springframework.util.Assert;
//
///**
// * @author leijian
// * @version 1.0
// * @date 2019/5/7 16:18
// **/
//public class SonicExecutorServerRegistrar implements DeferredImportSelector, BeanClassLoaderAware, ResourceLoaderAware,
//        BeanFactoryAware, EnvironmentAware, Ordered {
//
//    private ClassLoader beanClassLoader;
//
//    private ConfigurableListableBeanFactory beanFactory;
//
//    private Environment environment;
//
//    @Override
//    public void setBeanClassLoader(ClassLoader classLoader) {
//        this.beanClassLoader = classLoader;
//    }
//
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        Assert.isInstanceOf(ConfigurableListableBeanFactory.class, beanFactory);
//        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
//    }
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        this.environment = environment;
//    }
//
//    protected final Environment getEnvironment() {
//        return this.environment;
//    }
//
//    @Override
//    public void setResourceLoader(ResourceLoader resourceLoader) {
//
//    }
//
//    @Override
//    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
//        return new String[0];
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
