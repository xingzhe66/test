package com.dcits.comet.batch;

import com.dcits.comet.batch.holder.JobContextHolder;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.yunyun.entity.SysLog;
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
    public void preBatchStep(){
        LOGGER.info("preBatchStep.......cBatchStep");
    }

    @Override
    public List getPageList(int offset, int pageSize) {
        SysLog sysLog=new SysLog();
        List list=daoSupport.selectList(SysLog.class.getName()+".extendSelect",sysLog,offset/pageSize+1, pageSize);
        return list;
    }
    @Override
    public SysLog process(SysLog item) {
        return item;
    }
    @Override
    public void writeChunk(List<SysLog> item) {
        LOGGER.info("write C....."+item.get(0));
        JobContextHolder.getInstance().put("1","hahaha","1111111");
    }
    @Override
    public void afterBatchStep(){
        LOGGER.info("afterBatchStep.......cBatchStep");
    }

}
