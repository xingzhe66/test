package com.dcits.comet.batch.config;

import com.dcits.comet.batch.dao.BatchContextDao;
import com.dcits.comet.batch.dao.BatchContextDaoImpl;
import com.dcits.comet.batch.launcher.CometJobLauncher;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.jsr.JsrJobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description 批量默认配置
 */
//@Configuration
public class BatchConfig {


    @Value("${batch.jdbc.driver}")
    String driverClassName;
    @Value("${batch.jdbc.url}")
    String url;
    @Value("${batch.jdbc.user}")
    String userName;
    @Value("${batch.jdbc.password}")
    String password;
    @Value("${batch.jdbc.testWhileIdle}")
    String testWhileIdle;
    @Value("${batch.jdbc.validationQuery}")
    String validationQuery;

    @Value("${batch.threadPool.corePoolSize}")
    int corePoolSize;
    @Value("${batch.threadPool.maxPoolSize}")
    int maxPoolSize;
    @Value("${batch.threadPool.queueCapacity}")
    int queueCapacity;
    @Value("${batch.threadPool.keepAliveSeconds}")
    int keepAliveSeconds;
    @Value("${batch.threadPool.threadNamePrefix}")
    String threadNamePrefix;
    @Value("${batch.threadPool.waitForTasksToCompleteOnShutdown}")
    boolean waitForTasksToCompleteOnShutdown;



    @Bean(name="batchTaskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 设置队列容量
        executor.setQueueCapacity(queueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 设置默认线程名称
        executor.setThreadNamePrefix(threadNamePrefix);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        return executor;
    }

    @Bean
    public static StepScope stepScope() {
        StepScope stepScope = new StepScope();
        stepScope.setAutoProxy(false);
        return stepScope;
    }

    @Bean
    public static JobScope jobScope() {
        JobScope jobScope = new JobScope();
        jobScope.setAutoProxy(false);
        return jobScope;
    }
    @Bean
    public BatchContextDao batchContextDao(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        BatchContextDao batchContextDao = new BatchContextDaoImpl();
        ((BatchContextDaoImpl) batchContextDao).setJdbcTemplate(jdbcTemplate);
        return batchContextDao;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("batchDataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean(name = "batchTransactionManager")
    @Conditional(JobRepositoryCondition.class)
    public DataSourceTransactionManager batchTransactionManager(@Qualifier("batchDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "batchDataSource")
    @Conditional(JobRepositoryCondition.class)
    public DataSource dataSource(
    ) throws Exception {

        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setTestWhileIdle(Boolean.valueOf(testWhileIdle));
        dataSource.setValidationQuery(validationQuery);


        return dataSource;
    }

    @Bean
    @Conditional(JobRepositoryCondition.class)
    public JobRepository jobRepository(@Qualifier("batchDataSource") DataSource dataSource,
                                       @Qualifier("batchTransactionManager") PlatformTransactionManager transactionManager)
            throws Exception {
        //new MapJobRepositoryFactoryBean();

        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setDatabaseType(DatabaseType.MYSQL.name());

        return jobRepositoryFactoryBean.getObject();

    }
    @Bean
    @Conditional(JobRepositoryCondition.class)
    public JobRepository jobRepositoryMap()
            throws Exception {
        MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean=new MapJobRepositoryFactoryBean();
        return mapJobRepositoryFactoryBean.getObject();

    }
//    @Bean
//    public SimpleJobLauncher jobLauncher(
//            @Qualifier("jobRepository") JobRepository jobRepository
//    ) throws Exception {
//
//        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//        jobLauncher.setJobRepository(jobRepository);
//        //jobLauncher.setTaskExecutor();
//        return jobLauncher;
//    }
    @Bean
    public CometJobLauncher jobLauncher(
            @Qualifier("jobRepository") JobRepository jobRepository
    ) throws Exception {

        CometJobLauncher jobLauncher = new CometJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        //jobLauncher.setTaskExecutor();
        return jobLauncher;
    }
    @Bean(name = "stepBuilders")
    public StepBuilderFactory stepBuilders(
            @Qualifier("jobRepository") JobRepository jobRepository,
            @Qualifier("batchTransactionManager") DataSourceTransactionManager batchTransactionManager
    ) throws Exception {

        StepBuilderFactory stepBuilderFactory = new StepBuilderFactory(jobRepository, batchTransactionManager);
        return stepBuilderFactory;
    }

    @Bean(name = "jobBuilders")
    public JobBuilderFactory jobBuilders(
            @Qualifier("jobRepository") JobRepository jobRepository
    ) throws Exception {

        JobBuilderFactory jobBuilders = new JobBuilderFactory(jobRepository);
        return jobBuilders;
    }


    @Bean(name = "batchJobOperator")
    public SimpleJobOperator batchJobOperator(
            @Qualifier("jobExplorer") JobExplorer jobExplorer,
            @Qualifier("jobLauncher") JobLauncher jobLauncher,
            @Qualifier("jobRegistry") JobRegistry jobRegistry,
            @Qualifier("jobRepository") JobRepository jobRepository
    ) throws Exception {

        SimpleJobOperator batchJobOperator = new SimpleJobOperator();
        batchJobOperator.setJobExplorer(jobExplorer);
        batchJobOperator.setJobLauncher(jobLauncher);
        batchJobOperator.setJobRegistry(jobRegistry);
        batchJobOperator.setJobRepository(jobRepository);

        return batchJobOperator;
    }


    @Bean(name = "jobExplorer")
    public JobExplorerFactoryBean jobExplorer(
            @Qualifier("batchDataSource") DataSource dataSource
    ) throws Exception {

        JobExplorerFactoryBean jobExplorer = new JobExplorerFactoryBean();
        jobExplorer.setDataSource(dataSource);

        return jobExplorer;
    }



    @Bean(name = "jobParametersConverter")
    public JsrJobParametersConverter jobParametersConverter(
            @Qualifier("batchDataSource") DataSource dataSource
    ) throws Exception {

        JsrJobParametersConverter jobParametersConverter = new JsrJobParametersConverter(dataSource);

        return jobParametersConverter;
    }

    @Bean(name = "jobRegistry")
    public MapJobRegistry jobRegistry(
    ) throws Exception {

        MapJobRegistry jobRegistry = new MapJobRegistry();

        return jobRegistry;
    }


}
