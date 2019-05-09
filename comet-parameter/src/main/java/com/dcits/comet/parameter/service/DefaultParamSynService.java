
package com.dcits.comet.parameter.service;

import com.dcits.comet.batch.util.FileUtil;
import com.dcits.comet.commons.utils.BusiUtil;
import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.parameter.config.ParamSynConfig;
import com.dcits.comet.parameter.constant.ParamConstant;
import com.dcits.comet.parameter.dao.ParamSynDao;
import com.dcits.comet.parameter.model.ParamInSQL;
import com.dcits.comet.parameter.service.AbstractParamSynService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @ClassName DefaultParamSynService
 * @Author huangjjg
 * @Date 2019/5/8 16:26
 * @Description 默认参数同步方式 DefaultParamSynService
 * @Version 1.0
 **/
@Slf4j
public class DefaultParamSynService extends AbstractParamSynService<ParamInSQL> {


    //同步类型 0 增量 1 全量
    private String synType = "0";

    private ParamSynDao paramSynDao;

    private String fileDir = "";

    @Override
    protected void doSynParam(ParamInSQL paramInSQL) throws Throwable {
        paramSynDao.exeSql(paramInSQL.getStatement());
    }

    @Override
    protected List<ParamInSQL> readParams() throws Throwable{
        if(log.isDebugEnabled()){
            log.debug("starting readParams");
        }
        String currentDate = DateUtil.formatDate(new Date(),DateUtil.PATTERN_SIMPLE_DATE);
        String fileName = this.fileDir+ File.separator+currentDate+ ParamConstant.SQL_FILE_TAIL;
        Long totalRow = FileUtil.getFileRowCount(fileName,ParamInSQL.class);
        List<ParamInSQL> params = FileUtil.readFileToList(fileName,ParamInSQL.class,0,totalRow.intValue());
        return params;
    }

    @Override
    public boolean checkResourceIsOk() {
        String path = config.getFileRevPath();
        if(BusiUtil.isNull(path)){
            throw new RuntimeException("参数文件路径未配置!");
        }
        this.fileDir = builDir(path);
        String currentDate = DateUtil.formatDate(new Date(),DateUtil.PATTERN_SIMPLE_DATE);
        String fileName = this.fileDir+ File.separator+currentDate+ParamConstant.OK_FILE_TAIL;
        return FileUtil.exists(fileName);
    }

    /**
     * 构造文件目录位置
     * @param path
     * @return
     */
    private String builDir(String path) {
        StringBuffer sbf = new StringBuffer(path);
        String appName = config.getAppName();
        if(appName.contains("-"+ParamConstant.SYS_NAME_AC+"-")){
            sbf.append(File.separator).append(ParamConstant.SYS_NAME_AC);
        }else if(appName.contains("-"+ParamConstant.SYS_NAME_RB+"-")){
            sbf.append(File.separator).append(ParamConstant.SYS_NAME_RB);
        }else if(appName.contains("-"+ParamConstant.SYS_NAME_BO+"-")){
            sbf.append(File.separator).append(ParamConstant.SYS_NAME_BO);
        }else if(appName.contains("-"+ParamConstant.SYS_NAME_PF+"-")){
            sbf.append(File.separator).append(ParamConstant.SYS_NAME_PF);
        }else if(appName.contains("-"+ParamConstant.SYS_NAME_CIF+"-")){
            sbf.append(File.separator).append(ParamConstant.SYS_NAME_CIF);
        }else{
            return path;
        }
        if(ParamConstant.SYN_TYPE_INCREMENT.equals(synType)){
            sbf.append(File.separator).append(ParamConstant.SYN_TYPE_INCREMENT_DIR);
        }else{
            sbf.append(File.separator).append(ParamConstant.SYN_TYPE_FULL_DIR);
        }
        return sbf.toString();
    }

    public ParamSynDao getParamSynDao() {
        return paramSynDao;
    }

    public void setParamSynDao(ParamSynDao paramSynDao) {
        this.paramSynDao = paramSynDao;
    }

    public void setSynType(String synType) {
        this.synType = synType;
    }
}
