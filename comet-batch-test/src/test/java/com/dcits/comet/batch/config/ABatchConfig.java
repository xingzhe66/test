package com.dcits.comet.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 *
 * 以后可以一个批量任务，建立一个BatchConfig
 * 每个BatchConfig里声明一套batch所需的内容。
 *
 */
/**
 * EnableBatchProcessing
 * 会自动注入以下bean
 JobRepository bean 名称 "jobRepository"
 JobLauncher bean名称"jobLauncher"
 JobRegistry bean名称"jobRegistry"
 PlatformTransactionManager bean名称 "transactionManager"
 JobBuilderFactory bean名称"jobBuilders"
 StepBuilderFactory bean名称"stepBuilders"
 *
 * 也就是当配置文件里定义spring.batch.job.enabled为true，
 * 或者没定义（默认为true）的时候，会初始化一个
 * JobLauncherCommandLineRunner的bean。
 * 也就是只要能找到ApplicationRunner或者CommandLineRunner的子类，就挨个执行。
 *
 *这个注解可能会导致Job的多次执行，需要注意。
 *
 * TODO：后续将配置拆出来，不允许使用此注解。
 */
public class ABatchConfig {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ABatchConfig.class);



    @Autowired
    private JobBuilderFactory jobBuilders;
//
//    @Bean
//    public Job job1(@Qualifier("step1") Step step1) {
//        LOGGER.info("job1.......");
//        return jobBuilders.get("job1")
//                .incrementer(new RunIdIncrementer())
//                .flow(step1) //1为Job指定Step。
//                .end()
//                //   .listener(csvJobListener()) //2绑定监听器csvJobListener。
//                .build();
//
//    }
//
//
//    @Autowired
//    private StepBuilderFactory stepBuilders;
////
//    @Bean
//    public Step step1() {
//        LOGGER.info("step1.......");
//        return stepBuilders.get("step1")
//                .<SysLog, SysLog>chunk(500)
//               // .reader(reader1).faultTolerant().skip(JsonParseException.class).skipLimit(1)
//                //    .listener(new MessageItemReadListener(errorWriter))
//              //  .writer(writer1).faultTolerant().skip(Exception.class).skipLimit(1)
//                //    .listener(new MessageWriteListener())
//                .build();
//    }

//
//
//    @Resource(name="cBatch")
//    IBatchStep cBatch;
//    @Bean
//    @StepScope
//    public ItemReader reader_cBatch() {
//        LOGGER.info("reader_cBatch.......");
//        Reader reader=new Reader();
//        reader.setBatch(cBatch);
//        return reader;
//    }
//    @Bean
//    @StepScope
//    public ItemProcessor processor_cBatch() {
//        LOGGER.info("reader_cBatch.......");
//        Processor processor=new Processor();
//        processor.setBatch(cBatch);
//        return processor;
//    }
//    @Bean
//    @StepScope
//    public ItemWriter writer_cBatch() {
//        LOGGER.info("reader_cBatch.......");
//        Writer writer=new Writer();
//        writer.setBatch(cBatch);
//        return writer;
//    }

//    @Autowired
//    private StepBuilderFactory stepBuilders;
//    @Bean
//    public Step step1(@Qualifier("reader_cBatch") ItemReader reader,
//                      @Qualifier("writer_cBatch")ItemWriter writer) {
//        LOGGER.info("step1.......");
//        return stepBuilders.get("step1")
//                .<SysLog, SysLog>chunk(500)
//               .reader(reader).faultTolerant().skip(JsonParseException.class).skipLimit(1)
//                //    .listener(new MessageItemReadListener(errorWriter))
//               .writer(writer).faultTolerant().skip(Exception.class).skipLimit(1)
//                //    .listener(new MessageWriteListener())
//                .build();
//    }


















//    @Bean
//    public JobRepository jobRepository2()
//            throws Exception {
//        //jobRepository的定义需要dataSource和transactioManager，Spring Boot已为我们自动配置了这两个类，Spring可通过方法注入已有的Bean。
////        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
////        jobRepositoryFactoryBean.setDataSource(dataSource);
////        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
////        jobRepositoryFactoryBean.setDatabaseType("mysql");
//        MapJobRepositoryFactoryBean jobRepositoryFactoryBean = new  MapJobRepositoryFactoryBean();
//        return jobRepositoryFactoryBean.getObject();
//    }

//    @Bean
//    public SimpleJobLauncher jobLauncher2(@Qualifier("jobRepository2")JobRepository jobRepositoryFactoryBean)
//            throws Exception {
//        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//        jobLauncher.setJobRepository(jobRepositoryFactoryBean);
//        return jobLauncher;
//    }




//    @Bean
//    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Person> reader, ItemWriter<Person> writer,
//                      ItemProcessor<Person, Person> processor) {
//        return stepBuilderFactory
//                .get("step1")
//                .<Person, Person>chunk(65000)//1批处理每次提交65000条数据。
//                .reader(reader)//2给step绑定reader。
//                .processor(processor)//3给step绑定processor。
//                .writer(writer)//4给step绑定writer。
//                .build();
//    }
}
