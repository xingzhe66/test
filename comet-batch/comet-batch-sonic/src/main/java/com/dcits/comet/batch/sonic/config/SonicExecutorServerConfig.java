package com.dcits.comet.batch.sonic.config;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 16:07
 **/
public class SonicExecutorServerConfig {
    //SystemConfig配置
    private String appGroup;
    private String appName;
    private int reportExecutorSize = 100;
    private int stepGroupSize = 500;
    private String beanGeneratorType;
    private String stepLockType;
    private int processorInitThreadSize = 200;
    private int processorMaxThreadSize = 200;
    private int ProcessorThreadQueue = 1000;



}
