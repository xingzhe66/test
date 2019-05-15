package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.AbstractTStep;
import com.dcits.comet.batch.param.BatchContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @param
 * @author leijian
 * @Description put任务测试
 * @date 2019/5/14 14:03
 * @return
 **/
@Service("syncBatchAcSubject")
@Slf4j
public class SyncBatchAcSubject extends AbstractTStep {

    @Override
    public void exe(BatchContext batchContext) throws Throwable {
        //workerNodePoRepository.deleteAll();
        log.info("batchContext{}交易成功", batchContext);

    }
}

