package com.dcits.comet.uid;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/30 16:44
 **/
public class UidGeneratorProxy {

    private UidGenerator uidGenerator;

    /**
     * @param uidGenerator
     * @return
     * @author leijian
     * @Description //TODO
     * @ate 2019/3/30 16:45
     **/
    public UidGeneratorProxy(final UidGenerator uidGenerator) {
        this.uidGenerator = uidGenerator;
    }

    public UidGenerator getProxy() {
        return (UidGenerator) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{UidGenerator.class},
                new UidInvocationHandler());
    }

    private class UidInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(uidGenerator, args);
        }
    }
}
