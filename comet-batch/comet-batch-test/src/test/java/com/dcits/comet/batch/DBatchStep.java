package com.dcits.comet.batch;

import com.dcits.comet.batch.holder.JobContextHolder;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.yunyun.entity.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service("dBatchStep")
public class DBatchStep extends AbstractSegmentStep<SysLog, SysLog> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DBatchStep.class);

    @Resource
    public DaoSupport daoSupport;

    public List getPageList(BatchContext batchContext,int offset, int pageSize,String node) {
        SysLog sysLog = new SysLog();
        sysLog.setId(2000000000002l);
        List list = new ArrayList();
        list.add(daoSupport.selectOne(sysLog));
        return list;
    }

    @Override
    public SysLog process(BatchContext batchContext,SysLog item) {
        return item;

    }

    @Override
    public void writeChunk(BatchContext batchContext,List<SysLog> item) {
        LOGGER.info("write D....." + item.get(0));
        // JobContextHolder.getInstance().put("1","hahaha","1");
        LOGGER.info("write JobParameterHelper....." + JobContextHolder.getInstance().get("1", "hahaha"));
        //  LOGGER.info("write D....."+name);
    }


    @Override
    public List<SysLog> getPageList(BatchContext batchContext, Comparable start, Comparable end, String node) {
        return null;
    }
}
