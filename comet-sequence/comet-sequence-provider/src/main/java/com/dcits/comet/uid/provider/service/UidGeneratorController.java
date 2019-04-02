package com.dcits.comet.uid.provider.service;

import com.dcits.comet.uid.impl.DefaultUidGenerator;
import com.dcits.comet.uid.impl.RedisUidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 13:20
 * @see UidGeneratorController
 **/
@RestController
@Slf4j
public class UidGeneratorController {

    @Resource
    private DefaultUidGenerator defaultUidGenerator;

    @Resource
    private RedisUidGenerator redisUidGenerator;

    //@Autowired
    //DaoSupport daoSupport;

    @GetMapping("/default")
    public String[] test() {
        long uid = defaultUidGenerator.getUID();
        String strings[] = new String[2];
        strings[0] = String.valueOf(uid);
        strings[1] = defaultUidGenerator.parseUID(uid);
        return strings;
    }

    @GetMapping("/redis")
    public String redis() {
        long uid = redisUidGenerator.getUID();
        return String.valueOf(uid);
    }
}
