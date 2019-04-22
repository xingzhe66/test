package com.dcits.comet.flow.model;

import com.dcits.comet.commons.data.BaseRequest;
import lombok.Data;

/**
 * @ClassName MessageIn
 * @Author guihj
 * @Date 2019/4/11 15:02
 * @Description TODO
 * @Version 1.0
 **/
@Data
public class MessageIn extends BaseRequest {
    private  String  messageId;
}
