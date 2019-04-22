package com.dcits.comet.mq.rocketmq.service;

import com.dcits.comet.commons.Context;
import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.commons.utils.StringUtil;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.mq.rocketmq.contanst.MessageStatus;
import com.dcits.comet.mq.rocketmq.model.MqProducerMsgPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MessageService
 * @Author guihj
 * @Date 2019/4/11 15:49
 * @Description TODO
 * @Version 1.0
 **/
@Component
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class MessageService {
    @Autowired
    DaoSupport daoSupport;

    /**
     * @return com.dcits.comet.mq.model.ProductMsgPo
     * @Author guihj
     * @Description 新增时初始化ProductMsgPo对象
     * @Date 2019/4/11 22:59
     * @Param []
     **/
    public MqProducerMsgPo messageConvert() {
        String  messageId = StringUtil.getUUID();
        MqProducerMsgPo productMsgPo = new MqProducerMsgPo();
        productMsgPo.setMqMsgId(messageId);
        productMsgPo.setCreateTime(DateUtil.getCurrentDate());
        productMsgPo.setFlowId(Context.getInstance().getFlowId());
        productMsgPo.setStatus(MessageStatus.STATUS_INIT);
        productMsgPo.setSeqNo(getMessageSize() + 1);
        return productMsgPo;
    }

    /**
     * @return void
     * @Author guihj
     * @Description 新增消息
     * @Date 2019/4/11 23:03
     * @Param [productMsgPo]
     **/
    public void saveMessage(MqProducerMsgPo productMsgPo) {
        daoSupport.insert(productMsgPo);
    }


    /**
     * @return int
     * @Author guihj
     * @Description 获取当前flowId已有消息条数
     * @Date 2019/4/15 14:44
     * @Param []
     **/
    public int getMessageSize() {
        int messageSize = 0;
        MqProducerMsgPo productMsgPo = new MqProducerMsgPo();
        productMsgPo.setFlowId(Context.getInstance().getFlowId());
        List<MqProducerMsgPo> productMsgPos = daoSupport.selectList(productMsgPo);
        if (productMsgPos != null) {
            messageSize = productMsgPos.size();
        }
        return messageSize;
    }


    /**
     * @return java.util.List<com.dcits.comet.mq.model.ProductMsgPo>
     * @Author guihj
     * @Description 获取状态为1的消息
     * @Date 2019/4/14 21:13
     * @Param []
     **/
    public List<MqProducerMsgPo> getMsgStatusOne() {
        List<MqProducerMsgPo> productMsgPos = new ArrayList<>();
        MqProducerMsgPo productMsgPo = new MqProducerMsgPo();
        productMsgPo.setStatus(MessageStatus.STATUS_INIT);
        productMsgPos = daoSupport.selectList(productMsgPo);
        return productMsgPos;
    }

    /**
     * @return java.util.List<com.dcits.comet.mq.model.ProductMsgPo>
     * @Author guihj
     * @Description 获取状态为2的消息
     * @Date 2019/4/14 21:13
     * @Param []
     **/
    public List<MqProducerMsgPo> getMsgStatusTwo() {
        List<MqProducerMsgPo> productMsgPos = new ArrayList<>();
        MqProducerMsgPo productMsgPo = new MqProducerMsgPo();
        productMsgPo.setStatus(MessageStatus.STATUS_SEND);
        productMsgPos = daoSupport.selectList(productMsgPo);
        return productMsgPos;
    }


}
