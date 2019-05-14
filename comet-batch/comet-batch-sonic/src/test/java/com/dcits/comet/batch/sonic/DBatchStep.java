package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.AbstractBStep;
import com.dcits.comet.batch.holder.JobContextHolder;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.commons.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/14 16:32
 **/
@Service("dBatchStep")
@Slf4j
public class DBatchStep extends AbstractBStep<WorkerNodePo, WorkerNodePo> {

    @Override
    public List getPageList(BatchContext batchContext, int offset, int pageSize, String node) {
        List list = new ArrayList();
        for (int i = 0; i < 100; i++) {
            WorkerNodePo workerNodePo = new WorkerNodePo();
            workerNodePo.setId(i);
            workerNodePo.setHostName(NetUtils.getLocalAddress());
            list.add(workerNodePo);
        }
        return list;
    }

    @Override
    public WorkerNodePo process(BatchContext batchContext, WorkerNodePo item) {
        return item;

    }

    @Override
    public void writeChunk(BatchContext batchContext, List<WorkerNodePo> item) {
        log.info("write D....." + item.get(0));
        // JobContextHolder.getInstance().put("1","hahaha","1");
        log.info("write JobParameterHelper....." + JobContextHolder.getInstance().get("1", "hahaha"));
        //  LOGGER.info("write D....."+name);
    }

    @Override
    public void writeOne(BatchContext batchContext, WorkerNodePo item) {

    }
}
