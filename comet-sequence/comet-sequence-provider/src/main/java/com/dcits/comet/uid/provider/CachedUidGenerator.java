package com.dcits.comet.uid.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 11:07
 * @see CachedUidGenerator
 **/
@Slf4j
public class CachedUidGenerator extends DefaultUidGenerator implements DisposableBean {
    /**
     * @return void
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 11:13
     * @Param []
     **/
    @Override
    public void afterPropertiesSet() throws Exception {
        // initialize workerId & bitsAllocator
        super.afterPropertiesSet();

        // initialize RingBuffer & RingBufferPaddingExecutor
        this.initRingBuffer();
        log.info("Initialized RingBuffer successfully.");
    }

    /**
     * @return long
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 11:12
     * @Param []
     **/
    @Override
    public String getUID() {
        return "0";
    }

    @Override
    public String parseUID(String uid) {
        return "cache";
    }

    /**
     * @return void
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 11:13
     * @Param []
     **/
    @Override
    public void destroy() throws Exception {
        //TODO spring容器销毁时保存每个节点当前流水号信息，避免浪费
        log.info("DisposableBean{}", this.getClass().getName());

    }

    /**
     * @return void
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/27 11:11
     * @Param []
     **/
    private void initRingBuffer() {
    }


}
