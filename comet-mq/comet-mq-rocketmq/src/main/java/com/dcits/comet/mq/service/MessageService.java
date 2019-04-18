package com.dcits.comet.mq.service;

import com.dcits.comet.commons.Context;
import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.mq.contanst.MessageStatus;
import com.dcits.comet.mq.model.ProductMsgPo;
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
    public ProductMsgPo messageConvert() {
//        SnowflakeUidGeneratorFactory.getInstance().getKey();
        //TODO 需要用sequence模块生成
        Long messageId = com.dcits.comet.commons.utils.DateUtil.longRandomId();
        ProductMsgPo productMsgPo = new ProductMsgPo();
        productMsgPo.setProductedMsgId(messageId);
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveMessage(ProductMsgPo productMsgPo) {
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
        ProductMsgPo productMsgPo = new ProductMsgPo();
        productMsgPo.setFlowId(Context.getInstance().getFlowId());
        List<ProductMsgPo> productMsgPos = daoSupport.selectList(productMsgPo);
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
    public List<ProductMsgPo> getMsgStatusOne() {
        List<ProductMsgPo> productMsgPos = new ArrayList<>();
        ProductMsgPo productMsgPo = new ProductMsgPo();
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
    public List<ProductMsgPo> getMsgStatusTwo() {
        List<ProductMsgPo> productMsgPos = new ArrayList<>();
        ProductMsgPo productMsgPo = new ProductMsgPo();
        productMsgPo.setStatus(MessageStatus.STATUS_SEND);
        productMsgPos = daoSupport.selectList(productMsgPo);
        return productMsgPos;
    }


}
