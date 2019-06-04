package com.dcits.comet.batch.service.test.config;

import com.dcits.comet.batch.config.IBatchTxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @Author wangyun
 * @Date 2019/6/1
 **/
@Configuration
public class BatchTxConfig implements IBatchTxConfig {

    @Autowired
    @Qualifier("dbTransactionManager")
    DataSourceTransactionManager dataSourceTransactionManager;

    @Override
    public DataSourceTransactionManager getDataSourceTransactionManager() {
        return dataSourceTransactionManager;
    }


}
