package com.dcits.comet.batch;

import com.dcits.comet.batch.processor.Processor;
import com.dcits.comet.batch.reader.RowNumReader;
import com.dcits.comet.batch.writer.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import static java.lang.String.format;


/**
 * 动态注入batch相关bean
 *
 */

//@Configuration
@Deprecated
public class BatchBeanBuilder implements BeanFactoryPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(BatchBeanBuilder.class);



    public void buildBatchBean(ConfigurableListableBeanFactory configurableListableBeanFactory){


        log.info("BatchBeanBuilder开始执行。。。");

        //Bean的实例工厂
        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) configurableListableBeanFactory;
        //找到IBatch的bean定义名称数组；
        String[] names = dbf.getBeanNamesForType(IBStep.class);

        for (String name : names) {

            //Bean构建
            BeanDefinitionBuilder readerBuider = BeanDefinitionBuilder.genericBeanDefinition(RowNumReader.class);
            //向里面的属性注入值，提供get set方法
            readerBuider.addPropertyReference("batchStep",name); //因为实例还未生成，所以只定义引用；
            //todo 把相关配置放在接口中传入
            readerBuider.addPropertyValue("pageSize","1000");

            //.addPropertyValue("batch", batch);
            readerBuider.setScope("step");   //作用域为step，为了让jobParameters注解生效
            //将实例注册spring容器中   bs 等同于  id配置
            dbf.registerBeanDefinition("reader_" + name, readerBuider.getBeanDefinition());

            BeanDefinitionBuilder writerBuider = BeanDefinitionBuilder.genericBeanDefinition(Writer.class);
            writerBuider.addPropertyReference("batchStep",name);
            writerBuider.setScope("step");
            dbf.registerBeanDefinition("writer_" + name, writerBuider.getBeanDefinition());


            BeanDefinitionBuilder processorBuider = BeanDefinitionBuilder.genericBeanDefinition(Processor.class);
            processorBuider.addPropertyReference("batchStep", name);
            processorBuider.setScope("step");
            dbf.registerBeanDefinition("processor_" + name, processorBuider.getBeanDefinition());

        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
       this.buildBatchBean(configurableListableBeanFactory);
    }


}
