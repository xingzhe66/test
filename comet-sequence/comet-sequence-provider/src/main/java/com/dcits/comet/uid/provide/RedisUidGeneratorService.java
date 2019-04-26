package com.dcits.comet.uid.provide;

import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.factory.RedisUidGeneratorFactory;
import com.dcits.comet.uid.factory.SnowflakeUidGeneratorFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/26 10:09
 **/
@RestController
public class RedisUidGeneratorService {

    @GetMapping(value = UidGenerator.UID_REIDS_BIZTAG)
    public Long getKeyBybiztag(@PathVariable(required = false) String biztag) {
        return RedisUidGeneratorFactory.getInstance().getKey(biztag);
    }

    @GetMapping(value = UidGenerator.UID_REIDS_DEF)
    public Long getKey() {
        return SnowflakeUidGeneratorFactory.getInstance().getKey();
    }
}
