package com.dcits.comet.uid.provider;

import com.dcits.comet.commons.utils.NetUtils;
import com.dcits.comet.commons.utils.StringUtil;
import com.dcits.comet.uid.entity.WorkerNodePo;
import com.dcits.comet.uid.worker.WorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 13:17
 * @see RedisUidGenerator
 **/
@Slf4j
public class RedisUidGenerator extends DefaultUidGenerator {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected String nextId(String bizTag, final boolean preDate) {
        if (StringUtil.isEmpty(bizTag)) {
            bizTag = NetUtils.getLocalAddress();

        }
        String key = WorkerIdAssigner.keys.get(bizTag).getHostName();
        String hashKey = WorkerIdAssigner.keys.get(bizTag).getBizTag() == null ? key : WorkerIdAssigner.keys.get(bizTag).getBizTag();
        String delta = WorkerIdAssigner.keys.get(bizTag).getStep();
        String uid = String.valueOf(redisTemplate.opsForHash().increment(key, hashKey, Integer.valueOf(delta)));
        return uid;
    }

    @Override
    public String parseUID(String uid) {
        //TODO
        return "redis";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //获取到节点信息，获取到步长信息
        super.afterPropertiesSet();
        //检查redis服务器上是否有workerId的机器节点
        WorkerIdAssigner.keys.forEach((k, v) -> checkPoint(k, v));

    }

    void checkPoint(String string, final WorkerNodePo workerNodePo) {
        log.info("InitializingBean[{},机器号{},当前业务类型{},步长{}]", workerNodePo.getHostName(), workerNodePo.getId(), workerNodePo.getBizTag() == null ? workerNodePo.getHostName() : workerNodePo.getBizTag(), workerNodePo.getStep());
        redisTemplate.opsForHash().putIfAbsent(workerNodePo.getHostName(), workerNodePo.getBizTag() == null ? workerNodePo.getHostName() : workerNodePo.getBizTag(), Integer.valueOf(workerNodePo.getMinSeq()));
    }
}
