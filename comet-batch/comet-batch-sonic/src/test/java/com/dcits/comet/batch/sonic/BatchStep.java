package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.AbstractSegmentStep;
import com.dcits.comet.batch.Segment;
import com.dcits.comet.batch.helper.HintManagerHelper;
import com.dcits.comet.batch.helper.JobParameterHelper;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.sonic.entity.WorkerNodePo;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dbsharding.route.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/23 18:03
 **/
@Slf4j
@Service("BatchStep")
public class BatchStep extends AbstractSegmentStep<WorkerNodePo, WorkerNodePo> {

    @Autowired
    DaoSupport daoSupport;


    @Override
    public List<String> getNodeList(BatchContext batchContext) {
        return HintManagerHelper.getNodeList(WorkerNodePo.class);
    }


    public List<Segment> getSegmentList(BatchContext batchContext, String node) {
        Map<String, Object> map = new HashMap();
        map.put("PORT", 2);
        Route route = null;
        try {
            route = HintManagerHelper.getInstance(WorkerNodePo.class, node);
            return daoSupport.selectSegmentList(WorkerNodePo.class.getName() + ".genPostSelect", map, 200);
        } catch (Exception e) {
            log.error("{}", e);
            return null;
        } finally {
            route.close();
        }
    }

    @Override
    public void preBatchStep(BatchContext batchContext) {
        log.info("GenPostStep.......preBatchStep");
        log.info("GenPostStep.......JobId=" + JobParameterHelper.get("jobId"));//从启动参数获取jobId
    }




    @Override
    public void writeChunk(BatchContext batchContext, List<WorkerNodePo> workerNodePoList) {
        daoSupport.update(WorkerNodePo.class.getName() + ".updateBatch", workerNodePoList);
        log.info("writeChunk() end");
    }
}
