package com.dcits.comet.batch.service.test.batchstep;

import com.dcits.comet.batch.AbstractBStep;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.service.test.entity.SysLog;
import com.dcits.comet.dao.DaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("cBatchStep")
//@StepScope
public class CBatchStep extends AbstractBStep<SysLog,SysLog> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CBatchStep.class);
    @Resource
    public DaoSupport daoSupport;

    @Override
    public void preBatchStep(BatchContext batchContext){
        LOGGER.info("preBatchStep.......cBatchStep");
//        JobParameterHelper.get("jobId");
//        BatchContextManager.getInstance().put(JobParameterHelper.get("jobId"),"context","preBatchStep");
    }

    @Override
    public List getPageList(BatchContext batchContext,int offset, int pageSize,String node) {
        SysLog sysLog=new SysLog();
        List list=daoSupport.selectList(SysLog.class.getName()+".extendSelect",sysLog,offset/pageSize+1, pageSize);
//        LOGGER.info((String)BatchContextManager.getInstance().get(JobParameterHelper.get("jobId"),"context"));
//
//        BatchContextManager.getInstance().put(JobParameterHelper.get("jobId"),"context","getPageList");
        return list;
    }
    @Override
    public SysLog process(BatchContext batchContext,SysLog item) {
        LOGGER.info("process C....." + item.toString());
//        LOGGER.info((String)BatchContextManager.getInstance().get(JobParameterHelper.get("jobId"),"context"));
//
//        BatchContextManager.getInstance().put(JobParameterHelper.get("jobId"),"context","process");
        return item;
    }
    @Override
    public void writeChunk(BatchContext batchContext,List<SysLog> item) {
        for (SysLog a:item) {
            LOGGER.info("write C....." + a.toString());
        }
//        LOGGER.info((String)BatchContextManager.getInstance().get(JobParameterHelper.get("jobId"),"context"));
//
//        BatchContextManager.getInstance().put(JobParameterHelper.get("jobId"),"context","writeChunk");
    }
    @Override
    public void afterBatchStep(BatchContext batchContext){
        LOGGER.info("afterBatchStep.......cBatchStep");
//        LOGGER.info((String)BatchContextManager.getInstance().get(JobParameterHelper.get("jobId"),"context"));
//
//        BatchContextManager.getInstance().put(JobParameterHelper.get("jobId"),"context","afterBatchStep");
    }

    @Override
    public void writeOne(BatchContext batchContext, SysLog item) {

    }
}
