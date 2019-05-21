package com.dcits.comet.dao.interceptor;

public class RouteCondition {

    private static final ThreadLocal<RouteParameters> routeParams = new ThreadLocal<RouteParameters>() {
                                                                      @Override
                                                                      protected RouteParameters initialValue() {
                                                                          return null;
                                                                      }
                                                                  };

    public static RouteParameters newRouteParameters() {
        RouteParameters routeParameters = new RouteParameters();
        setRouteParameters(routeParameters);
        return routeParameters;
    }

    public static RouteParameters getRouteParameters() {
        return routeParams.get();
    }

    public static void setRouteParameters(RouteParameters routeParameters) {
        routeParams.set(routeParameters);
    }

    public static void clear() {
        routeParams.remove();
    }

}
