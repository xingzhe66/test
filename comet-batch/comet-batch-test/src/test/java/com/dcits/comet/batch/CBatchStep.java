package com.dcits.comet.batch;

/*
@Service("cBatchStep")
public class CBatchStep extends AbstractBStep<SysLog,SysLog> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CBatchStep.class);
    @Resource
    public DaoSupport daoSupport;

    public void preBatchStep(BatchContext batchContext){
        LOGGER.info("preBatchStep.......cBatchStep");
    }

    public List getPageList(BatchContext batchContext,int offset, int pageSize,String node) {
        SysLog sysLog=new SysLog();
        List list=daoSupport.selectList(SysLog.class.getName()+".extendSelect",sysLog,offset/pageSize+1, pageSize);
        return list;
    }
    public SysLog process(BatchContext batchContext,SysLog item) {
        return item;
    }
    public void writeChunk(BatchContext batchContext,List<SysLog> item) {
        LOGGER.info("write C....."+item.get(0));
        JobContextHolder.getInstance().put("1","hahaha","1111111");
    }

    public void writeOne(BatchContext batchContext,SysLog item) {

    }

    public void afterBatchStep(BatchContext batchContext){
        LOGGER.info("afterBatchStep.......cBatchStep");
    }

}
*/
