
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
import java.io.FileFilter;
import java.util.ArrayList;
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
        List<ParamInSQL> params = new ArrayList<>();
        if(ParamConstant.SYN_TYPE_INCREMENT.equals(synType)){
            //增量每天一个sql文件
            String fileName = this.fileDir+ File.separator+currentDate+ ParamConstant.SQL_FILE_TAIL;
            Long totalRow = FileUtil.getFileRowCount(fileName,ParamInSQL.class);
            params = FileUtil.readFileToList(fileName,ParamInSQL.class,0,totalRow.intValue());
        }else{
            //全量 以表名称为文件名有多个
            params = loadParams(fileDir);
        }

        return params;
    }

    /**
     * 遍历目标目录下，加载参数
     * @param fileDir
     * @return
     */

    private List<ParamInSQL> loadParams(String fileDir) {
        List<ParamInSQL> paramInSQLS = new ArrayList<>();
        List<File> result = new ArrayList<File>();
        File folder = new File(fileDir);
        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return false;
                }
                if (file.getName().toLowerCase().contains(".sql")) {
                    return true;
                }
                return false;
            }
        });
        if (subFolders != null) {
            for (File file : subFolders) {
                if (file.isFile()) {
                    // 如果是文件则将文件添加到结果列表中
                    result.add(file);
                } else {
                    // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                    //result.addAll(searchFiles(file, keyword));
                }
            }
        }
        for(File file : result){
            Long totalRow = FileUtil.getFileRowCount(file.getAbsolutePath(),ParamInSQL.class);
            List<ParamInSQL> subList = FileUtil.readFileToList(file.getAbsolutePath(),ParamInSQL.class,0,totalRow.intValue());
            paramInSQLS.addAll(subList);
        }
        return paramInSQLS;


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
