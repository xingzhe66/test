package com.dcits.comet.uid.repository;

import com.dcits.comet.uid.entity.WorkerNodePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/25 17:43
 **/
@Repository
public interface WorkerNodePoRepository extends JpaRepository<WorkerNodePo, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    WorkerNodePo findByHostNameAndBizTag(String hostName, String bizTag);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<WorkerNodePo> findByBizTag(String bizTag);

    @Override
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<WorkerNodePo> findAll();

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<WorkerNodePo> findByType(String type);

    @Override
    <S extends WorkerNodePo> S saveAndFlush(S entity);

}
