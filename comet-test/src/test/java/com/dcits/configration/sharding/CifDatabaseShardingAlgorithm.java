package com.dcits.configration.sharding;

import com.alibaba.fastjson.JSON;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public final class CifDatabaseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CifDatabaseShardingAlgorithm.class);
    @Override
    public String doSharding(final Collection<String> availableTargetNames, final PreciseShardingValue<String> shardingValue) {
//        if(null!=HintManagerHolder.getDatabaseShardingValue(HintManagerHolder.DB_TABLE_NAME)){
//            List<String> list = (List<String>) ((ListShardingValue) HintManagerHolder.getDatabaseShardingValue(HintManagerHolder.DB_TABLE_NAME).get()).getValues();
//            if(list.size()>0) return list.get(0);
//        }
//        if(null!=Context.getInstance().getDefinedMap().get("node")) return (String) Context.getInstance().getDefinedMap().get("node");
//

        LOGGER.info("collection:" + JSON.toJSONString(availableTargetNames) + ",preciseShardingValue:" + JSON.toJSONString(shardingValue));

        int size = availableTargetNames.size();
        for (String each : availableTargetNames) {

            if (each.endsWith(Long.valueOf(shardingValue.getValue()) % size + "")) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }

}
