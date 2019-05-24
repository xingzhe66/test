package com.dcits.comet.batch;

import static com.dcits.comet.batch.constant.BatchConstant.*;
import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.processor.Processor;
import com.dcits.comet.batch.reader.IReader;
import com.dcits.comet.batch.reader.RowNumReader;
import com.dcits.comet.batch.reader.SegmentReader;
import com.dcits.comet.batch.reader.SplitSegmentReader;
import com.dcits.comet.batch.step.StepParam;
import com.dcits.comet.batch.writer.Writer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import static com.dcits.comet.batch.constant.BatchConstant.PROCESSOR_NAME_PEX;
import static com.dcits.comet.batch.constant.BatchConstant.WRITER_NAME_PEX;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description BatchBeanFactory
 */
public class BatchBeanFactory {



    /**
     * todo 多线程对同一个job并发时，需要用new对象，不能用spring bean
     * 目前使用spring bean的意义在于让jobParameters注解生效。
     */

    public static ItemReader getReader(String name, int pageSize, int beginIndex, int endIndex){

        ConfigurableApplicationContext context= (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();

        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getBeanFactory();

        if(dbf.containsBean(READER_NAME_PEX + name)){
            dbf.removeBeanDefinition(READER_NAME_PEX + name);
            dbf.destroySingleton(READER_NAME_PEX + name);
        }
        //Bean构建
        BeanDefinitionBuilder readerBuider = BeanDefinitionBuilder.genericBeanDefinition(RowNumReader.class);
        //向里面的属性注入值，提供get set方法
        readerBuider.addPropertyReference(BATCH_STEP_NAME, name); //因为实例还未生成，所以只定义引用；
        //todo 把相关配置放在接口中传入
        readerBuider.addPropertyValue(PAGE_SIZE_NAME, pageSize);
        if(beginIndex>=0&&endIndex>0) {
            readerBuider.addPropertyValue(BEGIN_INDEX, beginIndex);
            readerBuider.addPropertyValue(END_INDEX, endIndex);
            readerBuider.setInitMethodName(INIT);
        }
        //.addPropertyValue("batch", batch);
        readerBuider.setScope("step");   //作用域为step，为了让jobParameters注解生效
        //将实例注册spring容器中   bs 等同于  id配置
        dbf.registerBeanDefinition(READER_NAME_PEX + name, readerBuider.getBeanDefinition());

        return (ItemReader) dbf.getBean(READER_NAME_PEX + name);
    }

    /**
     * 多实例
     */
    public static ItemReader getNewReader(StepParam stepParam){

        int pageSize = stepParam.getPageSize();
        int beginIndex = (int) stepParam.getBeginIndex();
        int endIndex = (int) stepParam.getEndIndex();
        String stepName = stepParam.getStepName();
        String node = stepParam.getNode();

        ConfigurableApplicationContext context= (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();
        IStep ibStep=(IStep) context.getBean(stepName);

        if(ibStep instanceof IRowNumStep) {
            RowNumReader rowNumReader = new RowNumReader();
            rowNumReader.setBatchStep((IRowNumStep) ibStep);
            rowNumReader.setPageSize(pageSize);
            rowNumReader.setNode(node);
            if (beginIndex >= 0 && endIndex > 0) {
                rowNumReader.setBeginIndex(beginIndex);
                rowNumReader.setEndIndex(endIndex);
                rowNumReader.init();
            }
            return rowNumReader;
        }else if(ibStep instanceof ISegmentStep) {
//            SegmentReader segmentReader = new SegmentReader();
            SplitSegmentReader segmentReader = new SplitSegmentReader();
            segmentReader.setBatchStep((ISegmentStep) ibStep);
//            segmentReader.setStart(stepParam.getSegmentStart());
//            segmentReader.setEnd(stepParam.getSegmentEnd());
            segmentReader.setBeginIndex(stepParam.getSegmentStart());
            segmentReader.setEndIndex(stepParam.getSegmentEnd());
            segmentReader.setKeyField(stepParam.getKeyField());
            segmentReader.setPageSize(stepParam.getPageSize());
            segmentReader.setStepName(stepParam.getStepName());
            segmentReader.setNode(node);
            return segmentReader;
        }
        return null;
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

        if(dbf.containsBean(PROCESSOR_NAME_PEX + name)){
            dbf.removeBeanDefinition(PROCESSOR_NAME_PEX + name);
            dbf.destroySingleton(PROCESSOR_NAME_PEX + name);
        }
        BeanDefinitionBuilder processorBuider = BeanDefinitionBuilder.genericBeanDefinition(Processor.class);
        processorBuider.addPropertyReference(BATCH_STEP, name);
        processorBuider.setScope(STEP);
        dbf.registerBeanDefinition(PROCESSOR_NAME_PEX + name, processorBuider.getBeanDefinition());
        return (ItemProcessor) dbf.getBean(PROCESSOR_NAME_PEX + name);
    }

    public static ItemWriter getWriter(String name){
        ConfigurableApplicationContext context= (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();

        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getBeanFactory();

        if(dbf.containsBean(WRITER_NAME_PEX + name)){
            dbf.removeBeanDefinition(WRITER_NAME_PEX + name);
            dbf.destroySingleton(WRITER_NAME_PEX + name);
        }

        BeanDefinitionBuilder writerBuider = BeanDefinitionBuilder.genericBeanDefinition(Writer.class);
        writerBuider.addPropertyReference(BATCH_STEP, name);
        writerBuider.setScope(STEP);
        dbf.registerBeanDefinition(WRITER_NAME_PEX + name, writerBuider.getBeanDefinition());

        return (ItemWriter) dbf.getBean(WRITER_NAME_PEX + name);
    }
}
