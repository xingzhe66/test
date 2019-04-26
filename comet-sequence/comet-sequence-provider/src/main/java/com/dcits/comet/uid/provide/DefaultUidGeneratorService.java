package com.dcits.comet.uid.provide;

import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.factory.SnowflakeUidGeneratorFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultUidGeneratorService {


    @GetMapping(value = UidGenerator.UID_DEF_BIZTAG)
    public Long getKeyBybiztag(@PathVariable(required = false) String biztag) {
        return SnowflakeUidGeneratorFactory.getInstance().getKey(biztag);
    }

    @GetMapping(value = UidGenerator.UID_DEF_DEF)
    public Long getKey() {
        return SnowflakeUidGeneratorFactory.getInstance().getKey();
    }

}
