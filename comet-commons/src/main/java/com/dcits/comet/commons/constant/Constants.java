package com.dcits.comet.commons.constant;

public class Constants {

    public final static String SERVICE_NAME = "ServiceName";

    public static class ResponseCode {
        public static final String SUCCESS = "00000000";
        public static final String SUCCESS_MSG = "交易成功";

        public static final String EXCEPTION = "99999999";

        public static final String CONNECT_ERROR = "DAPGWY01";
        public static final String CONNECT_ERROR_MSG = "通讯异常";
    }

    public static class ResponseType {
        public static final String SUCCESS = "S";
        /** 技术异常 */
        public static final String EXCEPTION = "E";
        /** 失败 */
        public static final String FAULT = "F";
        /** 不一致 */
        public static final String INCONFORMITY = "I";
    }

}
