package com.dcits.comet.batch.config.sharding;

import com.dcits.comet.dbsharding.TableTypeMapContainer;
import com.google.common.collect.Multimap;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.HintShardingStrategyConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShardingConfig {

    @Primary
    @DependsOn({"ds_0", "ds_1", "ds_2"})
    @Bean(name = "shardingDataSource")
    public DataSource getDataSource(@Qualifier("ds_0") DataSource ds0, @Qualifier("ds_1") DataSource ds1, @Qualifier("ds_2") DataSource ds2) throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        //未配置分片规则的表将通过默认数据源定位
        shardingRuleConfig.setDefaultDataSourceName("ds_0");
        //默认分库策略
        //shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new HintShardingStrategyConfiguration(new RbHintShardingAlgorithm()));
        //默认分表策略
        //shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", new TablePreciseShardingAlgorithm(), new TableRangeShardingAlgorithm()));
        //获取表的类型和表名
        Multimap<String, String> paramMultiMap = TableTypeMapContainer.getMultiMap();
        //如果是水平分库表分片规则列表
        if(paramMultiMap.get("level").size()>0){
            for(String levelTableName : paramMultiMap.get("level")){
                shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration(levelTableName));
            }
        }
        //绑定表规则列表
        if(paramMultiMap.get("param").size()>0){
            String paramString= paramMultiMap.get("param").toString();
            shardingRuleConfig.getBindingTableGroups().add(paramString.substring(1,paramString.length()-1));
        }
//        //绑定表规则列表
        if(paramMultiMap.get("upright").size()>0){
            String paramString= paramMultiMap.get("upright").toString();
            shardingRuleConfig.getBindingTableGroups().add(paramString.substring(1,paramString.length()-1));
        }
        //数据源集合
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds_2", ds2);
        dataSourceMap.put("ds_1", ds1);
        dataSourceMap.put("ds_0", ds0);
        //配置信息
        Properties properties = new Properties();
        properties.setProperty("sql.show", Boolean.TRUE.toString());
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new HashMap<String, Object>(), properties);
    }

    private static TableRuleConfiguration getTableRuleConfiguration(String tableName) {
        TableRuleConfiguration result = new TableRuleConfiguration();
        result.setLogicTable(tableName);
        result.setActualDataNodes("ds_${0..2}."+tableName);
        // result.setKeyGeneratorColumnName("id");
        //result.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("client_no",new RbDatabaseShardingAlgorithm()));
        // result.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "sys_log"));
        return result;
    }

}


