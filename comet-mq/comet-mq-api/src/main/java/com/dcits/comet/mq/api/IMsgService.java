package com.dcits.comet.mq.api;


import java.util.List;

/**
 * @interfaceName IMsgService
 * @Author guihj
 * @Date 2019/4/11 22:04
 * @Description TODO
 * @Version 1.0
 **/
public interface IMsgService {
    /**
     * @Author guihj
     * @Description //发送消息
     * @Date 2019/4/15 14:28
     * @Param []
     * @return void
     **/
    public String send(RocketMessage rocketMessage) throws Exception;

    /**
     * @Author guihj
     * @Description 修改消息状态为2  开始发送
     * @Date 2019/4/15 14:29
     * @Param []
     * @return int
     **/
    public int updateMsgStatusSend();
    /**
     * @Author guihj
     * @Description 修改消息状态为3  发送成功
     * @Date 2019/4/15 14:29
     * @Param []
     * @return int
     **/
    public int updateMessageSuccess(RocketMessage message);


    /**
     * @Author guihj
     * @Description 修改消息状态为4  发送失败，出现异常
     * @Date 2019/4/15 14:29
     * @Param []
     * @return int
     **/
    public int updateMsgStatusException();


    /**
     * @return
     * @Author guihj
     * @Description 真实发送消息时，根据flowId在表中获取消息集合
     * @Date 2019/4/11 23:00
     * @Param
     **/
    public List<RocketMessage>  getMessageByFloWId();
}
