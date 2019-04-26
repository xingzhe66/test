package com.dcits.comet.uid.provide;

import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.factory.SnowflakeUidGeneratorFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultUidGeneratorService {

    @PostMapping(value = UidGenerator.UID_DEF_DEF)
    public Long getKey() {
        return SnowflakeUidGeneratorFactory.getInstance().getKey();
    }

}
