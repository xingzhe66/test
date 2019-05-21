package com.dcits;

import com.dcits.comet.batch.AbstractBStep;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.yunyun.entity.CifBusinessPo;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/21 10:40
 **/
public class Batch extends AbstractBStep<CifBusinessPo, CifBusinessPo> {


    DaoSupport daoSupport;

    public void setDaoSupport(DaoSupport daoSupport) {
        this.daoSupport = daoSupport;
    }

    @Override
    public int getCountNum(BatchContext batchContext, String node) {
        return daoSupport.count(new CifBusinessPo());
    }

}
