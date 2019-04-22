package com.dcits.comet.flow.constant;
/**
 * @Author guihj
 * @Description 消息生产者发送消息结果状态
 * @Date 2019/4/15 9:30
 * @Param
 * @return
 **/
public enum MsgSendStatus {
    /**
     * @Param  消息发送成功
     *
     **/
    SEND_OK,
    /**
     * @Param  消息发送成功，但是服务器刷盘超时，消息已经进入服务器队列，
     *        只有此时服务器宕机，消息才会丢失
     *
     **/
    FLUSH_DISK_TIMEOUT,
    /**
     * @Param 消息发送成功，但是服务器同步到 Slave 时超时，
     *        消息已经进入服务器队列，只有此时服务器宕机，消息才会丢失
     *
     **/
    FLUSH_SLAVE_TIMEOUT,
    /**
     * @Param  消息发送成功，但是此时 slave 不可用，消息已经进入服务器队列，
     *         只有此时服务器宕机，消息才会丢
     *
     **/
    SLAVE_NOT_AVAILABLE;

    private MsgSendStatus() {
    }

}
