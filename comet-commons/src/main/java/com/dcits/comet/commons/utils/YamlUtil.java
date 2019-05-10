package com.dcits.comet.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @Author wangyun
 * @Date 2019/3/27
 * yaml工具类
 **/
@Slf4j
public class YamlUtil {

    /**
     * @author wangyun
     * @date 2019/3/27
     * @description 读取一个list配置
     */
    public static List<String> getListProperty(String ymlName,String propertyName){
        Map map = getMap(ymlName);
        return  (List<String>) map.get(propertyName);
    }

    /**
     * @author wangyun
     * @date 2019/3/27
     * @description
     */
    public static String getStringProperty(String ymlName,String propertyName){
        Map map = getMap(ymlName);
        return  (String) map.get(propertyName);
    }

    /**
     * @author wangyun
     * @date 2019/3/27
     * @description
     */
    public static<T> T getProperty(String ymlName,String propertyName){
        Map map = getMap(ymlName);
        return  (T) map.get(propertyName);
    }

    private static Map getMap(String ymlName) {
        Yaml yaml = new Yaml();
        URL url = YamlUtil.class.getClassLoader().getResource(ymlName);
        if (url == null) {
            log.info(ymlName + "配置信息不存在！");
        }
        //comet.yaml文件中的配置数据，然后转换为obj，
        Map map = null;
        try {
            map = yaml.load(new FileInputStream(url.getFile()));
        } catch (FileNotFoundException e) {
            log.error("context",e);
        }
        if (null == map) {
            log.info(ymlName + "配置信息读取失败！");
        }
        log.info(map.toString());
        return map;
    }
}
