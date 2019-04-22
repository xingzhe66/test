package com.dcits.comet.mq.rocketmq.contanst;

/**
 * @ClassName MessageStatus
 * @Author guihj
 * @Date 2019/4/15 9:23
 * @Description 消息状态
 * @Version 1.0
 **/
public class MessageStatus {
    /**
     * @Param  初始状态  1
     **/
    public static final int  STATUS_INIT=1;
    /**
     * @Param  发送状态  2
     **/
    public static final int   STATUS_SEND = 2;
    /**
     * @Param  发送成功状态  3
     **/
    public static final int   STATUS_SUCCESS=3;
    /**
     * @Param  异常状态 4
     **/
    public static final  int  STATUS_EXCEPTION = 4;

}
