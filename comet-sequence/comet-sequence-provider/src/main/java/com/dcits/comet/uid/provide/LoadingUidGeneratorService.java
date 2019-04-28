package com.dcits.comet.uid.provide;

import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.factory.LoadingUidGeneratorFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/26 10:09
 **/
@Deprecated
public class LoadingUidGeneratorService {

    public Long getKeyBybiztag(@PathVariable(required = false) String biztag) {
        return LoadingUidGeneratorFactory.getInstance().getKey(biztag);
    }


}
