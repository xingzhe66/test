package com.dcits.comet.commons.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leijian
 * @Description //TODO
 * @date 2019/5/20 14:30
 **/
public class ClassLoaderUtils {
    private static final Map<String, Class<?>> classCache = new ConcurrentHashMap();

    public ClassLoaderUtils() {
    }

    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        Class<?> clazz = null;
        if (classCache.containsKey(className)) {
            return (Class) classCache.get(className);
        } else {
            clazz = getClassLoader().loadClass(className);
            if (null != clazz) {
                classCache.put(className, clazz);
            }

            return clazz;
        }
    }

    public static Object newInstance(String classImpl) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return loadClass(classImpl).newInstance();
    }

    public static Object newInstance(String classImpl, Class<?>[] pType, Object[] obj) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return loadClass(classImpl).getConstructor(pType).newInstance(obj);
    }

    public static ClassLoader getClassLoader() {
        return ClassLoaderUtils.class.getClassLoader();
    }
}
