package com.dcits.comet.batch.sonic.dao;

import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;
import io.shardingsphere.core.hint.HintManagerHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class CifHintShardingAlgorithm implements HintShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ShardingValue shardingValue) {
        if(null!= HintManagerHolder.getDatabaseShardingValue(HintManagerHolder.DB_TABLE_NAME)){
            Collection<String> list =  ((ListShardingValue) HintManagerHolder.getDatabaseShardingValue(HintManagerHolder.DB_TABLE_NAME).get()).getValues();
            return list;
        }else{
            log.info("hint shardingValue未被赋值，路由到默认库执行sql");
        }
        return availableTargetNames;
    }
}
