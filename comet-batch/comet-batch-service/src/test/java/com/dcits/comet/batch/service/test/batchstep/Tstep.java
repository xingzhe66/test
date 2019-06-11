package com.dcits.comet.batch.service.test.batchstep;

import com.dcits.comet.batch.AbstractTStep;
import com.dcits.comet.batch.param.BatchContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wangyun
 * @Date 2019/6/4
 **/
@Service("tstep")
public class Tstep extends AbstractTStep {
    @Override
    public void exe(BatchContext batchContext) throws InterruptedException {
        List n=null;
      //  Thread.sleep(100000000);
     //   n.size();

    }
}
