package com.dcits.comet.commons.utils;



import com.dcits.comet.commons.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


/**
 * 日期工具类(线程安全)
 * @author wang.yq
 */
public final class DateUtil {
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
	public static final String PATTERN_ISO_DATE = "yyyy-MM-dd";
	public static final String PATTERN_ISO_TIME = "HH:mm:ss";
	public static final String PATTERN_ISO_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_ISO_FULL = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String PATTERN_SIMPLE_DATE = "yyyyMMdd";
	public static final String PATTERN_SIMPLE_TIME = "HHmmss";
	public static final String PATTERN_SIMPLE_DATETIME = "yyyyMMddHHmmss";
	public static final String PATTERN_SIMPLE_FULL = "yyyyMMddHHmmssSSS";
	protected static final String[] PATTERNS = { PATTERN_ISO_DATE, PATTERN_ISO_TIME, PATTERN_ISO_DATETIME, PATTERN_ISO_FULL, PATTERN_SIMPLE_DATE,
			PATTERN_SIMPLE_TIME, PATTERN_SIMPLE_DATETIME, PATTERN_SIMPLE_FULL };

	private  DateUtil() {
		throw new IllegalStateException("Utility class");
	}
	private static ThreadLocal<Calendar> calendar = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			return Calendar.getInstance();
		}
	};
	
	/**
	 * 日期转字符串
	 * @param date 日期类型
	 * @param pattern 格式字符串
	 * @return String 格式化后的字符串
	 */
	public static String formatDate(Date date, String pattern) {
		String locPattern=pattern;
		if (StringUtil.isEmpty(locPattern)) {
			locPattern = PATTERN_ISO_DATE;
		}
		return DateFormatUtils.format(date, locPattern);
	}

	/**
	 * 日期转字符串
	 * @param calendar 日历类型
	 * @param pattern 格式字符串
	 * @return String 格式化后的字符串
	 */
	public static String formatDate(Calendar calendar, String pattern) {
		String locPattern=pattern;
		if (StringUtils.isEmpty(locPattern)) {
			locPattern = PATTERN_ISO_DATE;
		}
		return DateFormatUtils.format(calendar, locPattern);
	}

	/**
	 * 字符串转日期
	 * @param strDate 日期字符串
	 * @return Date 解析后的日期类型
	 * @throws ParseException
	 */
	public static Date parseDate(String strDate)  {
		Date date=null;
		if(StringUtils.isEmpty(strDate)){
			return null;
		}
		try {
			date= DateUtils.parseDate(strDate, PATTERNS);
		} catch (ParseException e) {
			if (logger.isWarnEnabled()) {
				logger.warn(StringUtil.getStackTrace(e));
			}

		}
		return date;
	}

	/**
	 * 字符串转日期
	 * @param strDate 日期字符串
	 * @param patterns 格式字符串
	 * @return Date 日期类型
	 * @throws ParseException
	 */
	public static Date parseDate(String strDate, String... patterns)  {
		Date date=null;
		if(StringUtils.isEmpty(strDate)){
			return null;
		}

		try {
			 date= DateUtils.parseDate(strDate, patterns);
		} catch (ParseException e) {
			if (logger.isWarnEnabled()) {
				logger.warn(StringUtil.getStackTrace(e));
			}
		}
		return date;
	}

	/**
	 * 计算日期间隔天数
	 * @param startDate
	 * @param endDate
	 * @return BigDecimal
	 */
	public static  BigDecimal getDiffDays(Date startDate, Date endDate) {
		long time = endDate.getTime() - startDate.getTime();
		return new BigDecimal(time / 1000 / 60 / 60 / 24);
	}

     /* @param date1 <String>
     * @param date2 <String>
     * @return int
     * @throws ParseException
     */
	public static int getDateSpace(String date1, String date2) {

		int result = 0;

		Calendar calst = Calendar.getInstance();;
		Calendar caled = Calendar.getInstance();
		calst.setTime(parseDate("yyyyMMdd",date1));
		caled.setTime(parseDate("yyyyMMdd",date2));
		//设置时间为0时
		calst.set(Calendar.HOUR_OF_DAY, 0);
		calst.set(Calendar.MINUTE, 0);
		calst.set(Calendar.SECOND, 0);
		caled.set(Calendar.HOUR_OF_DAY, 0);
		caled.set(Calendar.MINUTE, 0);
		caled.set(Calendar.SECOND, 0);
		//得到两个日期相差的天数
		int days = ((int)(caled.getTime().getTime()/1000)-(int)(calst.getTime().getTime()/1000))/3600/24;

		return days;
	}


	/**
	 * 获取给定日期的日
	 *
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		return ca.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 功能说明:获取给定日期的下月对日日期，如果对日日期大于下月最大日期(月末)，取下月最大日期（月末）
	 *
	 * @param startDate
	 * @return
	 */
	public static Date getDateOfNextMonth(Date startDate, Integer day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.MONTH, 1);
		Date lastDayOfMonth = getLastDayOfMonth(calendar.getTime());
		// 下一月的最大天数大于需要对日的数，直接赋值对日数值，否则赋值月末
		if (day < getDayOfMonth(lastDayOfMonth)) {
			calendar.set(Calendar.DAY_OF_MONTH, day);
		} else {
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		}

		return calendar.getTime();
	}

	/**
	 * 获取给定日期的最后一天日期
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 功能说明：判断日期是否是月末
	 *
	 * @param date
	 * @return
	 */
	public static boolean isEndOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
			return calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}
	
	/**
	 * 计算日期加上天数后得到的日期
	 * @param date
	 * @param i
	 * @return
	 * @throws ParseException
	 */
	public static  Date addDate(Date date, int i){
		return DateUtils.addDays(date, i);
	}

	/**
	 * 根据变动周期计算下一变动日
	 * @param date 变动起始日期
	 * @param datMth 利率变更周期单位 Y年M月W周R日
	 * @param rollFreq 利率变更周期值
	 * @param rollDay 变更日期
	 * @return
	 */
	public static Date getNextDate(Date date,String datMth, int rollFreq, String rollDay) throws ParseException {
		int year = getYear(date);
		int month = getMonth(date);
		int day ;
		int rollDayInt = 0;
		int monthFirstDay = getDay(getMonthFirstDay(date));
		if (StringUtils.isNotEmpty(rollDay)) {
			rollDayInt = Integer.parseInt(rollDay);
		}
		if("Y".equals(datMth)){//年
			year = year + rollFreq;
			day = rollDayInt;
			Date newDateMoth = parseDate(year+"-"+month+"-"+monthFirstDay);//增加相频率后当月同一天
			int monthEndDay = getDay(getMonthLastDay(newDateMoth));//获取计算出的当月的最后一天
			if (day > monthEndDay) {//若day大于当月最后一天，则将当月最后一天赋值给day
				day = monthEndDay;
			}
			return parseDate(year+"-"+month+"-"+day);
		}else if("M".equals(datMth)){//月
			int mm = month + rollFreq;
			int residue = mm/12;//取月份除以12的余数
			int mod = mm%12;//取月份除以12的模
			if(residue > 0){//想加的月份大于12则年加相应的时间
				year = year + residue;
				month = mod;
			}else if(mod > 0){
				month = mod;
			}
			day = rollDayInt;
			Date newDateMoth = parseDate(year+"-"+month+"-"+monthFirstDay);//增加相频率后当月同一天
			int monthEndDay = getDay(getMonthLastDay(newDateMoth));//获取计算出的当月的最后一天
			if (day > monthEndDay) {//若day大于当月最后一天，则将当月最后一天赋值给day
				day = monthEndDay;
			}
			return parseDate(year+"-"+month+"-"+day);//得到变更后的日期
		}else if("W".equals(datMth)){//周
			return addDate(date,rollFreq*7);
		}else if("D".equals(datMth)){//日
			return addDate(date,rollFreq);
		}
		return null;
	}
	

	/**
	 * 获取日期的年份
	 * @param date
	 * @return int
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * 获取日期的月份
	 * @param date
	 * @return int
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 获取日期的日
	 * @param date
	 * @return int
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	} 
	

//	
//	/**
//	 * 根据利率变动周期和利率变更日，计算每段周期的时间段
//	 * @param rollDate 下一个利率变更日期，需要在startDate和endDate之间
//	 * @param datMth 利率变更周期单位 Y年M月R日
//	 * @param rollFreq 利率变更周期
//	 * @param rollDay 变更日期
//	 */

	public static Calendar getCalendar() {
		Calendar calendar = DateUtil.calendar.get();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return calendar;
	}

	public static Calendar getCalendar(long date) {
		Calendar calendar = DateUtil.calendar.get();
		calendar.setTimeInMillis(date);
		return calendar;
	}

	public static Calendar getCalendar(Date date) {
		Calendar calendar = DateUtil.calendar.get();
		calendar.setTimeInMillis(date.getTime());
		return calendar;
	}

	/**
	 * 返回给定日期中的月份中的第一天
	 *
	 * @param date 基准日期
	 * @return 该月第一天的日期
	 */
	public static Date getMonthFirstDay(Date date) {

		Calendar calendar = getCalendar();
		calendar.setTime(date);

		// 将日期设置为当前月第一天
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), 1);

		return calendar.getTime();
	}


	/**
	 * 返回给定日期中的月份中的最后一天
	 *
	 * @param date 基准日期
	 * @return 该月最后一天的日期
	 */
	public static Date getMonthLastDay(Date date) {

		Calendar calendar = getCalendar();
		calendar.setTime(date);

		// 将日期设置为下一月第一天
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1, 1);

		// 减去1天，得到的即本月的最后一天
		calendar.add(Calendar.DATE, -1);

		return calendar.getTime();
	}
	public static Date addMonths(Date date,int addNo){
		return DateUtils.addMonths(date, addNo);
	}

	
	public static Date getYearLastDay(Date date) {

		Calendar calendar = getCalendar();
		calendar.setTime(date);
		// 将日期设置为当前月第一天
		calendar.set(calendar.get(Calendar.YEAR), 12, 31);
		return calendar.getTime();
	}
	/**
	 * 返回给定日期中的月份中的第一天
	 *
	 * @param date
	 *            基准日期
	 * @return 该月第一天的日期
	 */
	public static Date getYearFirstDay(Date date) {

		Calendar calendar = getCalendar();
		calendar.setTime(date);
		// 将日期设置为当前月第一天
		calendar.set(calendar.get(Calendar.YEAR), 0, 1);
		return calendar.getTime();
	}
	
	/**
	 * 返回给定日期所在季的第一天
	 *
	 * @param date 基准日期
	 * @return 该季第一天的日期
	 */
	public static Date getQuarterFirstDay(Date date) {
		Date newDate = null;
		int month = DateUtil.getMonth(date);
		if (month > 0 && month <= 3) {// 第一季度
			newDate = getYearFirstDay(date);
		} else if (month > 3 && month <= 6) {// 第二季度
			newDate = getYearFirstDay(date);
			newDate = DateUtils.addMonths(newDate, 3);
		} else if (month > 6 && month <= 9) {// 第三季度
			newDate = getYearFirstDay(date);
			newDate = DateUtils.addMonths(newDate, 6);
		} else if (month > 9 && month <= 12) {// 第四季度
			newDate = getYearFirstDay(date);
			newDate = DateUtils.addMonths(newDate, 9);
		}
		return newDate;
	}
	
	/**
	 * 返回给定日期所在季的最后一天
	 *
	 * @param date 基准日期
	 * @return 该季最后一天的日期
	 */
	public static Date getQuarterLastDay(Date date) {
		Date newDate = null;
		int month = DateUtil.getMonth(date);
		if (month > 0 && month <= 3) {// 第一季度
			newDate = getYearFirstDay(date);
			newDate = DateUtils.addMonths(newDate, 2);
			newDate = getMonthLastDay(newDate);
		} else if (month > 3 && month <= 6) {// 第二季度
			newDate = getYearFirstDay(date);
			newDate = DateUtils.addMonths(newDate, 5);
			newDate = getMonthLastDay(newDate);
		} else if (month > 6 && month <= 9) {// 第三季度
			newDate = getYearFirstDay(date);
			newDate = DateUtils.addMonths(newDate, 8);
			newDate = getMonthLastDay(newDate);
		} else if (month > 9 && month <= 12) {// 第四季度
			newDate = getYearLastDay(date);
		}
		return newDate;
	}
	/**
	 * 日期转时间戳
	 *
	 * @param date
	 * @return
	 */
	public static Integer transForMilliSecond(Date date) {
		if (date == null) {
            return null;
        }
		return (int) (date.getTime() / 1000);
	}


    /**
     * @Author guihj
     * @Description 获取系统当前时间
     * @Date 2019/4/15 9:43
     * @Param []
     * @return java.lang.String
     **/
    public static String  getCurrentDate(){
        Date date = new Date();
        //时间戳
        long times = date.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  formatter.format(date);
    }

    /**
     * 获取给定时间与当前系统时间的差值（以毫秒为单位）
     *
     * @author GaoHuanjie
     */
    public static long getTimeDifference(String paramTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取系统时间
        String systemTime = getCurrentDate();
        long difference = 0;
        try {
            Date systemDate = dateFormat.parse(systemTime);
            Date paramDate = dateFormat.parse(paramTime);
            difference = systemDate.getTime() - paramDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return difference;
    }


    /**
     * @Author guihj
     * @Description 生成随机id TODO  提交时删除  需要用自动生成随机id
     * @Date 2019/4/15 13:30
     * @Param []
     * @return java.lang.Long
     **/
    public  static Long  longRandomId(){
        long min = 1;
        long max = 99999999;
        Long rangeLong = min + (((long) (new Random().nextDouble() * (max - min))));
        return rangeLong;
    }



}
