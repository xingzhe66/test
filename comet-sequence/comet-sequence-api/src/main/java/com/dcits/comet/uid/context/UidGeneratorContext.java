package com.dcits.comet.uid.context;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/9 16:29
 **/
public class UidGeneratorContext {

    public static final String UID_APP_NAME = "comet-sequence-provider";
    //default序列
    public static final String UID_REDIS_DEF = "redis";

    //default序列
    public static final String UID_DEF_DEF = "snow";

    //buffer序列
    public static final String UID_LOAD_DEF = "load";


    public static final long UID_DEF_MAX_SEQ = 200;

    public static final long UID_DEF_MIN_SEQ = 0;

    public static final String UID_MIDDLEID = "0.75";

    public static final int UID_DEF_STEP = 1;

    public static final String UID_IS_CYCLE = "Y";

    public static final String UID_NOT_CYCLE = "N";

}
