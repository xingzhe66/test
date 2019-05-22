package com.dcits.comet.dbsharding.route.dbSharding;

import com.dcits.comet.dbsharding.route.Route;
import io.shardingsphere.api.HintManager;
import io.shardingsphere.core.hint.HintManagerHolder;

/**
 * @ClassName DbShardingHintManager
 * @Author leijian
 * @Date 2019/5/21 9:52
 * @Description TODO
 * @Version 1.0
 **/
public class DbShardingHintManager implements Route {

    public static DbShardingHintManager getInstance(String dbIndex, String tableId) {
        HintManager hintManager = HintManager.getInstance();
        hintManager.setDatabaseShardingValue(dbIndex);
        return new DbShardingHintManager();
    }

    @Override
    public void close() {
        HintManagerHolder.clear();
    }
}
