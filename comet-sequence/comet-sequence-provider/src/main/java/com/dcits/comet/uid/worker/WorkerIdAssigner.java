package com.dcits.comet.uid.worker;

import com.dcits.comet.uid.entity.WorkerNodePo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 10:26
 * @see WorkerIdAssigner
 **/
public interface WorkerIdAssigner {

    Map<String, WorkerNodePo> keys = new ConcurrentHashMap<>();

    long assignWorkerId(final String bizType);
}
