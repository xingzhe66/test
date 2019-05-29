package com.dcits.comet.rpc.api.constants;

/**
 * 常量定义
 *
 * @author ChengLiang
 */
public class Constants {

    public final static String SERVICE_NAME = "ServiceName";

    public static class ResponseCode {

        public static final String CODE_CONNECT_ERROR = "777777";
        public static final String CODE_CONNECT_ERROR_MSG = "通讯异常";

//        /**
//         * 执行成功时，默认返回的RET_CODE的值
//         */
//        public static final String CODE_SUCCESS = "000000";
//        /**
//         * 执行成功时，默认返回的RET_MSG的值
//         */
//        public static final String MESSAGE_SUCCESS = "SUCCESS";
//        /**
//         * 执行失败时，默认返回的RET_CODE的值
//         */
//        public static final String CODE_FAILED = "999999";
//
//        public static final String MESSAGE_FAILED = "业务执行异常!";
        /**
         * RPC服务调用执行超时，默认返回的RET_CODE的值
         */
        public static final String CODE_TIMEOUT = "888888";


    }

    public static class ResponseStatus {

        /**
         * 执行成功时，返回报文中的RET_STATUS的值
         */
        public static final String STATUS_SUCCESS = "S";
        /**
         * 执行失败时，返回报文中的RET_STATUS的值
         */
        public static final String STATUS_FAILED = "F";
        /**
         * 需要授权时，返回报文中的RET_STATUS的值
         */
        public static final String STATUS_AUTH = "O";
        /**
         * 需要确认时，返回报文中的RET_STATUS的值
         */
        public static final String STATUS_CONF = "C";
        /**
         * 需要授权&确认时，返回报文中的RET_STATUS的值
         */
        public static final String STATUS_AUTH_CONF = "B";

        /**
         * 不一致
         */
        public static final String STATUS_INCONFORMITY = "I";
    }

}
