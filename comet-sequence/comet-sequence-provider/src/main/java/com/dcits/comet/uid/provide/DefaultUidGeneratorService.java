package com.dcits.comet.uid.provide;

import com.dcits.comet.uid.factory.SnowflakeUidGeneratorFactory;

@Deprecated
public class DefaultUidGeneratorService {

    public Long getKey() {
        return SnowflakeUidGeneratorFactory.getInstance().getKey();
    }

}
