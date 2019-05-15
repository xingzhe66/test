package com.dcits.comet.uid.worker;

import com.dcits.comet.commons.exception.UidGenerateException;
import com.dcits.comet.commons.utils.NetUtils;
import com.dcits.comet.uid.context.UidGeneratorContext;
import com.dcits.comet.uid.entity.WorkerNodePo;
import com.dcits.comet.uid.repository.WorkerNodePoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 1.获取机器序列号，支持直接去网卡IP地址或者从配置文件加载；2.获取流水号生成的步长
 *
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 10:26
 * @see DisposableWorkerIdAssigner
 **/
@Slf4j
public class DisposableWorkerIdAssigner {

    WorkerNodePoRepository workerNodePoRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public WorkerNodePo findByHostNameAndBizTag(String bizTag) {
        return workerNodePoRepository.findByHostNameAndBizTag(NetUtils.getLocalAddress(), bizTag);
    }

    public WorkerNodePo update(WorkerNodePo workerNodePo) {
        return workerNodePoRepository.saveAndFlush(workerNodePo);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public WorkerNodePo selectByBizTagAndTypeTodoForUpdateOrInsert(String type, String bizTag) {
        //获取所有节点的信息
        List<WorkerNodePo> dbTagsType = workerNodePoRepository.findAll();
        List<WorkerNodePo> dbTags = dbTagsType.stream().filter(Objects::nonNull).filter(v -> bizTag.equalsIgnoreCase(v.getBizTag())).collect(Collectors.toList());
        log.info("{}", dbTags);
        if (dbTags == null || dbTags.isEmpty()) {
            //插入并且返回
            WorkerNodePo workerNodePo = new WorkerNodePo();
            workerNodePo.setHostName(NetUtils.getLocalAddress());
            workerNodePo.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
            workerNodePo.setType(type);
            workerNodePo.setLaunchDate(LocalDateTime.now());
            workerNodePo.setModified(LocalDateTime.now());
            workerNodePo.setCreated(LocalDateTime.now());
            workerNodePo.setBizTag(bizTag);
            workerNodePo.setMinSeq(UidGeneratorContext.UID_DEF_MIN_SEQ);
            //默认加载200个
            workerNodePo.setMaxSeq(UidGeneratorContext.UID_DEF_MAX_SEQ);
            workerNodePo.setStep(UidGeneratorContext.UID_DEF_STEP);
            workerNodePo.setCurrSeq("0");
            workerNodePo.setCountSeq("0");
            workerNodePo.setMiddleId(UidGeneratorContext.UID_MIDDLEID);
            workerNodePo.setSeqCycle(UidGeneratorContext.UID_NOT_CYCLE);
            workerNodePo.setSeqCache("0");
            workerNodePo.setCacheCount("0");
            workerNodePo = workerNodePoRepository.save(workerNodePo);
            return workerNodePo;
        } else {
            //校验每个bizTag配置的流水号类型只能是一种
            HashMap map = new HashMap();
            dbTags.stream().forEach(v -> {
                if (!type.equalsIgnoreCase(v.getType())) {
                    throw new UidGenerateException("999999", "节点" + v.getHostName() + "配置的" + v.getBizTag() + "与当前的流水生成类型不一致" + v.getType() + "请求的是" + type);
                }
                map.put(v.getType(), bizTag);
            });
            if (map.size() != 1) {
                throw new UidGenerateException("999999", "流水号生成的bizTag配置有重复项" + map.keySet());
            }
            //如果已经有当前的值，取最大值
            WorkerNodePo workerNodePo = dbTags.stream().filter(v -> bizTag.equals(v.getBizTag()) && StringUtils.isNotEmpty(v.getMaxSeq())).sorted(Comparator.comparing(WorkerNodePo::getMaxSeq).reversed()).findFirst().get();
            log.info("获取的当前类型[{}]的最大数据库节点信息{}", bizTag, workerNodePo);
            //数据节点不同NetUtils.getLocalAddress()如果当前节点的信息不存在，需要插入；如果已存在进行更新操作
            WorkerNodePo needUpdate = dbTags.stream().filter(v -> bizTag.equals(v.getBizTag()) && StringUtils.equals(NetUtils.getLocalAddress(), v.getHostName())).sorted(Comparator.comparing(WorkerNodePo::getMaxSeq).reversed()).findFirst().orElseGet(() -> {
                log.info("存在相同的序列类型{}，在{}节点生成新的实例", bizTag, NetUtils.getLocalAddress());
                WorkerNodePo workerNodePo1 = new WorkerNodePo();
                workerNodePo1.setHostName(NetUtils.getLocalAddress());
                workerNodePo1.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
                workerNodePo1.setType(type);
                workerNodePo1.setLaunchDate(LocalDateTime.now());
                workerNodePo1.setCreated(LocalDateTime.now());
                workerNodePo1.setBizTag(bizTag);
                workerNodePo1.setCurrSeq("0");
                workerNodePo1.setCountSeq("0");
                workerNodePo1.setMiddleId(UidGeneratorContext.UID_MIDDLEID);
                workerNodePo1.setSeqCycle(UidGeneratorContext.UID_NOT_CYCLE);
                workerNodePo1.setSeqCache("0");
                workerNodePo1.setCacheCount("0");
                return workerNodePo1;
            });
            log.info("更新前[{}]的PO节点信息{}", bizTag, workerNodePo);
            needUpdate.setModified(LocalDateTime.now());
            needUpdate.setMinSeq(workerNodePo.getMaxSeq());
            //默认加载200个
            needUpdate.setMaxSeq(String.valueOf(Long.valueOf(workerNodePo.getMaxSeq()) + Long.valueOf(UidGeneratorContext.UID_DEF_MAX_SEQ)));
            needUpdate.setStep(workerNodePo.getStep());
            workerNodePo = workerNodePoRepository.saveAndFlush(needUpdate);
            log.info("更新后PO值{}", workerNodePo);
            return workerNodePo;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<WorkerNodePo> findByTypeIfNullInsert(String type) {
        List<WorkerNodePo> dbTags = new LinkedList<WorkerNodePo>();
        dbTags = workerNodePoRepository.findByType(type);
        if (dbTags == null || dbTags.isEmpty()) {
            //插入默认类型type并且返回
            WorkerNodePo workerNodePo = new WorkerNodePo();
            workerNodePo.setHostName(NetUtils.getLocalAddress());
            workerNodePo.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
            workerNodePo.setType(type);
            workerNodePo.setLaunchDate(LocalDateTime.now());
            workerNodePo.setModified(LocalDateTime.now());
            workerNodePo.setCreated(LocalDateTime.now());
            workerNodePo.setBizTag(type);
            workerNodePo.setMinSeq(UidGeneratorContext.UID_DEF_MIN_SEQ);
            //默认加载200个
            workerNodePo.setMaxSeq(UidGeneratorContext.UID_DEF_MAX_SEQ);
            workerNodePo.setStep(UidGeneratorContext.UID_DEF_STEP);
            workerNodePo.setCurrSeq("0");
            workerNodePo.setCountSeq("0");
            workerNodePo.setMiddleId(UidGeneratorContext.UID_MIDDLEID);
            workerNodePo.setSeqCycle(UidGeneratorContext.UID_NOT_CYCLE);
            workerNodePo.setSeqCache("0");
            workerNodePo.setCacheCount("0");
            workerNodePo = workerNodePoRepository.save(workerNodePo);
            dbTags.add(workerNodePo);
        } else {
            if (UidGeneratorContext.UID_DEF_DEF.equalsIgnoreCase(type)) {
                WorkerNodePo needUpdate = dbTags.stream().filter(v -> type.equals(v.getBizTag()) && StringUtils.equals(NetUtils.getLocalAddress(), v.getHostName())).sorted(Comparator.comparing(WorkerNodePo::getMaxSeq).reversed()).findFirst().orElseGet(() -> {
                    log.info("存在相同的序列类型{}，在{}节点生成新的实例", type, NetUtils.getLocalAddress());
                    WorkerNodePo workerNodePo1 = new WorkerNodePo();
                    workerNodePo1.setHostName(NetUtils.getLocalAddress());
                    workerNodePo1.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
                    workerNodePo1.setType(type);
                    workerNodePo1.setLaunchDate(LocalDateTime.now());
                    workerNodePo1.setCreated(LocalDateTime.now());
                    workerNodePo1.setBizTag(type);
                    workerNodePo1.setCurrSeq("0");
                    workerNodePo1.setCountSeq("0");
                    workerNodePo1.setMiddleId(UidGeneratorContext.UID_MIDDLEID);
                    workerNodePo1.setSeqCycle(UidGeneratorContext.UID_NOT_CYCLE);
                    workerNodePo1.setSeqCache("0");
                    workerNodePo1.setCacheCount("0");
                    workerNodePo1.setModified(LocalDateTime.now());
                    workerNodePo1.setMinSeq(UidGeneratorContext.UID_DEF_MIN_SEQ);
                    workerNodePo1.setMaxSeq(UidGeneratorContext.UID_DEF_MAX_SEQ);
                    workerNodePo1.setStep(UidGeneratorContext.UID_DEF_STEP);
                    return workerNodePo1;
                });
                log.info("更新前[{}]的PO节点信息{}", type, needUpdate);
                WorkerNodePo workerNodePo = workerNodePoRepository.saveAndFlush(needUpdate);
                log.info("更新前[{}]的PO节点信息{}", type, workerNodePo);
                dbTags.add(workerNodePo);
            }
        }
        return dbTags;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<WorkerNodePo> findByBizTag(String bizTag) {
        return workerNodePoRepository.findByBizTag(bizTag);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<WorkerNodePo> findAll() {
        return workerNodePoRepository.findAll();
    }

    public WorkerNodePo save(String type, String bizTag) {
        WorkerNodePo workerNodePo = new WorkerNodePo();
        workerNodePo.setHostName(NetUtils.getLocalAddress());
        workerNodePo.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
        workerNodePo.setType(type);
        workerNodePo.setLaunchDate(LocalDateTime.now());
        workerNodePo.setCreated(LocalDateTime.now());
        workerNodePo.setModified(LocalDateTime.now());
        workerNodePo.setBizTag(bizTag);
        workerNodePo.setMinSeq("");
        workerNodePo.setMaxSeq("");
        workerNodePo.setCurrSeq("");
        workerNodePo.setCountSeq("");
        workerNodePo.setMiddleId(UidGeneratorContext.UID_MIDDLEID);
        workerNodePo.setSeqCycle(UidGeneratorContext.UID_NOT_CYCLE);
        workerNodePo.setSeqCache("");
        workerNodePo.setCacheCount("");
        return workerNodePoRepository.save(workerNodePo);
    }

    public void setWorkerNodePoRepository(WorkerNodePoRepository workerNodePoRepository) {
        this.workerNodePoRepository = workerNodePoRepository;
    }
}
