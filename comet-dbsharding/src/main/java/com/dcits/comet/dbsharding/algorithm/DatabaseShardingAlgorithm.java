package com.dcits.comet.dbsharding.algorithm;

import com.alibaba.fastjson.JSON;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
/**
 * @author wangyun
 * @date 2019/3/21
 * @description 精确分片算法
 */
public final class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DatabaseShardingAlgorithm.class);
    @Override
    public String doSharding(final Collection<String> availableTargetNames, final PreciseShardingValue<Long> shardingValue) {

        LOGGER.info("collection:" + JSON.toJSONString(availableTargetNames) + ",preciseShardingValue:" + JSON.toJSONString(shardingValue));

        int size = availableTargetNames.size();
        for (String each : availableTargetNames) {

            if (each.endsWith(shardingValue.getValue() % size + "")) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }
}
