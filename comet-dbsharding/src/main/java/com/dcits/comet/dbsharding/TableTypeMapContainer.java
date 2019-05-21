package com.dcits.comet.dbsharding;

import com.dcits.comet.commons.utils.YamlUtil;
import com.dcits.comet.dao.annotation.TableType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName TableTypeMapContainer
 * @Author guihj
 * @Date 2019/3/15 15:04
 * @Description TODO
 * @Version 1.0
 **/
@Slf4j
public class TableTypeMapContainer {

    private static Multimap<String, String> multiMap = ArrayListMultimap.create();
//    @Autowired
//    ClassScaner classScaner;

    private static List<String> packageNames;


    public static final String COMET_YML = "comet.yml";

    public static final String GATEWAY_API_PACKAGE_NAMES = "entityPackageNames";

    static {
        try {
            packageNames = YamlUtil.getListProperty(COMET_YML,GATEWAY_API_PACKAGE_NAMES);
           // packageNames=YamlUtil.getProperty(COMET_YML,GATEWAY_API_PACKAGE_NAMES);
        } catch (Exception e) {
            log.info(COMET_YML+"配置信息读取失败！");
            e.printStackTrace();
        }
        new TableTypeMapContainer();
    }


//
//    @Value("${entity.sanner.packageName}")
//    private String packageName;

    private TableTypeMapContainer(){
        init();
    }
    private void init() {
        ClassScaner classScaner=new ClassScaner();
        for (String packageName : packageNames) {
            List<Class<?>> classNameList = classScaner.doScanPackage(packageName);
            if (classNameList != null) {
                getTableTypeMap(classNameList);
            }
            log.info("entity扫描包：" + packageName + "结束！");
        }
        log.info("=============================================");
        log.info("=========entity扫描结束！====================");
        log.info("=============================================");
    }
    /* *
        * @Author guihj
        * @Description //获取表名和类型
        * @Date 2019/3/15 17:46
        * @Param 包下类集合
        * @return  表名和类型集合
        **/
    private void getTableTypeMap(List<Class<?>> classNameList) {
      //  Multimap<String, String> multiMap = ArrayListMultimap.create();
        for (Class className:classNameList) {
            boolean hasAnnotation = className.isAnnotationPresent(TableType.class);
            if (hasAnnotation) {
                TableType[] tableTypes = (TableType[])className.getAnnotationsByType(TableType.class);
                if (tableTypes != null) {
                    for (TableType tableType : tableTypes) {
                        if("PARAM".equals(tableType.value().toString())){
                            multiMap.put("param",tableType.name());
                        }
                        if("LEVEL".equals(tableType.value().toString())){
                            multiMap.put("level",tableType.name());
                        }
                        if("UPRIGHT".equals(tableType.value().toString())){
                            multiMap.put("upright",tableType.name());
                        }
                    }
                }
            }
        }
//        return multiMap;
    }


    public static Multimap<String, String> getMultiMap() {
        return multiMap;
    }

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        init();
//    }

//    public static void main(String[] args) {
//        new TableTypeMapContainer();
//    }
}

