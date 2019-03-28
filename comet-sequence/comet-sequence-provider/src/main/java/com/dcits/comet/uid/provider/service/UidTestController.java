package com.dcits.comet.uid.provider.service;

import com.dcits.comet.uid.provider.CachedUidGenerator;
import com.dcits.comet.uid.provider.DefaultUidGenerator;
import com.dcits.comet.uid.provider.RedisUidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 13:20
 * @see UidTestController
 **/
@RestController
@Slf4j
public class UidTestController {

    @Resource
    private DefaultUidGenerator defaultUidGenerator;

    @Resource
    private CachedUidGenerator cachedUidGenerator;

    @Resource
    private RedisUidGenerator redisUidGenerator;

    //@Autowired
    //DaoSupport daoSupport;

    @GetMapping("/default")
    public String[] test() {
        String uid = defaultUidGenerator.getUID(true);
        String strings[] = new String[2];
        strings[0] = String.valueOf(uid);
        strings[1] = defaultUidGenerator.parseUID(uid);
        return strings;
    }

    @GetMapping("/cache")
    public String[] cache() {
        String uid = cachedUidGenerator.getUID();
        String strings[] = new String[2];
        strings[0] = String.valueOf(uid);

        return strings;
    }

    @GetMapping("/redis")
    public String redis() {
        String uid = redisUidGenerator.getUID();
        return uid;
    }
}
