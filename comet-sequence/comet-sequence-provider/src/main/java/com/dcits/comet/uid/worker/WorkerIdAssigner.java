package com.dcits.comet.uid.worker;


import com.dcits.comet.uid.entity.WorkerNodePo;

import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 10:26
 * @see WorkerIdAssigner
 **/
public interface WorkerIdAssigner {

    long assignWorkerId(final String bizType, final String type);

    void doUpdateNextSegment(final String bizTag, final long nextid, final String type);

    List<WorkerNodePo> getWorkNodePoList(final String bizTag);

    void doUpdatenextSeqCache(WorkerNodePo workerNodePo,String seqCache,long nextid);

    void createSnowWorkerNode(String className, String bizTag);
}
