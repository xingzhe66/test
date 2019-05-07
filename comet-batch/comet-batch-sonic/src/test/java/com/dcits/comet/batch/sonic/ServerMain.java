package com.dcits.comet.batch.sonic;

import com.dcits.sonic.api.config.ClientProfile;
import com.dcits.sonic.executor.SonicExecutorServer;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 10:14
 **/
public class ServerMain {
    public static void main(String[] args) {
        //获取实例
        SonicExecutorServer executorServer = SonicExecutorServer.getInstance();
        //初始化参数
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSchedulerAddresses("127.0.0.1:8897");
        //设置参数
        //启动
        executorServer.startup();
    }
}