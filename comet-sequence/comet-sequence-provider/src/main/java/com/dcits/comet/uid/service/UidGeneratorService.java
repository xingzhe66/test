package com.dcits.comet.uid.service;

import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.impl.DefaultUidGenerator;
import com.dcits.comet.uid.impl.LoadingUidGenerator;
import com.dcits.comet.uid.impl.RedisUidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/27 18:37
 **/
@RestController
public class UidGeneratorService {

    @Autowired
    DefaultUidGenerator defaultUidGenerator;

    @Autowired
    LoadingUidGenerator loadingUidGenerator;

    @Autowired
    RedisUidGenerator redisUidGenerator;

    @GetMapping(UidGenerator.UID_DEF_DEF)
    public long getDef() {
        return defaultUidGenerator.getUID();
    }

    @GetMapping(UidGenerator.UID_LOAD_BIZTAG)
    public long getLoad(@PathVariable(required = false) String biztag) {
        return loadingUidGenerator.getUID(biztag);
    }

    @GetMapping(UidGenerator.UID_REIDS_BIZTAG)
    public long getRedis(@PathVariable(required = false) String biztag) {
        return redisUidGenerator.getUID(biztag);
    }

}
