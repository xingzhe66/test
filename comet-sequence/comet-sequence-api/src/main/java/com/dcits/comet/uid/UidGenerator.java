package com.dcits.comet.uid;

import com.dcits.comet.commons.exception.UidGenerateException;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 9:45
 * @see UidGenerator
 **/
public interface UidGenerator {
    /**
     * 根据主机名获取ID
     *
     * @param []
     * @return String
     * @throws UidGenerateException
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/28 9:25
     **/
    String getUID() throws UidGenerateException;

    /**
     * 根据业务编码获取流水号主机名+业务编码 false前缀不带日期
     *
     * @param bizTag  业务类型
     * @param preDate 是否带前缀日期
     * @return long
     * @throws UidGenerateException
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/28 9:26
     **/
    String getUID(String bizTag, boolean preDate) throws UidGenerateException;

    /**
     * 根据业务编码获取流水号主机名+业务编码 false前缀不带日期
     *
     * @param preDate 是否带前缀日期
     * @return String
     * @throws UidGenerateException
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/28 9:26
     **/
    String getUID(boolean preDate) throws UidGenerateException;

    /**
     * Parse the UID into elements which are used to generate the UID. <br>
     * Such as timestamp & workerId & sequence...
     *
     * @param uid
     * @return Parsed info
     */
    String parseUID(String uid);
}
