package com.dcits.comet.commons.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 *
 * @author ChengLiang
 */
public class BeanUtil {

    public static <T> void setValue(final T targetObject, final String fieldName, final Object targetValue) {
        final Field field = ReflectionUtils.findField(targetObject.getClass(), fieldName);
        field.setAccessible(true);
        ReflectionUtils.setField(field, targetObject, targetValue);
    }
}
