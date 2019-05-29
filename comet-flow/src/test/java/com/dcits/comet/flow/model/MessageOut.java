package com.dcits.comet.flow.model;

import com.dcits.comet.rpc.api.model.BaseResponse;
import lombok.Data;

/**
 * @ClassName MessageIn
 * @Author guihj
 * @Date 2019/4/11 15:02
 * @Description TODO
 * @Version 1.0
 **/
@Data
public class MessageOut  extends BaseResponse {
   private  String  messageTxt;
}
