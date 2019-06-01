package com.dcits.comet.batch.config;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public interface IBatchTxConfig {
    DataSourceTransactionManager getDataSourceTransactionManager();
}
