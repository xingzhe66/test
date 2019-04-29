package com.dcits.comet.batch.reader;

import com.dcits.comet.batch.IBStep;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.util.BatchContextTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;
/**
 * @author wangyun
 * @date 2019/3/21
 * @description Reader
 */
public class Reader extends AbstractPagingReader {
    protected static final Logger logger = LoggerFactory.getLogger(Reader.class);

    private IBStep batchStep;

    private int beginIndex=0;

    private String node;

    private Object lock = new Object();


    public void setBeginIndex(int beginIndex){
        this.beginIndex=beginIndex;
    }

    public void setBatchStep(IBStep batchStep) {
        this.batchStep = batchStep;
    }

    public void setNode(String node) {
        this.node = node;
    }
    @Override
    protected void doReadPage(int offset, int pageSize) {
        if (results == null) {
            results = new CopyOnWriteArrayList();
        } else {
            results.clear();
        }
        BatchContext batchContext= BatchContextTool.getBatchContext();
        results.addAll(batchStep.getPageList(batchContext,offset, pageSize,node));
        logger.info("doReadPage完毕,results大小为:"+results.size());
    }

    public void init()  {

        try {
            jumpToItem(beginIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
