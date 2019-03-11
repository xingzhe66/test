package com.dcits.comet.dbsharding.helper;

import com.dcits.comet.commons.utils.SpringContextUtil;
import io.shardingsphere.core.rule.ShardingDataSourceNames;
import io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.LinkedHashSet;
import java.util.List;

@Component
public class ShardingDataSourceHelper {

    public static LinkedHashSet getDataSourceNames(){
        ApplicationContext applicationContext= SpringContextUtil.getApplicationContext();
        ShardingDataSource dataSource= (ShardingDataSource) applicationContext.getBean("shardingDataSource");

        if (dataSource!=null) {
            ShardingDataSourceNames shardingDataSourceNames = dataSource.getShardingContext().getShardingRule().getShardingDataSourceNames();
            return (LinkedHashSet) shardingDataSourceNames.getDataSourceNames();
        }

        return null;


    }


}
