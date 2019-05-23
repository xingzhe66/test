package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.AbstractRowNumStep;
import com.dcits.comet.batch.helper.HintManagerHelper;
import com.dcits.comet.batch.helper.JobParameterHelper;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.sonic.entity.WorkerNodePo;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dbsharding.route.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/14 16:32
 **/
@Service("dBatchStep")
@Slf4j
public class DBatchStep extends AbstractRowNumStep<WorkerNodePo, WorkerNodePo> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DaoSupport daoSupport;

    @Override
    public void preBatchStep(BatchContext batchContext) {
        log.info("GenPostStep.......preBatchStep");
        log.info("GenPostStep.......JobId=" + JobParameterHelper.get("jobId"));//从启动参数获取jobId
    }

    @Override
    public List getPageList(BatchContext batchContext, int offset, int pageSize, String node) {
        log.info("getPageList() begin ");
        WorkerNodePo workerNodePo = new WorkerNodePo();
        Map glTranHistMap = new HashMap();
        glTranHistMap.put("PORT", "2");
        return daoSupport.selectList(WorkerNodePo.class.getName() + ".genPostSelect", glTranHistMap, 1, pageSize);
    }

    @Override
    public int getCountNum(BatchContext batchContext, String node) {
        WorkerNodePo workerNodePo = new WorkerNodePo();
        workerNodePo.setPort("2");
        Route route = null;
        try {
            route = HintManagerHelper.getInstance(WorkerNodePo.class, node);
            return daoSupport.count(workerNodePo);
        } catch (Exception e) {
            return 0;
        } finally {
            route.close();
        }
    }

    @Override
    public List<String> getNodeList(BatchContext batchContext) {
        return HintManagerHelper.getNodeList(WorkerNodePo.class);
    }

    @Override
    public void writeChunk(BatchContext batchContext, List<WorkerNodePo> workerNodePoList) {
        daoSupport.update(WorkerNodePo.class.getName() + ".updateBatch", workerNodePoList);
        log.info("writeChunk() end");
    }
}
