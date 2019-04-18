package com.dcits.comet.mq.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author wangyun
 * @Date 2019/4/8
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class RocketMessage extends Message {

    private String topic;

    private String tag;

    private String msgText;


}
