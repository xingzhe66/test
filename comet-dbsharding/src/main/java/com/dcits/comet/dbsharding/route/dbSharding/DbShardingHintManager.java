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

    private static HintManager hintManager;

    public static DbShardingHintManager getInstance() {
        hintManager = HintManager.getInstance();
        return new DbShardingHintManager();
    }

    @Override
    public void buildDbIndex(String dbIndex, String tableId) {
        hintManager.setDatabaseShardingValue(dbIndex);
    }

    @Override
    public void close() {
        HintManagerHolder.clear();
    }
}
