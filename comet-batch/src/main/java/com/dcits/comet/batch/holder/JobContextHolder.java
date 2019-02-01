package com.dcits.comet.batch.holder;

import org.springframework.batch.core.jsr.launch.JsrJobOperator;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *单jvm范围内的JobContext
 */
public class JobContextHolder {

    private ConcurrentHashMap<String,ConcurrentHashMap<String,String>> map;

    private static volatile JobContextHolder jobContextHolder;

    private JobContextHolder() {
        synchronized (JobContextHolder.class) {
            if(this.map == null) {
                this.map =new ConcurrentHashMap<String,ConcurrentHashMap<String,String>>();
            }
        }
    }

    public static JobContextHolder getInstance() {
        if (jobContextHolder == null) {
            synchronized (JobContextHolder.class) {
                if (jobContextHolder == null) {
                    jobContextHolder = new JobContextHolder();
                }
            }
        }
        return jobContextHolder;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getMap() {
        return this.map;
    }
    //todo 多线程执行process时会有线程安全问题
    //todo 对于多线程执行部分，不允许put
    public void put(String jobid,String key,String value) {

        ConcurrentHashMap<String, String> submap =this.map.get(jobid);

        if(null==submap){

            submap = new ConcurrentHashMap<String, String>();

        }

        submap.put(key,value);
        this.map.put(jobid,submap);

    }


    public String get(String jobid,String key) {

        ConcurrentHashMap<String, String> submap =this.map.get(jobid);

        if(null==submap){

            return null;

        }

        return submap.get(key);

    }

    public void clear(String jobid) {

        ConcurrentHashMap<String, String> submap =this.map.get(jobid);

        if(null==submap){
            return;
        }
        submap.clear();

    }
}
