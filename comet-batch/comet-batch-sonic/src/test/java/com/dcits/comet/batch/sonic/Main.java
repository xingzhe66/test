package com.dcits.comet.batch.sonic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/14 19:27
 **/
public class Main {
    public static void main(String[] args) {
        //HashMap<String, String> hashMap = new HashMap<>();
        //hashMap.put("params", "{lastRunDate: 20190418,runDate: 20190419,nextRunDate: 20190411}");
        //System.out.println(JsonUtil.jsonToMap(hashMap.get("params")));
        DBatchStep ibstep = new DBatchStep();
        Type type = ((ParameterizedType) ibstep.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        int num = ibstep.getCountNum(null,null);
        System.out.println(type);
        System.out.println(num);
    }
}
