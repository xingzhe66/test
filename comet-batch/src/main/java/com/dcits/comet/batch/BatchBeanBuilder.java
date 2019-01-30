package com.dcits.comet.batch;

import com.dcits.comet.batch.processor.Processor;
import com.dcits.comet.batch.reader.AbstractPagingReader;
import com.dcits.comet.batch.reader.Reader;
import com.dcits.comet.batch.writer.Writer;
import com.dcits.comet.dao.model.BasePo;
import com.fasterxml.jackson.core.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static java.lang.String.format;


/**
 * 动态注入batch相关bean
 *
 */
@Configuration

public class BatchBeanBuilder implements BeanFactoryPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(BatchBeanBuilder.class);



    public void build(ConfigurableListableBeanFactory configurableListableBeanFactory){


        log.info("BatchBeanBuilder开始执行。。。");

        //Bean的实例工厂
        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) configurableListableBeanFactory;
        //找到IBatch的bean定义名称数组；
        String[] names = dbf.getBeanNamesForType(IBatch.class);

        for (String name : names) {

            //Bean构建
            BeanDefinitionBuilder readerBuider = BeanDefinitionBuilder.genericBeanDefinition(Reader.class);
            //向里面的属性注入值，提供get set方法
            readerBuider.addPropertyReference("batch",name); //因为实例还未生成，所以只定义引用；
                    //.addPropertyValue("batch", batch);
            readerBuider.setScope("step");   //作用域为step，为了让jobParameters注解生效
            //将实例注册spring容器中   bs 等同于  id配置
            dbf.registerBeanDefinition("reader_" + name, readerBuider.getBeanDefinition());

            BeanDefinitionBuilder writerBuider = BeanDefinitionBuilder.genericBeanDefinition(Writer.class);
            writerBuider.addPropertyReference("batch",name);
            writerBuider.setScope("step");
            dbf.registerBeanDefinition("writer_" + name, writerBuider.getBeanDefinition());


            BeanDefinitionBuilder processorBuider = BeanDefinitionBuilder.genericBeanDefinition(Processor.class);
            processorBuider.addPropertyReference("batch", name);
            processorBuider.setScope("step");
            dbf.registerBeanDefinition("processor_" + name, processorBuider.getBeanDefinition());

        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        this.build(configurableListableBeanFactory);

    }
}
