package com.dcits.comet.dbsharding;

import com.dcits.comet.dao.Route;
import io.shardingsphere.api.HintManager;
import io.shardingsphere.core.hint.HintManagerHolder;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/21 9:54
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
