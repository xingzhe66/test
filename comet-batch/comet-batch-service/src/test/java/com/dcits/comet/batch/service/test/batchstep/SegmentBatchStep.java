package com.dcits.comet.batch.service.test.batchstep;

import com.dcits.comet.batch.AbstractRowNumStep;
import com.dcits.comet.batch.AbstractSegmentStep;
import com.dcits.comet.batch.Segment;
import com.dcits.comet.batch.helper.HintManagerHelper;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.service.test.entity.SysLog;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dbsharding.route.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wangyun
 * @Date 2019/5/23
 **/
@Slf4j
@Component("segmentBatchStep")
public class SegmentBatchStep extends AbstractSegmentStep<SysLog,SysLog> {
    @Resource
    public DaoSupport daoSupport;

    @Override
    public List<String> getNodeList(BatchContext batchContext) {
        return HintManagerHelper.getNodeList(SysLog.class);
    }

    @Override
    public List<Segment> getSegmentList(BatchContext batchContext, String node) {
        Map<String,Object> map=new HashMap();
        Route route = null;
        try {
            route = HintManagerHelper.getInstance(SysLog.class, node);
            return daoSupport.selectList(SysLog.class.getName()+".extendSelect2",map);
        } catch (Exception e) {
            return null;
        } finally {
            route.close();
        }
    }

    @Override
    public void preBatchStep(BatchContext batchContext){
        log.info("preBatchStep.......cBatchStep");}


    @Override
    public List<SysLog> getPageList(BatchContext batchContext, Comparable start, Comparable end, String node) {
        Map<String,Object> map=new HashMap();
        map.put("beginIndex",start);
        map.put("endIndex",end);
        List list=daoSupport.selectList(SysLog.class.getName()+".extendSelect2",map);
        return list;
    }

    @Override
    public SysLog process(BatchContext batchContext,SysLog item) {
        log.info("process C....." + item.toString());
        return item;
    }
    @Override
    public void writeChunk(BatchContext batchContext,List<SysLog> item) {
        for (SysLog a:item) {
            log.info("write C....." + a.toString());
        }
    }
    @Override
    public void afterBatchStep(BatchContext batchContext){
        log.info("afterBatchStep.......cBatchStep");
    }


}
