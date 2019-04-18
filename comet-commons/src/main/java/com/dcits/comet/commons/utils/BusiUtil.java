package com.dcits.comet.commons.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcits.comet.commons.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 业务工具类
 *
 * @author Chengliang
 */
public class BusiUtil {
    public static final String R = "R";
    public static final String T = "T";
    public static final int END = 31;
    private static Logger logger = LoggerFactory.getLogger(BusiUtil.class);
    private static String context = "context";
    private static final String AFTER_OPER = "|ALL$'";
    private static final String BEFORE_OPER = "'^";
    private static final String DATE_DAY = "D";
    private static final String DATE_WEEK = "W";
    private static final String DATE_MONTH = "M";
    private static final String DATE_YEAR = "Y";
    private static final String MONTH_BASIS_ACT = "ACT";

    /**
     * 获取异常
     *
     * @param errorCode
     * @param errorMessageParams
     * @return
     * @author chengliang
     */
    public static BusinessException createBusinessException(String errorCode, String... errorMessageParams) {
        // return createException(BusinessException.class, ret);
        return new BusinessException(errorCode, errorMessageParams);
    }


//    /**
//     * 不满足expression条件，抛出对应errCode异常
//     *
//     * @param expression
//     * @param errCode
//     */
//    public static void throwBusinessException(boolean expression, String errCode) {
//        if (!expression) {
//            throw createException(BusinessException.class, new Results(createResult(errCode)));
//        }
//    }

//    /**
//     * 功能说明：如果参数为空，直接抛异常
//     *
//     * @param obj
//     */
//    public static void checkNullOrEmpty(Object obj) {
//        if (BusiUtil.isNull(obj)) {
//            throw createException(BusinessException.class, new Results("is not null", "is not null"));
//        }
//    }

//    /**
//     * 功能说明：如果参数为空，抛出 errCode 对应配置的错误信息，如果找不到直接返回errCode
//     *
//     * @param obj
//     * @param errCode
//     */
//    public static void checkNullOrEmpty(Object obj, String errCode) {
//        if (BusiUtil.isNull(obj)) {
//            throw createException(BusinessException.class, new Results(new Result(errCode, BusiUtil.nvl(getErrorMsg(errCode), errCode))));
//        }
//    }


    public static boolean isEquals(Long s, Long t) {
        return s.equals(t);
    }

    /**
     * 字符类型比较是否相等
     *
     * @param s
     * @param t
     * @return
     */
    public static boolean isEquals(String s, String t) {
        return StringUtil.isEquals(s, t);
    }

    public static boolean isNotEquals(String s, String t) {
        return !isEquals(s, t);
    }

    /**
     * 字符类型比较是否等于Y
     *
     * @param s
     * @return
     */
    public static boolean isEqualY(String s) {
        return isEquals("Y", s);
    }

    /**
     * 字符类型比较指定位置字符串是否等于
     *
     * @param s
     * @param t
     * @param index
     * @return
     */
    public static boolean isEqualforIndex(String s, String t, int index) {

        if (isNotNull(s)) {
            int len = s.length();
            if (len < index) {
                return false;
            } else {
                return isEquals(s.substring(index, index + 1), t);
            }
        }
        return false;
    }

    /**
     * 当source与condition比较值大小时，return 较大值<br>
     *
     * @param source
     * @param condition
     * @return
     * @description
     * @version 1.0
     * @author furong
     * @update 2016年1月28日 下午20:35:46
     */
    public static String Max(String source, String condition) {
        if (source.compareTo(condition) >= 0) {
            return source;
        }
        return condition;
    }

    /**
     * 转换为BigDecimal类型
     *
     * @param obj
     * @return BigDecimal
     * @author xucxd
     * @update 2016年2月24日 下午16:50:50
     */


    public static BigDecimal toBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof String) {
            String str = (String) obj;
            if ("".equals(str)) {
                return null;
            } else {
                return new BigDecimal(str);
            }
        }
        return new BigDecimal(String.valueOf(obj));
    }

    public static String formatArrayString(List list) {
        String ret = "";
        ret = list.toString();
        ret = ret.substring(1, ret.length());
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

    /**
     * 判断对象是否为Null，数组size = 0,字符串 length = 0
     *
     * @param obj Object
     * @return boolean
     */
    private static boolean isNullObj(Object obj) {
        if (obj == null) {
            return true;
        }
        if (String.class.isInstance(obj)) {
            return StringUtils.isEmpty((String) obj);
        } else if (List.class.isInstance(obj)) {
            return ((List) obj).isEmpty();
        } else if (Map.class.isInstance(obj)) {
            return ((Map) obj).size() == 0;
        } else {
            return obj == null;
        }
    }

    /**
     * 对象是否为空
     *
     * @param obj Object
     * @return boolean
     */
    public static boolean isNull(Object obj) {
        return isNullObj(obj);
    }


    /**
     * 判断第一个参数是否在后面参数存在
     *
     * @param source
     * @param obj
     * @return
     */
    public static boolean isIn(Object source, Object... obj) {
        boolean result = false;
        if (isNotNull(source) && obj.length > 0) {
            for (int i = 0; i < obj.length; i++) {
                if (source.toString().equals(obj[i])) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }


    /**
     * 对象是否不为空
     *
     * @param obj Object
     * @return boolean
     */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }


    /**
     * 对象是否全部为null
     *
     * @param objs
     * @return
     */
    public static boolean isNullAll(Object... objs) {

        for (int i = 0; i < objs.length; i++) {
            if (isNotNull(objs[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对象是否全部不为null
     *
     * @param objs
     * @return
     */
    public static boolean isNotNullAll(Object... objs) {

        for (int i = 0; i < objs.length; i++) {
            if (isNull(objs[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对象是否存在不为null
     *
     * @param objs
     * @return
     */
    public static boolean existNotNull(Object... objs) {

        for (int i = 0; i < objs.length; i++) {
            if (isNotNull(objs[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象是否存在为null
     *
     * @param objs
     * @return
     */
    public static boolean existNull(Object... objs) {

        for (int i = 0; i < objs.length; i++) {
            if (isNull(objs[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象为空返回0，否则返回对应int类型的数值
     *
     * @param strNum
     * @return
     */
    public static int nvlZero(String strNum) {
        int ret = 0;

        if (BusiUtil.isNotNull(strNum)) {
            ret = Integer.parseInt(strNum);
        }
        return ret;
    }

    /**
     * 对象为空（包括list size=0, string length=0），返回dest
     *
     * @param source
     * @param dest
     * @return
     */
    public static <T> T nvl(T source, T dest) {
        return isNullObj(source) ? dest : source;
    }


    /**
     * 当source等于condition时，return value<br>
     * 否则return value2
     *
     * @param source
     * @param condition
     * @param value
     * @param value1
     * @return
     */
    public static <T> T decode(String source, String condition, T value,
                               T value1) {
        if (condition.equals(source)){
            return value;
        }

        return value1;
    }

    /**
     * 当source等于condition1时，return value1<br>
     * 当source等于condition2时，return value2<br>
     * 否则return value3
     *
     * @param source
     * @param condition1
     * @param value1
     * @param condition2
     * @param value2
     * @param value3
     * @return
     */
    public static <T> T decode(String source, String condition1, T value1,
                               String condition2, T value2, T value3) {
        if (condition1.equals(source)){
            return value1;
        } else if (condition2.equals(source)){
            return value2;
        }

        return value3;
    }

    /**
     * 当source等于condition时，return value<br>
     * 否则return value2
     *
     * @param source
     * @param condition
     * @param value
     * @param value1
     * @return
     */
    public static <T> T decode(int source, int condition, T value, T value1) {
        return decode(String.valueOf(source), String.valueOf(condition), value,
                value1);
    }

    /**
     * 当source等于condition1时，return value1<br>
     * 当source等于condition2时，return value2<br>
     * 否则return value3
     *
     * @param source
     * @param condition1
     * @param value1
     * @param condition2
     * @param value2
     * @param value3
     * @return
     */
    public static <T> T decode(int source, int condition1, T value1,
                               int condition2, T value2, T value3) {
        return decode(String.valueOf(source), String.valueOf(condition1),
                value1, String.valueOf(condition2), value2, value3);
    }

    /**
     * 日期格式化
     *
     * @param date
     * @return
     */
    public static String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_SIMPLE_DATE);
        return sdf.format(date);
    }

    /**
     * 获取日期
     *
     * @param dateStr
     * @return
     */
    public static Date getDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_SIMPLE_DATE);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.error("text", e);
        }
        return date;
    }

    /**
     * 日期计算
     *
     * @param dateStr yyyyMMdd 格式日期
     * @param addNum  增加数值，负数代码减少数值
     * @param freq    周期类型 D：天数 M：月数 Y：年数
     * @return
     */
    public static String calcFreqDate(String dateStr, String addNum, String freq) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.error(context, e);
        }

        int add = Integer.parseInt(addNum);

        int freqType = -1;
        switch (freq) {
            case "D":
                freqType = Calendar.DAY_OF_YEAR;
                break;
            case "M":
                freqType = Calendar.MONTH;
                break;
            case "Y":
                freqType = Calendar.YEAR;
                break;
            default:
                break;
        }
        if (freqType < 0) {
            createBusinessException("FM4031", freq);
        }
        Date ret = addDate(date, add, freqType);
        return dateFormat(ret);
    }

    /**
     * 日期计算
     *
     * @param date 日期
     * @param num  增加数值
     * @param freq 周期类型
     * @return
     */
    private static Date addDate(Date date, int num, int freq) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(freq, num);
        return calendar.getTime();
    }

    /**
     * 两个日期间隔数
     *
     * @param dateStr1 日期1
     * @param dateStr2 日期2
     * @param freq     周期类型 D：天数 M：月数 Y：年数
     * @return int
     */

    public static int diff(String dateStr1, String dateStr2, String freq) {
        Date date1 = getDate(dateStr1);
        Date date2 = getDate(dateStr2);
        int diff = 0;
        if (BusiUtil.isNotNullAll(date1, date2)) {

            Calendar c1 = Calendar.getInstance();
            c1.setTime(date1);
            //设置时间为0
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);

            Calendar c2 = Calendar.getInstance();
            c2.setTime(date2);
            //设置时间为0
            c2.set(Calendar.HOUR_OF_DAY, 0);
            c2.set(Calendar.MINUTE, 0);
            c2.set(Calendar.SECOND, 0);

            switch (freq) {
                case "D":
                    //跨年月计算
                    long days = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (1000 * 3600 * 24);
                    diff = Integer.parseInt(String.valueOf(days));
                    break;
                case "M":
                    diff = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
                    break;
                case "Y":
                    diff = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
                    break;
                default:
                    break;
            }
        }
        return diff;
    }

    /**
     * 对象比较
     *
     * @param bean1
     * @param bean2
     * @param flag   IN:include;EX:exclude
     * @param filter
     * @return key:{afterValue:"",beforeValue:""}
     */
    public static JSONObject compare(Object bean1, Object bean2, String flag, String filter) {
        JSONObject ret = new JSONObject();

        String[] filters = filter.split(",");
        HashMap filterMap = new HashMap(10);
        for (int i = 0; i < filters.length; i++) {
            filterMap.put(filters[i], filters[i]);
        }

        JSONObject t = (JSONObject) JSON.toJSON(bean1);
        Set<Map.Entry<String, Object>> keySet = t.entrySet();
        Iterator<Map.Entry<String, Object>> iter = keySet.iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            if (BusiUtil.isEquals(flag, "IN")) {
                if (BusiUtil.isNotNull(filterMap.get(key))) {
                    try {
                        Object v1 = BusiUtil.nvl(entry.getValue(), "").toString();
                        Object v2 = BusiUtil.nvl(BeanUtil.getValue(bean2, entry.getKey()), "").toString();
                        // Object v2 = BusiUtil.nvl(bean2.readValue(entry.getKey()), "").toString();
                        if (!v1.equals(v2)) {
                            JSONObject json = new JSONObject();
                            json.put("beforeValue", v1);
                            json.put("afterValue", v2);
                            ret.put(key, json);
                        }

                    } catch (Exception e) {
                        logger.error(context, e);
                    }
                }
            }
            if (BusiUtil.isEquals(flag, "EX")) {
                if (BusiUtil.isNull(filterMap.get(key))) {
                    try {
                        Object v1 = BusiUtil.nvl(entry.getValue(), "").toString();
                        // Object v2 = BusiUtil.nvl(bean2.readValue(entry.getKey()), "").toString();
                        Object v2 = BusiUtil.nvl(BeanUtil.getValue(bean2, entry.getKey()), "").toString();
                        if (!v1.equals(v2)) {
                            JSONObject json = new JSONObject();
                            json.put("beforeValue", v1);
                            json.put("afterValue", v2);
                            ret.put(key, json);
                        }

                    } catch (Exception e) {
                        logger.error(context, e);
                    }
                }
            }

        }

        return ret;
    }

//    /**
//     * 获取SpringBean
//     *
//     * @param clazz
//     * @param <T>
//     * @return
//     */
//    public static <T> Object getSpringBean(Class<T> clazz) {
//        Object bean = null;
//        try {
//            bean = SpringApplicationContext.getContext().getBean(clazz);
//        } catch (BeansException e) {
//            return null;
//        }
//        return bean;
//    }

    /**
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V getFirstOrNull(Map<K, V> map) {
        V obj = null;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            obj = entry.getValue();
            if (BusiUtil.isNotNull(obj)) {
                break;
            }
        }
        return obj;
    }

    /**
     * 预处理靠档天数
     *
     * @param startDate
     * @param endDate
     * @return BigDecimal
     */
    public static BigDecimal processGearDays(Date startDate, Date endDate) {
        if (endDate.before(startDate)) {
            return BigDecimal.ZERO;
        }
        BigDecimal diffYear = new BigDecimal(DateUtil.getYear(endDate) - DateUtil.getYear(startDate));
        BigDecimal diffMonth = new BigDecimal(DateUtil.getMonth(endDate) - DateUtil.getMonth(startDate));
        //得到两日期相间隔的月份
//		//解决20150131到20150201天数相差0天的问题，而实际应该相差1天
        int fromDays = DateUtil.getDay(startDate);
        int toDays = DateUtil.getDay(endDate);
        BigDecimal diffDay;
        if (fromDays > toDays) {
            diffMonth = diffMonth.subtract(new BigDecimal(1));
            int toAllDays = DateUtil.getDay(DateUtil.getMonthLastDay(endDate));
            int fromAllDays = DateUtil.getDay(DateUtil.getMonthLastDay(startDate));
            if (toDays == toAllDays) {
                //如果toDays 为月末，则此时 diffDay应该为30
                diffDay = new BigDecimal(30);
            } else {
                //考虑非对月时，比如：20160112 和 20160310
                diffDay = new BigDecimal(toDays + fromAllDays - fromDays);
            }
        } else {
            diffDay = new BigDecimal(toDays - fromDays);
        }
        // 天数=年间隔*360+月间隔*30+日间隔
        BigDecimal days1 = diffYear.multiply(new BigDecimal("360"));
        BigDecimal days2 = diffMonth.multiply(new BigDecimal("30"));
        return days1.add(days2).add(diffDay);
    }

    /**
     * 预处理计息天数
     *
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @param monthBasis 月基准天数类型
     * @return BigDecimal
     */
    public static BigDecimal processInterestDays(Date startDate, Date endDate, String monthBasis) {
        BigDecimal days = null;
        if (isEquals(monthBasis, MONTH_BASIS_ACT)) {
            // ACT模式,两日期直接相减
            days = DateUtil.getDiffDays(startDate, endDate);
        } else {
            // 非ACT模式(按照产品配置的年月基数计算天数,(按照整年，整月计算天数))
            //使用新的计算计息天数的方法，原来使用的processGearDays仅用于靠档天数的计算 zhanghmc 20160531
            days = calcNdayDiff(startDate, endDate, "30", null);
        }
        if (days == null) {
            days = BigDecimal.ZERO;
        }
        return days;
    }


    /**
     * 根据开始日期及周期频率计算结束日期
     *
     * @param startDate
     * @param periodType
     * @param periodValue
     * @return Date
     */
    public static Date convertDate(Date startDate, String periodType, String periodValue) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        if (isEquals(DATE_DAY, periodType)) {
            // 日
            cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(periodValue));
        } else if (isEquals(DATE_WEEK, periodType)) {
            // 星期
            cal.add(Calendar.WEEK_OF_MONTH, Integer.parseInt(periodValue));
        } else if (isEquals(DATE_MONTH, periodType)) {
            // 月
            cal.add(Calendar.MONTH, Integer.parseInt(periodValue));
        } else if (isEquals(DATE_YEAR, periodType)) {
            // 年
            cal.add(Calendar.YEAR, Integer.parseInt(periodValue));
        }
        return cal.getTime();
    }


    /**
     * 对金额精度进行处理
     *
     * @param type
     * @param amt
     * @param scale
     * @return BigDecimal
     */
    public static BigDecimal processAmt(String type, BigDecimal amt, Integer scale) {
        Integer locScale = scale;
        if (scale == null || scale < 0) {
            locScale = 0;
        }
        BigDecimal result = null;
        if (isEquals(R, type)) {
            result = amt.setScale(locScale, BigDecimal.ROUND_HALF_UP);
        }
        if (isEquals(T, type)) {
            result = amt.setScale(locScale, BigDecimal.ROUND_FLOOR);
        }
        return result;
    }


    /**
     * 计算日终批处理时两日期的间隔天数
     *
     * @param startDate
     * @param endDate
     * @param month
     * @return 间隔天数
     * 目前确定：非ACT时，按30天计息，则当月有31天时，30日计算利息，31日不计算利息；当月只有29天时，29天计提2天；
     * 如当月只有28天，则28天计息3天；
     */
    public static BigDecimal calcNdayDiff(Date startDate, Date endDate, String month, Integer day1) {

        Integer day = day1;
        BigDecimal diffDays = BigDecimal.ZERO;
        if (isEquals(MONTH_BASIS_ACT, month)) {
            diffDays = DateUtil.getDiffDays(startDate, endDate);
        } else {
            // 判断计息开始日期与计息到期日是否满足对月
            // 对月标准 日对日，如果超过下月最大日，对月末
            // 推下一个计息日期，如果下一计息日期在endDate之前，按照ACT计算
            Date nextAcrDate = null;
            Date lastAcrDate = startDate;
            if (isNull(day)) {
                day = DateUtil.getDayOfMonth(startDate);
            }
            if (day <= 0 || day > END) {
                //计息天数为空
                throw BusiUtil.createBusinessException("LI4017");
            }
            do {
                // 取给定日期的下月对日日期，如果是月末对下月月末
                nextAcrDate = DateUtil.getDateOfNextMonth(lastAcrDate, day);
                if (nextAcrDate.compareTo(endDate) > 0) {
                    diffDays = diffDays.add(DateUtil.getDiffDays(lastAcrDate, endDate));
                } else {
                    diffDays = diffDays.add(new BigDecimal(30));
                }
                lastAcrDate = nextAcrDate;
            } while (nextAcrDate.before(endDate));

        }
        return diffDays;
    }


    /**
     * 多字符拼接
     *
     * @param str
     * @return
     */
    public static String appendString(String... str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            if (isNotNull(str[i])) {
                sb.append(str[i]);
            }
        }
        return sb.toString();
    }

    public static int diffYears(Date one, Date two) {
        Calendar calendar = DateUtil.getCalendar();
        calendar.setTime(one);
        int yearOne = calendar.get(1);
        calendar.setTime(two);
        int yearTwo = calendar.get(1);
        return yearOne - yearTwo;
    }

    public static String nextDay(String date) throws ParseException {
        Date d = convertStr2Date(date);
        d = DateUtil.addDate(d, 1);
        return convertDate2Str(d);
    }

    /**
     * 将字符String类型转换为日期Date类型,默认转换为yyyyMMdd
     *
     * @param date
     * @return
     * @description
     * @version 1.0
     * @author Liang
     * @update 2015年2月5日 下午4:00:16
     */
    public static Date convertStr2Date(String date)  {
        return DateUtil.parseDate(date, DateUtil.PATTERN_SIMPLE_DATE);
    }

    /**
     * 将日期转为String类型，Date如果为null，返回null，默认转换为yyyyMMdd
     *
     * @param date
     * @return
     * @description
     * @version 1.0
     * @author Tim
     * @update 2015年2月4日 上午11:04:07
     */
    public static String convertDate2Str(Date date) {
        return convertDate2Str(date, DateUtil.PATTERN_SIMPLE_DATE);
    }

    /**
     * 将日期转为String类型
     *
     * @param date
     * @param pattern
     * @return
     * @description
     * @version 1.0
     * @author Tim
     * @update 2015年2月4日 上午11:03:04
     */
    public static String convertDate2Str(Date date, String pattern) {
        if (null == date) {
            return null;
        }
        return DateUtil.formatDate(date, pattern);
    }

    public static boolean isGreaterZero(BigDecimal v) {

        if (isNull(v)) {
            return false;
        }

        return v.compareTo(BigDecimal.ZERO) > 0;
    }

//    /**
//     * 获取request对象field的值，查询不到返回Null
//     *
//     * @param request
//     * @param fieldName
//     * @return
//     */
//    public static Object getBodyFieldValue(BaseRequest request, String fieldName) {
//
//        try {
//            return BeanUtil.getFieldValue(request, "body." + fieldName);
//        } catch (Exception e) {
//            return null;
//        }
//    }

    /**
     * 核算
     *
     * @param tableName
     * @param pkName
     * @param models
     * @return
     */
    public static boolean checkDataOperateParams(String tableName, String pkName, List<Map<String, Object>> models) {
        return !(BusiUtil.isNull(tableName) || BusiUtil.isNull(pkName) || models == null);
    }

    /**
     * 核算
     *
     * @param input
     * @return
     */
    public static String getOperString(String input) {
        return new StringBuffer().append(BEFORE_OPER).append(input).append(AFTER_OPER).toString();
    }
}
