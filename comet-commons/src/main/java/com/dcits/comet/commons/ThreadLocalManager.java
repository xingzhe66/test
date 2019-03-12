package com.dcits.comet.commons;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-26 15:52
 * @Version 1.0
 **/
public class ThreadLocalManager {

    public static ThreadLocal<Map<String, Object>> threadContext = ThreadLocal.withInitial(() -> new HashMap<>());

    public static final String BUSI_CONTEXT_KEY = "BUSI_CONTEXT";

    /**
     * 产生字符串序列(长度:32)
     *
     * @return String 32位字符串序列
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("\\-", "").toUpperCase();
    }

    /**
     * 设置交易上下文
     *
     * @param tranContext
     */
    public static <T> void setBusiContext(T tranContext) {
        threadContext.get().put(BUSI_CONTEXT_KEY, tranContext);
    }

    /**
     * 获取交易上下文
     *
     * @param <T>
     * @return
     */
    public static <T> T getBusiContext() {
        return (T) threadContext.get().get(BUSI_CONTEXT_KEY);
    }

    /**
     * 清空线程
     */
    public static void remove() {
        threadContext.remove();
    }
}
