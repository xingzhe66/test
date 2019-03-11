package com.dcits.comet.batch;

import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.processor.Processor;
import com.dcits.comet.batch.reader.Reader;
import com.dcits.comet.batch.writer.Writer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class BatchBeanFactory {
    /**
     * todo 多线程对同一个job并发时，需要用new对象，不能用spring bean
     * 目前使用spring bean的意义在于让jobParameters注解生效。
     */

    public static ItemReader getReader(String name, int pageSize, int beginIndex, int endIndex){

        ConfigurableApplicationContext context= (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();

        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getBeanFactory();

        if(dbf.containsBean("reader_" + name)){
            dbf.removeBeanDefinition("reader_" + name);
            dbf.destroySingleton("reader_" + name);
        }
        //Bean构建
        BeanDefinitionBuilder readerBuider = BeanDefinitionBuilder.genericBeanDefinition(Reader.class);
        //向里面的属性注入值，提供get set方法
        readerBuider.addPropertyReference("batchStep", name); //因为实例还未生成，所以只定义引用；
        //todo 把相关配置放在接口中传入
        readerBuider.addPropertyValue("pageSize", pageSize);
        if(beginIndex>=0&&endIndex>0) {
            readerBuider.addPropertyValue("beginIndex", beginIndex);
            readerBuider.addPropertyValue("endIndex", endIndex);
            readerBuider.setInitMethodName("init");
        }
        //.addPropertyValue("batch", batch);
        readerBuider.setScope("step");   //作用域为step，为了让jobParameters注解生效
        //将实例注册spring容器中   bs 等同于  id配置
        dbf.registerBeanDefinition("reader_" + name, readerBuider.getBeanDefinition());

        return (ItemReader) dbf.getBean("reader_" + name);
    }

    /**
     * 多实例
     */
    public static ItemReader getNewReader(String stepName, int pageSize, int beginIndex, int endIndex, String node){
        ConfigurableApplicationContext context= (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();

        Reader reader=new Reader();
        reader.setBatchStep((IBStep) context.getBean(stepName));
        reader.setPageSize(pageSize);
        reader.setNode(node);
        if(beginIndex>=0&&endIndex>0) {
            reader.setBeginIndex(beginIndex);
            reader.setEndIndex(endIndex);
            reader.init();
        }
        return reader;
    }
    public static ItemProcessor getNewProcessor(String stepName){
        ConfigurableApplicationContext context= (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();
        Processor processor=new Processor();
        processor.setBatchStep((IBStep) context.getBean(stepName));
        return processor;
    }

    public static ItemWriter getNewWriter(String stepName){
        ConfigurableApplicationContext context= (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();
        Writer writer =new Writer();
        writer.setBatchStep((IBStep) context.getBean(stepName));
        return writer;
    }

    public static ItemProcessor getProcessor(String name){
        ConfigurableApplicationContext context= (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();

        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getBeanFactory();

        if(dbf.containsBean("processor_" + name)){
            dbf.removeBeanDefinition("processor_" + name);
            dbf.destroySingleton("processor_" + name);
        }
        BeanDefinitionBuilder processorBuider = BeanDefinitionBuilder.genericBeanDefinition(Processor.class);
        processorBuider.addPropertyReference("batchStep", name);
        processorBuider.setScope("step");
        dbf.registerBeanDefinition("processor_" + name, processorBuider.getBeanDefinition());
        return (ItemProcessor) dbf.getBean("processor_" + name);
    }

    public static ItemWriter getWriter(String name){
        ConfigurableApplicationContext context= (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();

        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getBeanFactory();

        if(dbf.containsBean("writer_" + name)){
            dbf.removeBeanDefinition("writer_" + name);
            dbf.destroySingleton("writer_" + name);
        }

        BeanDefinitionBuilder writerBuider = BeanDefinitionBuilder.genericBeanDefinition(Writer.class);
        writerBuider.addPropertyReference("batchStep", name);
        writerBuider.setScope("step");
        dbf.registerBeanDefinition("writer_" + name, writerBuider.getBeanDefinition());

        return (ItemWriter) dbf.getBean("writer_" + name);
    }
}
