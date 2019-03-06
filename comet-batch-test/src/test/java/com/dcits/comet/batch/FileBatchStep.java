package com.dcits.comet.batch;

import com.dcits.comet.batch.holder.JobContextHolder;
import com.dcits.comet.batch.util.FileUtil;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.yunyun.entity.FileLog;
import com.dcits.yunyun.entity.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("fileBatchStep")
public class FileBatchStep extends AbstractBStep<FileLog,SysLog> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DBatchStep.class);

    @Resource
    public DaoSupport daoSupport;

    private static BeanCopier beanCopier = BeanCopier.create(FileLog.class, SysLog.class, false);

    @Override
    public List getPageList(int offset, int pageSize) {
        List<FileLog> listdata =
                FileUtil.readFileToList("C:\\javaprojects\\microservice\\comet\\comet-batch-test\\src\\test\\resources\\file.log", FileLog.class, offset, pageSize);
        return listdata;
    }

    @Override
    public SysLog process(FileLog item) {
        SysLog item1=new SysLog();
        beanCopier.copy(item,item1,null);
        return item1;

    }

    @Override
    public void writeChunk(List<SysLog> item) {
        LOGGER.info("write D....."+item.get(0));
        //  LOGGER.info("write D....."+name);
        LOGGER.info("write JobParameterHelper....."+ JobContextHolder.getInstance().get("1","hahaha"));
    }
}
