package com.dcits.comet.dbsharding.helper;

import com.dcits.comet.commons.utils.SpringContextUtil;
import io.shardingsphere.core.rule.ShardingDataSourceNames;
import io.shardingsphere.shardingjdbc.jdbc.core.ShardingContext;
import io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.context.ApplicationContext;

import java.util.LinkedHashSet;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description sharding配置帮助类
 */
public class ShardingDataSourceHelper {

    public static LinkedHashSet getDataSourceNames() {
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        ShardingDataSource dataSource = applicationContext.getBean(ShardingDataSource.class);
        if (dataSource != null) {
            ShardingDataSourceNames shardingDataSourceNames = dataSource.getShardingContext().getShardingRule().getShardingDataSourceNames();
            return (LinkedHashSet) shardingDataSourceNames.getDataSourceNames();
        }
        return null;
    }


    public static ShardingContext getShardingContext() {
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        ShardingDataSource dataSource = applicationContext.getBean(ShardingDataSource.class);
        if (dataSource != null) {
            return dataSource.getShardingContext();
        }
        return null;


    }


}
