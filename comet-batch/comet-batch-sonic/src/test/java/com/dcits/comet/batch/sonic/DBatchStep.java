package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.AbstractBStep;
import com.dcits.comet.batch.helper.JobParameterHelper;
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
    public void preBatchStep(BatchContext batchContext) {
        log.info("GenPostStep.......preBatchStep");
        log.info("GenPostStep.......JobId=" + JobParameterHelper.get("jobId"));//从启动参数获取jobId
    }

    @Override
    public List getPageList(BatchContext batchContext, int offset, int pageSize, String node) {
        List list = new ArrayList();
        for (int i = 0; i < 3; i++) {
            WorkerNodePo workerNodePo = new WorkerNodePo();
            workerNodePo.setId(i);
            workerNodePo.setHostName(NetUtils.getLocalAddress());
            list.add(workerNodePo);
        }
        return list;
    }

    @Override
    public void writeChunk(BatchContext batchContext, List<WorkerNodePo> BatchGlTranHists) {
        log.info("writeChunk");
    }


    @Override
    public void writeOne(BatchContext batchContext, WorkerNodePo item) {
        log.info("writezOne");
    }
}
