package com.dcits.comet.uid;

import com.dcits.comet.commons.exception.UidGenerateException;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 9:45
 * @see UidGenerator
 **/

public interface UidGenerator {
    //redis序列
    String UID_REIDS_DEF = "/uid/redis/def";

    String UID_REIDS_PREDATE = "/uid/redis/predate";

    String UID_REIDS_BIZTAG = "/uid/redis/biztag";

    String UID_REIDS_PREDATEANDBIZTAG = "/uid/redis/predateandbiztag";

    //default序列
    String UID_DEF_DEF = "/uid/def/def";

    String UID_DEF_PREDATE = "/uid/def/predate";

    String UID_DEF_BIZTAG = "/uid/def/biztag";

    String UID_DEF_PREDATEANDBIZTAG = "/uid/def/predateandbiztag";

    //buffer序列
    String UID_BUFF_DEF = "/uid/buf/def";

    String UID_BUFF_PREDATE = "/uid/buf/predate";

    String UID_BUFF_BIZTAG = "/uid/buf/biztag";

    String UID_BUFF_PREDATEANDBIZTAG = "/uid/buf/predateandbiztag";

    /**
     * 根据主机名获取ID
     *
     * @param
     * @return String
     * @throws UidGenerateException
     * @Author leijian
     * @Description //TODO
     * @Date 2019/3/28 9:25
     **/
    long getUID() throws UidGenerateException;

    /**
     * @param bizTag
     * @return java.lang.String
     * @author leijian
     * @Description //TODO
     * @ate 2019/3/28 17:29
     **/
    long getUID(String bizTag) throws UidGenerateException;


    /**
     * Parse the UID into elements which are used to generate the UID. <br>
     * Such as timestamp & workerId & sequence...
     *
     * @param uid
     * @return Parsed info
     */
    String parseUID(long uid);

}
