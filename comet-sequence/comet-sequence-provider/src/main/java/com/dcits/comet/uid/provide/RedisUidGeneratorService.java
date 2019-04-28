package com.dcits.comet.uid.provide;

import com.dcits.comet.uid.factory.RedisUidGeneratorFactory;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/26 10:09
 **/
@Deprecated
public class RedisUidGeneratorService {

    public Long getKeyBybiztag(@PathVariable(required = false) String biztag) {
        return RedisUidGeneratorFactory.getInstance().getKey(biztag);
    }

}
