package com.dcits.comet.batch.service.test.batchstep;

import com.dcits.comet.batch.AbstractBatchStep;
import com.dcits.comet.batch.helper.JobParameterHelper;
import com.dcits.comet.batch.holder.JobContextHolder;
import com.dcits.comet.batch.param.BatchContextManager;
import com.dcits.comet.batch.service.test.entity.SysLog;
import com.dcits.comet.dao.DaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("cBatchStep")
//@StepScope
public class CBatchStep extends AbstractBatchStep<SysLog,SysLog> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CBatchStep.class);
    @Resource
    public DaoSupport daoSupport;

    @Override
    public void preBatchStep(){
        LOGGER.info("preBatchStep.......cBatchStep");
//        JobParameterHelper.get("jobId");
//        BatchContextManager.getInstance().put(JobParameterHelper.get("jobId"),"context","preBatchStep");
    }

    @Override
    public List getPageList(int offset, int pageSize) {
        SysLog sysLog=new SysLog();
        List list=daoSupport.selectList(SysLog.class.getName()+".extendSelect",sysLog,offset/pageSize+1, pageSize);
//        LOGGER.info((String)BatchContextManager.getInstance().get(JobParameterHelper.get("jobId"),"context"));
//
//        BatchContextManager.getInstance().put(JobParameterHelper.get("jobId"),"context","getPageList");
        return list;
    }
    @Override
    public SysLog process(SysLog item) {
        LOGGER.info("process C....." + item.toString());
//        LOGGER.info((String)BatchContextManager.getInstance().get(JobParameterHelper.get("jobId"),"context"));
//
//        BatchContextManager.getInstance().put(JobParameterHelper.get("jobId"),"context","process");
        return item;
    }
    @Override
    public void writeChunk(List<SysLog> item) {
        for (SysLog a:item) {
            LOGGER.info("write C....." + a.toString());
        }
//        LOGGER.info((String)BatchContextManager.getInstance().get(JobParameterHelper.get("jobId"),"context"));
//
//        BatchContextManager.getInstance().put(JobParameterHelper.get("jobId"),"context","writeChunk");
    }
    @Override
    public void afterBatchStep(){
        LOGGER.info("afterBatchStep.......cBatchStep");
        LOGGER.info((String)BatchContextManager.getInstance().get(JobParameterHelper.get("jobId"),"context"));

        BatchContextManager.getInstance().put(JobParameterHelper.get("jobId"),"context","afterBatchStep");
    }

}
