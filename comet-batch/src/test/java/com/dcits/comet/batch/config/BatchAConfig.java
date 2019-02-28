package com.dcits.comet.batch.config;

import com.dcits.comet.batch.listener.CsvJobListener;
import com.dcits.comet.batch.model.Person;
import com.dcits.comet.batch.processor.CsvItemProcessor;
import com.dcits.comet.batch.validator.CsvBeanValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
/**
 *
 * 以后可以一个批量任务，建立一个BatchConfig
 * 每个BatchConfig里声明一套batch所需的内容。
 *
 */
public class BatchAConfig {
    @Bean
    public ItemReader<Person> reader() throws Exception {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();//1使用FlatFileItemReader读取文件。
        reader.setResource(new ClassPathResource("people.csv"));//2使用FlatFileItemReader的setResource方法设置csv文件的路径。
        reader.setLineMapper(new DefaultLineMapper<Person>() {{//3在此处对cvs文件的数据和领域模型类做对应映射。
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"name", "age", "nation", "address"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        CsvItemProcessor processor = new CsvItemProcessor();//1使用我们自己定义的ItemProcessor的实现CsvItemProcessor。
        processor.setValidator(csvBeanValidator());//2为processor指定校验器为CsvBeanValidator；
        return processor;
    }

    @Bean
    public ItemWriter<Person> writer(@Qualifier("dataSource") DataSource dataSource) {//1Spring能让容器中已有的Bean以参数的形式注入，Spring Boot已为我们定义了dataSource。
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();//2我们使用JDBC批处理的JdbcBatchItemWriter来写数据到数据库。
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        String sql = "insert into person " + " (name,age,nation,address) "
                + "values( :name, :age, :nation,:address)";
        writer.setSql(sql);//3在此设置要执行批处理的SQL语句。
        writer.setDataSource(dataSource);
        return writer;
    }

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

    @Bean
    public Job importJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("importJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1) //1为Job指定Step。
                .end()
                .listener(csvJobListener()) //2绑定监听器csvJobListener。
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Person> reader, ItemWriter<Person> writer,
                      ItemProcessor<Person, Person> processor) {
        return stepBuilderFactory
                .get("step1")
                .<Person, Person>chunk(65000)//1批处理每次提交65000条数据。
                .reader(reader)//2给step绑定reader。
                .processor(processor)//3给step绑定processor。
                .writer(writer)//4给step绑定writer。
                .build();
    }

    @Bean
    public CsvJobListener csvJobListener() {
        return new CsvJobListener();
    }

    @Bean
    public Validator<Person> csvBeanValidator() {
        return new CsvBeanValidator<Person>();
    }
}
