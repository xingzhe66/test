package com.dcits.comet.uid;

import com.dcits.comet.uid.provider.service.LoadingUidGeneratorFactory;
import com.dcits.comet.uid.provider.service.SnowflakeUidGeneratorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UidGeneratorController {

    //@Autowired
    //DaoSupport daoSupport;

    @GetMapping("/default")
    public String def() {
        long uid = SnowflakeUidGeneratorFactory.getInstance().getKey();
        return String.valueOf(uid);
    }

    @GetMapping("/loading")
    public String loading() {
        long uid = LoadingUidGeneratorFactory.getInstance().getKey("cif.seqNo");
        return String.valueOf(uid);
    }
}
