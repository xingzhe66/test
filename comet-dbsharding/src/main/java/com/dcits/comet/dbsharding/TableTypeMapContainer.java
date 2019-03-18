package com.dcits.comet.dbsharding;

import com.dcits.comet.dao.annotation.TableType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName TableTypeMapContainer
 * @Author guihj
 * @Date 2019/3/15 15:04
 * @Description TODO
 * @Version 1.0
 **/
@Component
public class TableTypeMapContainer implements InitializingBean {

    private static Multimap<String, String> multiMap = ArrayListMultimap.create();
    @Autowired
    ClassScaner classScaner;

    @Value("${entity.sanner.packageName}")
    private String packageName;

    private TableTypeMapContainer(){
//        init();
    }
    private void init() {
      //  ClassScaner classScaner=new ClassScaner();
        List<Class<?>> classNameList=classScaner.doScanPackage(packageName);
        if(classNameList!=null){
            multiMap=getTableTypeMap(classNameList);
        }
    }
    /* *
        * @Author guihj
        * @Description //获取表名和类型
        * @Date 2019/3/15 17:46
        * @Param 包下类集合
        * @return  表名和类型集合
        **/
    private static Multimap<String, String> getTableTypeMap(List<Class<?>> classNameList) {
        Multimap<String, String> multiMap = ArrayListMultimap.create();
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
        return multiMap;
    }


    public static Multimap<String, String> getMultiMap() {
        return multiMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    public static void main(String[] args) {
        new TableTypeMapContainer();
    }
}

