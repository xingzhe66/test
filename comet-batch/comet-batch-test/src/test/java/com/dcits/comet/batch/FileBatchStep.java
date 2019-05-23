package com.dcits.comet.batch;

/*
@Service("fileBatchStep")
public class FileBatchStep extends AbstractBStep<FileLog,SysLog> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DBatchStep.class);

    @Resource
    public DaoSupport daoSupport;

    private static BeanCopier beanCopier = BeanCopier.create(FileLog.class, SysLog.class, false);

    @Override
    public List getPageList(BatchContext batchContext, int offset, int pageSize,String node) {
        List<FileLog> listdata =
                FileUtil.readFileToList("C:\\javaprojects\\microservice\\comet\\comet-batch-test\\src\\test\\resources\\file.log", FileLog.class, offset, pageSize);
        return listdata;
    }

    @Override
    public SysLog process(BatchContext batchContext,FileLog item) {
        SysLog item1=new SysLog();
        beanCopier.copy(item,item1,null);
        return item1;

    }

    @Override
    public void writeChunk(BatchContext batchContext,List<SysLog> item) {
        LOGGER.info("write D....."+item.get(0));
        //  LOGGER.info("write D....."+name);
        LOGGER.info("write JobParameterHelper....."+ JobContextHolder.getInstance().get("1","hahaha"));
    }

    @Override
    public void writeOne(BatchContext batchContext,SysLog item) {

    }
}
*/
