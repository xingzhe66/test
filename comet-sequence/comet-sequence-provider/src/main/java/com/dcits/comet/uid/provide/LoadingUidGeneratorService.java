package com.dcits.comet.uid.provide;

import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.factory.LoadingUidGeneratorFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/26 10:09
 **/
@RestController
public class LoadingUidGeneratorService {

    @GetMapping(value = UidGenerator.UID_LOAD_BIZTAG)
    public Long getKeyBybiztag(@PathVariable(required = false) String biztag) {
        return LoadingUidGeneratorFactory.getInstance().getKey(biztag);
    }

    @GetMapping(value = UidGenerator.UID_LOAD_DEF)
    public Long getKey() {
        return LoadingUidGeneratorFactory.getInstance().getKey();
    }
}
