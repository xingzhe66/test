package com.dcits.comet.mq.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author wangyun
 * @Date 2019/4/8
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class Message implements Serializable {

    private Long messageId;

    private String msgText;
}
