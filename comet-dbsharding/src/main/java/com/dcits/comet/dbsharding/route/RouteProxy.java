package com.dcits.comet.dbsharding.route;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName RouteProxy
 * @Author leijian
 * @Date 2019/5/21 9:52
 * @Description TODO
 * @Version 1.0
 **/
public class RouteProxy {

    private Route route;

    public RouteProxy(final Route route) {
        this.route = route;
    }

    public Route getProxy() {
        return (Route) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Route.class},
                new RouteInvocationHandler());
    }

    private class RouteInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(route, args);
        }
    }
}
