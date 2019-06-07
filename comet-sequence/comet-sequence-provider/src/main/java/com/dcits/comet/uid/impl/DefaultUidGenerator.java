package com.dcits.comet.uid.impl;

import com.dcits.comet.commons.exception.UidGenerateException;
import com.dcits.comet.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/25 18:31
 **/
@Slf4j
public class DefaultUidGenerator implements UidGenerator {

    @Override
    public long getUID() throws UidGenerateException {
        try {
            return getUID(null);
        } catch (Exception e) {
            log.error("Generate unique id exception.{}", e);
            throw new UidGenerateException("999999", "流水号生成异常");
        }
    }


    @Override
    public long getUID(String bizTag) throws UidGenerateException {
        return nextId(bizTag);
    }

    @Override
    public List<Long> getUIDList(long value) throws UidGenerateException {
        Assert.isTrue(value <= 0L, "获取流水号的个数不能小于或者等于0");
        return getUIDList(null, value);
    }

    @Override
    public List<Long> getUIDList(String bizTag, long value) throws UidGenerateException {
        Assert.isTrue(value <= 0L, "获取流水号的个数不能小于或者等于0");
        List uidList = new LinkedList();
        for (long i = 0L; i < value; i++) {
            uidList.add(nextId(null));
        }

        return uidList;
    }


    @Override
    public String parseUID(long uid) {
        //TODO
        return "";
    }

}

