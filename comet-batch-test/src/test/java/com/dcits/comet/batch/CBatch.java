package com.dcits.comet.batch;

import com.dcits.comet.dao.DaoSupport;
import com.dcits.yunyun.entity.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("cBatch")
//@StepScope
public class CBatch extends AbstractBatch<SysLog,SysLog> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CBatch.class);
    @Resource
    public DaoSupport daoSupport;
    @Override
    public List getPageData(int offset, int pageSize) {
        SysLog sysLog=new SysLog();
        List list=daoSupport.selectList(SysLog.class.getName()+".extendSelect",sysLog,offset/pageSize+1, pageSize);
        return list;
    }
    @Override
    public SysLog process(SysLog item) {
        return item;
    }
    @Override
    public void write(List<SysLog> item) {
        LOGGER.info("write C....."+item.get(0));
    }

}
