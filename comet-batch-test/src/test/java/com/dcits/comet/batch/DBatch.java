package com.dcits.comet.batch;

import com.dcits.comet.dao.DaoSupport;
import com.dcits.yunyun.entity.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service("dBatch")
//@StepScope
public class DBatch extends AbstractBatch<SysLog,SysLog> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DBatch.class);

    @Resource
    public DaoSupport daoSupport;



    @Override
    public List getPageData(int offset, int pageSize) {
        SysLog sysLog=new SysLog();
        sysLog.setId(2000000000002l);
        List list=new ArrayList();
        list.add(daoSupport.selectByPrimaryKey(sysLog));
        return list;
    }

    @Override
    public SysLog process(SysLog item) {
        return item;

    }

    @Override
    public void write(List<SysLog> item) {
        LOGGER.info("write D....."+item.get(0));
      //  LOGGER.info("write D....."+name);
    }

}
