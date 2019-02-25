package com.dcits.comet.mybatis.generator.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @功能说明： 系统常用工具类
 * @作者： herun
 * @创建日期：2014-2-25
 * @版本号：V1.0
 */
public class PbUtils {

    /*
     * 表名转换为驼峰命名
     */
    public static String convertToCamelCase(String str) {
        String result = "";
        if (str == null) {
            return "";
        }
        String[] strArr = str.trim().split("_");
        for (String s : strArr) {
            if (s.length() > 1) {
                result += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            } else {
                result += s.toUpperCase();
            }
        }

        return result;
    }

    /*
     * 表名转换为首字母小写的驼峰命名
     */
    public static String convertToFirstLetterLowerCaseCamelCase(String str) {
        String resultCamelCase = convertToCamelCase(str);

        String result = "";
        if (resultCamelCase.length() > 1) {
            result = resultCamelCase.substring(0, 1).toLowerCase() + resultCamelCase.substring(1);
        } else {
            result = resultCamelCase.toLowerCase();
        }

        return result;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {
        // SimpleDateFormat format = new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转换成日期 yyyy-MM-dd HH:mm:ss
     *
     * @param str
     * @return date
     */
    public static Date StrToDateTime(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str) {
        boolean isString = str instanceof String;
        if (isString) {
            if (str == null) {
                return true;
            } else if (String.valueOf(str).trim().equals("")) {
                return true;
            }
        } else {
            if (str == null) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取当前时间
     *
     * @return yyyyMMddHHmmss
     */
    public static synchronized String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyyMMddHHmmss
     */
    public static synchronized String getCurrenForMilli() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static synchronized String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd
     */
    public static synchronized String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyyMMddhhmmssSSSSSS
     */
    public static synchronized String getMillisecondTime() {
        return new SimpleDateFormat("yyyyMMddhhmmssSSSSSS").format(new Date());
    }

    /**
     * 根据指定格式返回当前时间
     *
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 替换掉下划线并让紧跟它后面的字母大写,例如 ad_code 转成 adCode
     *
     * @param str
     * @return
     */
    public static String strRelplacetoLowerCase(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(str.toLowerCase());

        int count = sb.indexOf("_");
        while (count != 0) {
            int num = sb.indexOf("_", count);
            count = num + 1;
            if (num != -1) {
                char ss = sb.charAt(count);
                char ia = (char) (ss - 32);
                sb.replace(count, count + 1, ia + "");
            }
        }
        return sb.toString().replaceAll("_", "");
    }

    /**
     * 返回首字母大写的字符串
     *
     * @param str
     * @return
     */
    public static String fristStrToUpperCase(String str) {
        String resultStr = str.substring(0, 1).toUpperCase() + str.substring(1);
        return resultStr;
    }

    /**
     * 返回首字母和第二个字母小写的字符串
     *
     * @param str
     * @return
     */
    public static String fristAndSecondStrToLowerCase(String str) {
        String resultStr = str.substring(0, 1).toLowerCase() + str.substring(1, 2).toLowerCase() + str.substring(2);
        return resultStr;
    }

    /**
     * 返回首字母小写的字符串
     *
     * @param str
     * @return
     */
    public static String fristStrToLowerCase(String str) {
        String resultStr = str.substring(0, 1).toLowerCase() + str.substring(1);
        return resultStr;
    }


    /**
     * String 转化 Date
     *
     * @param dateString YYYY-MM-DD HH:MI:SS
     * @return
     */
    public static Date getDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return date;
    }

    /**
     * po中的列类型
     * JDBC Type           Java Type
     * CHAR                String
     * VARCHAR             String
     * LONGVARCHAR         String
     * NUMERIC             java.math.BigDecimal
     * DECIMAL             java.math.BigDecimal
     * BIT                 boolean
     * BOOLEAN             boolean
     * TINYINT             byte
     * SMALLINT            short
     * INTEGER             INTEGER
     * BIGINT              long
     * REAL                float
     * FLOAT               double
     * DOUBLE              double
     * BINARY              byte[]
     * VARBINARY           byte[]
     * LONGVARBINARY       byte[]
     * DATE                java.sql.Date
     * TIME                java.sql.Time
     * TIMESTAMP           java.sql.Timestamp
     * CLOB                Clob
     * BLOB                Blob
     * ARRAY               Array
     * DISTINCT            mapping of underlying type
     * STRUCT              Struct
     * REF                 Ref
     * DATALINK            java.net.URL
     */

    public static String convertJavaType(String type) {
        String javaType = "";
        type = type.toUpperCase();
        if (type.contains("CHAR") || type.contains("VARCHAR") || type.contains("LONGVARCHAR")) {
            javaType = "String";
        }
        if (type.contains("NUMERIC") || type.contains("DECIMAL")) {
            javaType = "java.math.BigDecimal";
        }
        if (type.contains("BIT") || type.contains("BOOLEAN")) {
            javaType = "boolean";
        }
        if (type.contains("BINARY") || type.contains("VARBINARY") || type.contains("LONGVARBINARY")) {
            javaType = "byte[]";
        }
        if (type.contains("BIGINT")) {
            javaType = "Long";
        }
        if (type.contains("DOUBLE") || type.contains("FLOAT")) {
            javaType = "double";
        }
        if (type.contains("DATE")) {
            javaType = "java.sql.Date";
        }
        if (type.contains("INTEGER")) {
            javaType = "INTEGER";
        }
        return javaType;
    }

    public static String convertJdbcType(String type) {
        String javaType = "";
        if (type.contains("long") || type.contains("interval") || type.contains("blob") || type.contains("varchar") || type.contains("char")) {
            javaType = "VARCHAR";
        }
        if (type.contains("decimal") || type.contains("float") || type.contains("double") || type.contains("integer") || type.contains("longtext")) {
            javaType = "NUMERIC";
        }
        if (type.contains("date") || type.contains("datetime")) {
            javaType = "DATE";
        }
        if (type.contains("int")) {
            javaType = "INTEGER";
        }
        return javaType;
    }

    /*
     * 将数据库的数据类型转换为java的数据类型
     */
    public static String convertType(String databaseType) {
        String javaType = "";
        String databaseTypeStr = databaseType.trim().toLowerCase();
        if (databaseTypeStr.startsWith("int")
                || databaseTypeStr.equals("smallint")
                || databaseTypeStr.equals("tinyint")
        ) {
            javaType = "Integer";
        } else if (databaseTypeStr.equals("char")) {
            javaType = "String";
        } else if (databaseTypeStr.equals("number")
                || databaseTypeStr.equals("numeric")
        ) {
            javaType = "Integer";
        } else if (databaseTypeStr.indexOf("varchar") != -1) {
            javaType = "String";
        } else if (databaseTypeStr.equals("blob")) {
            javaType = "Byte[]";
        } else if (databaseTypeStr.equals("float")) {
            javaType = "Float";
        } else if (databaseTypeStr.equals("double")) {
            javaType = "Double";
        } else if (databaseTypeStr.equals("decimal")) {
            javaType = "java.math.BigDecimal";
        } else if (databaseTypeStr.startsWith("bigint")) {
            javaType = "Long";
        } else if (databaseTypeStr.equals("date")) {
            javaType = "java.util.Date";
        } else if (databaseTypeStr.equals("time")) {
            javaType = "java.util.Date";
        } else if (databaseTypeStr.equals("datetime")) {
            javaType = "java.util.Date";
        } else if (databaseTypeStr.startsWith("timestamp")) {
            javaType = "java.util.Date";
        } else if (databaseTypeStr.equals("year")) {
            javaType = "java.util.Date";
        } else {
            javaType = "String";
        }
        return javaType;
    }

}
