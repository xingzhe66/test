
package com.dcits.comet.parameter.service;

import com.dcits.comet.batch.util.FileUtil;
import com.dcits.comet.commons.utils.BusiUtil;
import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.parameter.constant.ParamConstant;
import com.dcits.comet.parameter.dao.ParamSynDao;
import com.dcits.comet.parameter.exception.ParamSynException;
import com.dcits.comet.parameter.model.ParamInSql;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName DefaultParamSynService
 * @Author huangjjg
 * @Date 2019/5/8 16:26
 * @Description 参数同步服务：参数文件为sql文件（默认）
 * @Version 1.0
 **/
@Slf4j
public class DefaultParamSynService extends AbstractParamSynService<ParamInSql> {


    /**
     * 同步类型 0 增量 1 全量
     */
    private String defaultSynType = ParamConstant.SYN_TYPE_NORT;

    private ParamSynDao paramSynDao;

    private String fileDir = "";

    @Override
    protected void doSynParam(ParamInSql ParamInSql) throws Throwable {
        paramSynDao.exeSql(ParamInSql.getStatement());
    }

    @Override
    protected List<ParamInSql> readParams() throws Throwable{
        if(log.isDebugEnabled()){
            log.debug("starting readParams");
        }
        String currentDate = DateUtil.formatDate(new Date(),DateUtil.PATTERN_SIMPLE_DATE);
        List<ParamInSql> params = new ArrayList<>();
        if(ParamConstant.SYN_TYPE_NORT.equals(defaultSynType)){
            //增量每天一个sql文件
            String fileName = this.fileDir+ File.separator+currentDate+ ParamConstant.SQL_FILE_TAIL;
            Long totalRow = FileUtil.getFileRowCount(fileName,ParamInSql.class);
            params = FileUtil.readFileToList(fileName,ParamInSql.class,0,totalRow.intValue());
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

    private List<ParamInSql> loadParams(String fileDir) {
        List<ParamInSql> paramInSQLS = new ArrayList<ParamInSql>();
        List<File> result = new ArrayList<File>();
        File folder = new File(fileDir);
        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return false;
                }
                if (file.getName().toLowerCase().endsWith(ParamConstant.SQL_FILE_TAIL)) {
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
            Long totalRow = FileUtil.getFileRowCount(file.getAbsolutePath(),ParamInSql.class);
            List<ParamInSql> subList = FileUtil.readFileToList(file.getAbsolutePath(),ParamInSql.class,0,totalRow.intValue());
            paramInSQLS.addAll(subList);
        }
        return paramInSQLS;


    }

    @Override
    public boolean checkResourceIsOk() {
        String path = config.getFileRevPath();
        if(BusiUtil.isNull(path)){
            throw new ParamSynException("参数文件路径未配置!");
        }
        this.fileDir = builDir(path,defaultSynType);
        String currentDate = DateUtil.formatDate(new Date(),DateUtil.PATTERN_SIMPLE_DATE);
        String fileName = this.fileDir+ File.separator+currentDate+ParamConstant.OK_FILE_TAIL;
        return FileUtil.exists(fileName);
    }



    public ParamSynDao getParamSynDao() {
        return paramSynDao;
    }

    public void setParamSynDao(ParamSynDao paramSynDao) {
        this.paramSynDao = paramSynDao;
    }

    public void setDefaultSynType(String defaultSynType) {
        this.defaultSynType = defaultSynType;
    }
}
