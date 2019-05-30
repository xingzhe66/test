package com.dctis.comet.autoconfigure.common.dao;

import com.dcits.comet.dao.interceptor.MySqlInterceptor;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@AutoConfigureAfter(value = {ShardingDataSourceConfig.class, DataSourceConfig.class})
public class MybatisConfig {
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory shardSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*.xml"));
        org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(false);
        configuration.setLazyLoadingEnabled(false);
        configuration.setAggressiveLazyLoading(false);
        configuration.setLogImpl(StdOutImpl.class);
        configuration.addInterceptor(new MySqlInterceptor());
        //configuration.addInterceptor(new OracleInterceptor());
        //configuration.addInterceptor(new SelectForUpdateHelper());
        bean.setConfiguration(configuration);
        //bean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis/mybatis-config.xml"));
       // bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath*:META-INF/mybatis/mybatis-config.xml"));

        return bean.getObject();
    }

    @Bean(name = "shardTransactionManager")
    public DataSourceTransactionManager shardTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate shardSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
