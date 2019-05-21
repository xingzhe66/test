package com.dcits.comet.dbsharding.route.dbp;

import com.dcits.comet.commons.utils.StringUtil;
import com.dcits.comet.dbsharding.route.Route;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName DbpHintManager
 * @Author leijian
 * @Date 2019/5/21 9:52
 * @Description TODO
 * @Version 1.0
 **/
public class DbpHintManager implements Route {

    private static DbpHintManager hintManager;

    private static final ThreadLocal<RouteParameters> routeParams = new ThreadLocal<RouteParameters>() {
        @Override
        protected RouteParameters initialValue() {
            return null;
        }
    };


    public static DbpHintManager getInstance() {

        return new DbpHintManager();
    }

    public static RouteParameters getRouteParameters() {
        return routeParams.get();
    }

    public static void setRouteParameters(RouteParameters routeParameters) {
        routeParams.set(routeParameters);
    }

    @Override
    public void buildDbIndex(String dbIndex, String tableId) {
        RouteParameters routeParameters = new RouteParameters();
        routeParameters.setGroupId(dbIndex);
        if (StringUtil.isNotEmpty(tableId)) {
            List<String> list = new LinkedList();
            list.add(tableId);
            routeParameters.setTargetTables(list);
        }
        setRouteParameters(routeParameters);
    }

    @Override
    public void close() {
        routeParams.remove();
    }
}
