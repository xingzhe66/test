package com.dcits.comet.batch;

import com.dcits.comet.dao.Route;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/21 10:14
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
