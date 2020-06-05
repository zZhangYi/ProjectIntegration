package com.zhy.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 【类或接口功能描述】
 * 日期工具类
 *
 * @version 1.0.0
 * @comment
 */
public class DateUtil {
    /**
     * 默认的日期格式组合，用来将字符串转化为日期用
     */
    private static final String[] DATE_PARSE_PATTERNS = {"yyyy/MM/dd", "yyyy-MM-dd", "yyyy年MM月dd日", "yyyyMMdd"};

    /**
     * 默认的时间格式
     */
    private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    /**
     * 默认的日期格式
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    public static final String EN_GB_DATE_PATTERN = "dd/MM/yyyy";

    public static final String EN_GB_DATE_PATTERN_DIR = "yyyy/MM/dd";

    public static final String ZH_CN_TIME_PATTERN = "HH:mm:ss";

    public static final String ZH_CN_DATE_PATTERN = "yyyy-MM-dd";

    public static final String ZH_CN_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String ZH_CN_DTP_NO_HOR = "yyyyMMdd HH:mm:ss";

    public static final String ZH_CN_DP_NO_HOR = "yyyyMMdd";

    public static final String ZH_CN_DP_NO_TRIM = "yyyyMMddHHmmss";

    /**
     * 日期代码，周日
     */
    public static final int SUNDAY = 1;

    /**
     * 日期代码，周一
     */
    public static final int MONDAY = 2;

    /**
     * 日期代码，周二
     */
    public static final int TUESDAY = 3;

    /**
     * 日期代码，周三
     */
    public static final int WEDNESDAY = 4;

    /**
     * 日期代码，周四
     */
    public static final int THURSDAY = 5;

    /**
     * 日期代码，周五
     */
    public static final int FRIDAY = 6;

    /**
     * 日期代码，周六
     */
    public static final int SATURDAY = 7;

    /**
     * 日期精度，秒
     */
    public static final int ACCURACY_SECOND = 1;

    /**
     * 日期精度，分
     */
    public static final int ACCURACY_MINUTE = 2;

    /**
     * 日期精度，小时
     */
    public static final int ACCURACY_HOUR = 3;

    /**
     * 日期精度，天
     */
    public static final int ACCURACY_DAY = 4;

    /**
     * 日期精度，月
     */
    public static final int ACCURACY_MONTH = 5;

    /**
     * 日期精度，年
     */
    public static final int ACCURACY_YEAR = 6;

    /**
     * 比较用日期格式，精度为年
     */
    private static final String ACCURACY_PATTERN_YEAR = "yyyy";

    /**
     * 比较用日期格式，精度为月
     */
    private static final String ACCURACY_PATTERN_MONTH = "yyyyMM";

    /**
     * 比较用日期格式，精度为日
     */
    private static final String ACCURACY_PATTERN_DAY = "yyyyMMdd";

    /**
     * 比较用日期格式，精度为时
     */
    private static final String ACCURACY_PATTERN_HOUR = "yyyyMMddHH";

    /**
     * 比较用日期格式，精度为分
     */
    private static final String ACCURACY_PATTERN_MINUTE = "yyyyMMddHHmm";

    /**
     * 比较用日期格式，精度为秒
     */
    private static final String ACCURACY_PATTERN_SECOND = "yyyyMMddHHmmss";

    /**
     * 单一属性格式，时
     */
    private static final String SINGLE_YEAR = "yyyy";

    /**
     * 单一属性格式，时
     */
    private static final String SINGLE_MONTH = "M";

    /**
     * 单一属性格式，时
     */
    private static final String SINGLE_DAY = "d";

    /**
     * 单一属性格式，时
     */
    private static final String SINGLE_HOUR = "H";

    /**
     * 单一属性格式，分
     */
    private static final String SINGLE_MINUTE = "m";

    /**
     * 单一属性格式，秒
     */
    private static final String SINGLE_SECOND = "s";

    /**
     *
     */
    private static final long MILLISECONDS_PER_SECOND = 1000;

    /**
     *
     */
    private static final long MILLISECONDS_PER_MINUTE = 60000;//1000 * 60

    /**
     *
     */
    private static final long MILLISECONDS_PER_HOUR = 3600000;//1000 * 60 * 60

    /**
     *
     */
    private static final long MILLISECONDS_PER_DAY = 86400000;//1000 * 60 * 60 * 24


    /**
     * 偏移日期
     */
    private static String offset = null;

    public static void setOffset(String offset) {
        DateUtil.offset = offset;
    }

    /**
     * 将给定的日期字符串，按照预定的日期格式，转化为Date型数据
     *
     * @param dateStr 日期字符字符串
     * @return 日期型结果
     */
    public static Date parseDate(String dateStr) {
        Date date = null;
        try {
            date = DateUtils.parseDate(dateStr, DATE_PARSE_PATTERNS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDate(String dateStr) {
        Date date = null;
        try {
            if (dateStr.length() == 8) {
                date = DateUtils.parseDate(dateStr,
                        new String[]{"yyyyMMdd"});
            } else {
                date = DateUtils.parseDate(dateStr, new String[]{
                        "yyyyMMddHHmmss", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMdd HH:mm:ss",
                        "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"});
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据指定格式转化String型日期到Date型
     *
     * @param dateStr      String型日期
     * @param parsePattern 指定的格式
     * @return Date型日期
     */
    public static Date parseDate(String dateStr, String parsePattern) {
        Date date = null;
        try {
            date = DateUtils.parseDate(dateStr, parsePattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 返回系统当前时间（Date型）
     *
     * @return 系统当前时间
     */
    public static Date getCurrentDate() {
        Date currDate = new Date();
        if (offset == null || "".equals(offset)) {
            return currDate;
        } else {
            return parseDate(offset, ZH_CN_DATETIME_PATTERN);
        }
    }

    /**
     * 日期计算，日加减
     *
     * @param date   初始日期
     * @param amount 天数增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addDays(Date date, int amount) {
        return DateUtils.addDays(date, amount);
    }

    /**
     * 日期计算，根据传入的单位和数量进行计算
     * 仅支持年月日的计算
     *
     * @param date   基础日期
     * @param unit   单位
     * @param amount 数量
     * @return 计算后的日期
     */
    public static Date addDate(Date date, String unit, int amount) {
        if (StringUtils.equals("Y", unit)) {
            return DateUtils.addYears(date, amount);
        }
        if (StringUtils.equals("M", unit)) {
            return DateUtils.addMonths(date, amount);
        }
        if (StringUtils.equals("D", unit)) {
            return DateUtils.addDays(date, amount);
        }
        return date;
    }

    /**
     * 日期计算，周加减
     *
     * @param date   初始日期
     * @param amount 周数增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addWeeks(Date date, int amount) {
        return DateUtils.addWeeks(date, amount);
    }

    /**
     * 日期计算，月加减
     *
     * @param date   初始日期
     * @param amount 月数增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addMonths(Date date, int amount) {
        return DateUtils.addMonths(date, amount);
    }

    /**
     * 日期计算，年加减
     *
     * @param date   初始日期
     * @param amount 年数增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addYears(Date date, int amount) {
        return DateUtils.addYears(date, amount);
    }

    /**
     * 日期计算，小时加减
     *
     * @param date   初始日期
     * @param amount 小时增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addHours(Date date, int amount) {
        return DateUtils.addHours(date, amount);
    }

    /**
     * 日期计算，分钟加减
     *
     * @param date   初始日期
     * @param amount 分钟增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addMinutes(Date date, int amount) {
        return DateUtils.addMinutes(date, amount);
    }

    /**
     * 日期计算，秒加减
     *
     * @param date   初始日期
     * @param amount 秒增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addSeconds(Date date, int amount) {
        return DateUtils.addSeconds(date, amount);
    }

    /**
     * 根据指定格式，返回日期时间字符串
     *
     * @param date    日期变量
     * @param pattern 日期格式
     * @return 日期时间字符串
     */
    public static String getDateStr(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 输出时间String(默认格式)
     *
     * @param date 日期
     * @return 默认格式化的日期
     */
    public static String getTimeStr(Date date) {
        return getDateStr(date, DEFAULT_TIME_PATTERN);
    }

    /**
     * 取指定日期所在月的第一天的日期
     *
     * @param date 指定的日期
     * @return 指定日期所在月的第一天
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.DATE, 1);
        return cal.getTime();
    }

    /**
     * 取指定日期所在月的最后一天的日期
     *
     * @param date 指定的日期
     * @return 指定日期所在月的最后一天
     */
    public static Date getLastDayOfMonth(Date date) {
        Date nextMonth = addMonths(date, 1);
        Date firstDayOfNextMonth = getFirstDayOfMonth(nextMonth);
        return addDays(firstDayOfNextMonth, -1);
    }

    /**
     * 取指定日期所在年的第一天的日期
     *
     * @param date 指定的日期
     * @return 指定日期所在年的第一天
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, 0);
        return cal.getTime();
    }

    /**
     * 取指定日期所在年的最后一天的日期
     *
     * @param date 指定的日期
     * @return 指定日期所在月的最后一天
     */
    public static Date getLastDayOfYear(Date date) {
        Date nextMonth = addYears(date, 1);
        Date firstDayOfNextYear = getFirstDayOfYear(nextMonth);
        return addDays(firstDayOfNextYear, -1);
    }

    /**
     * 取指定日期所在周的指定天的日期
     *
     * @param date     指定的日期
     * @param day      指定的天（星期几）
     * @param firstDay 一星期的起始天
     * @return 指定周星期日的日期
     */
    public static Date getDayInWeek(Date date, int day, int firstDay) {
        Calendar cal = getCalendar(date);
        cal.setFirstDayOfWeek(firstDay);
        cal.set(Calendar.DAY_OF_WEEK, day);
        return cal.getTime();
    }

    /**
     * 根据Date型的日期，取Calendar型的日期
     *
     * @param date Date型的日期
     * @return Calendar型的日期
     */
    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * 日期比较（精确到天），date1晚于date2
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return date1晚于date2，返回true，否则返回false
     */
    public static boolean later(Date date1, Date date2) {
        boolean result = false;
        if (1 == compare(date1, date2, ACCURACY_DAY)) {
            result = true;
        }
        return result;
    }

    /**
     * 日期比较（精确到天），date1早于date2
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return date1早于date2，返回true，否则返回false
     */
    public static boolean earlier(Date date1, Date date2) {
        boolean result = false;
        if (-1 == compare(date1, date2, ACCURACY_DAY)) {
            result = true;
        }
        return result;
    }

    /**
     * 日期比较（精确到天），date1等于date2
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return date1等于date2，返回true，否则返回false
     */
    public static boolean isTheSameDay(Date date1, Date date2) {
        boolean result = false;
        if (0 == compare(date1, date2, ACCURACY_DAY)) {
            result = true;
        }
        return result;
    }

    /**
     * 根据指定规则比较日期，date1晚于date2
     *
     * @param date1    日期1
     * @param date2    日期2
     * @param accuracy 日期精度
     * @return date1晚于date2，返回true，否则返回false
     */
    public static boolean later(Date date1, Date date2, int accuracy) {
        boolean result = false;
        if (1 == compare(date1, date2, accuracy)) {
            result = true;
        }
        return result;
    }

    /**
     * 根据指定规则比较日期，date1早于date2
     *
     * @param date1    日期1
     * @param date2    日期2
     * @param accuracy 日期精度
     * @return date1早于date2，返回true，否则返回false
     */
    public static boolean earlier(Date date1, Date date2, int accuracy) {
        boolean result = false;
        if (-1 == compare(date1, date2, accuracy)) {
            result = true;
        }
        return result;
    }

    /**
     * 根据指定规则比较日期，date1等于date2
     *
     * @param date1    日期1
     * @param date2    日期2
     * @param accuracy 日期精度
     * @return date1等于date2，返回true，否则返回false
     */
    public static boolean isTheSameDate(Date date1, Date date2, int accuracy) {
        boolean result = false;
        if (0 == compare(date1, date2, accuracy)) {
            result = true;
        }
        return result;
    }

    /**
     * 根据指定规则，比较日期
     *
     * @param date1    日期1
     * @param date2    日期2
     * @param accuracy 日期精度
     * @return int型，date1晚，返回1；date1早，返回-1；相等，返回0
     */
    private static int compare(Date date1, Date date2, int accuracy) {
        String pattern = DEFAULT_DATE_PATTERN;
        switch (accuracy) {
            case ACCURACY_YEAR:
                pattern = ACCURACY_PATTERN_YEAR;
                break;
            case ACCURACY_MONTH:
                pattern = ACCURACY_PATTERN_MONTH;
                break;
            case ACCURACY_DAY:
                pattern = ACCURACY_PATTERN_DAY;
                break;
            case ACCURACY_HOUR:
                pattern = ACCURACY_PATTERN_HOUR;
                break;
            case ACCURACY_MINUTE:
                pattern = ACCURACY_PATTERN_MINUTE;
                break;
            case ACCURACY_SECOND:
                pattern = ACCURACY_PATTERN_SECOND;
                break;
            default:
                return date1.compareTo(date2);
        }

        Date formatedDate1 = transDateFormat(date1, pattern);
        Date formatedDate2 = transDateFormat(date2, pattern);
        if (null == formatedDate1 && null == formatedDate2) {
            return 0;
        } else if (null == formatedDate1 && null != formatedDate2) {
            return 1;
        } else if (null != formatedDate1 && null == formatedDate2) {
            return -1;
        } else {
            return formatedDate1.compareTo(formatedDate2);
        }

    }

    /**
     * 根据指定规则，转化日期，如只取年、取年月等
     *
     * @param date    待转化日期
     * @param pattern 日期格式
     * @return 转化后的日期
     */
    public static Date transDateFormat(Date date, String pattern) {
        String dateStr = getDateStr(date, pattern);
        return parseDate(dateStr, pattern);
    }

    /**
     * 返回时定时间的年
     *
     * @param date 日期
     * @return String型的年
     */
    public static String getYear(Date date) {
        return getDateStr(date, SINGLE_YEAR);
    }

    /**
     * 返回时定时间的月
     *
     * @param date 日期
     * @return String型的月
     */
    public static String getMonth(Date date) {
        return getDateStr(date, SINGLE_MONTH);
    }

    /**
     * 返回时定时间的日
     *
     * @param date 日期
     * @return String型的日
     */
    public static String getDay(Date date) {
        return getDateStr(date, SINGLE_DAY);
    }

    /**
     * 返回时定时间的小时
     *
     * @param date 日期
     * @return String型的小时
     */
    public static String getHour(Date date) {
        return getDateStr(date, SINGLE_HOUR);
    }

    /**
     * 返回时定时间的分
     *
     * @param date 日期
     * @return String型的分
     */
    public static String getMinute(Date date) {
        return getDateStr(date, SINGLE_MINUTE);
    }

    /**
     * 返回时定时间的秒
     *
     * @param date 日期
     * @return String型的秒
     */
    public static String getSecond(Date date) {
        return getDateStr(date, SINGLE_SECOND);
    }

    /**
     * 将时间日期变量的年份变为指定年, 如果日期不存在，则向后一天，如20102月
     *
     * @param date   日期时间变量
     * @param amount 指定年
     * @return 修改后的日期变量
     */
    public static Date setYear(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.YEAR, amount);
        return cal.getTime();
    }

    /**
     * 将时间日期变量的月份变为指定月
     *
     * @param date   日期时间变量
     * @param amount 指定月
     * @return 修改后的日期变量
     */
    public static Date setMonth(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.MONTH, amount - 1);
        return cal.getTime();
    }

    /**
     * 将时间日期变量的年份变为指定日
     *
     * @param date   日期时间变量
     * @param amount 指定日
     * @return 修改后的日期变量
     */
    public static Date setDay(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.DAY_OF_MONTH, amount);
        return cal.getTime();
    }

    /**
     * 将时间日期变量的小时变为指定时
     *
     * @param date   日期时间变量
     * @param amount 指定时
     * @return 修改后的日期变量
     */
    public static Date setHour(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.HOUR_OF_DAY, amount);
        return cal.getTime();
    }

    /**
     * 将时间日期变量的分钟变为指定分
     *
     * @param date   日期时间变量
     * @param amount 指定分
     * @return 修改后的日期变量
     */
    public static Date setMinute(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.MINUTE, amount);
        return cal.getTime();
    }

    /**
     * 将时间日期变量的秒变为指定秒
     *
     * @param date   日期时间变量
     * @param amount 指定秒
     * @return 修改后的日期变量
     */
    public static Date setSecond(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.SECOND, amount);
        return cal.getTime();
    }

    /**
     * 根据制定单位，计算两个日期之间的天数差
     *
     * @param a 时间点1
     * @param b 时间点2
     * @return 时间差
     */
    public static int getDateDistance(Date a, Date b) {
        return getDateDistance(a, b, ACCURACY_DAY);
    }

    /**
     * 根据制定单位，计算两个日期之间的差
     *
     * @param a    时间点1
     * @param b    时间点2
     * @param unit 时间单位
     * @return 时间差
     */
    public static int getDateDistance(Date a, Date b, int unit) {
        int result = 0;
        if (null != a && null != b) {
            String pattern = null;
            switch (unit) {
                case ACCURACY_HOUR: // '\003'
                    pattern = "yyyyMMddHH";
                    break;
                case ACCURACY_MINUTE: // '\002'
                    pattern = "yyyyMMddHHmm";
                    break;
                case ACCURACY_SECOND: // '\001'
                    pattern = "yyyyMMddHHmmss";
                    break;
                default:
                    pattern = "yyyyMMdd";
            }
            Date startDate = null;
            Date endDate = null;
            if (a.compareTo(b) > 0) { // a > b
                startDate = transDateFormat(b, pattern);
                endDate = transDateFormat(a, pattern);
            } else { // a <= b
                startDate = transDateFormat(a, pattern);
                endDate = transDateFormat(b, pattern);
            }
            if (1 <= unit && 4 >= unit) {
                result = getDistanceByUnit(startDate, endDate, unit);
                return result;
            }
            GregorianCalendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(startDate);
            int startYears = startCalendar.get(Calendar.YEAR);
            int startMonths = startCalendar.get(Calendar.MONTH);
            int startDays = startCalendar.get(Calendar.DAY_OF_MONTH);

            GregorianCalendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(endDate);
            int endYears = endCalendar.get(Calendar.YEAR);
            int endMonths = endCalendar.get(Calendar.MONTH);
            int endDays = endCalendar.get(Calendar.DAY_OF_MONTH);

            int yearBetween = endYears - startYears;
            int monthBetween = endMonths - startMonths;
            if (endDays < startDays && endDays != endCalendar.getActualMaximum(Calendar.DATE)) {
                monthBetween--;
            }
            if (ACCURACY_YEAR == unit) {
                if (monthBetween < 0) {
                    yearBetween--;
                }
                result = yearBetween;
            }
            if (ACCURACY_MONTH == unit) {
                result = (yearBetween * 12 + monthBetween);
            }
        }
        return result;

    }


    /**
     * 【方法功能描述】根据制定单位，计算两个日期之间的差（向上取整）
     *
     * @param a-时间点1
     * @param b-时间点2
     * @param unit-时间单位
     * @return 返回时间差
     * @comment
     * @history
     * @Version 1.0.0
     */
    public static int getDateDistanceMax(Date a, Date b, int unit) {
        int result = 0;
        if (null != a && null != b) {
            String pattern = null;
            switch (unit) {
                case ACCURACY_HOUR: // '\003'
                    pattern = "yyyyMMddHH";
                    break;
                case ACCURACY_MINUTE: // '\002'
                    pattern = "yyyyMMddHHmm";
                    break;
                case ACCURACY_SECOND: // '\001'
                    pattern = "yyyyMMddHHmmss";
                    break;
                default:
                    pattern = "yyyyMMdd";
            }
            Date startDate = null;
            Date endDate = null;
            if (a.compareTo(b) > 0) { // a > b
                startDate = transDateFormat(b, pattern);
                endDate = transDateFormat(a, pattern);
            } else { // a <= b
                startDate = transDateFormat(a, pattern);
                endDate = transDateFormat(b, pattern);
            }
            if (1 <= unit && 4 >= unit) {
                result = getDistanceByUnit(startDate, endDate, unit);
                return result;
            }
            GregorianCalendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(startDate);
            int startYears = startCalendar.get(Calendar.YEAR);
            int startMonths = startCalendar.get(Calendar.MONTH);
            int startDays = startCalendar.get(Calendar.DAY_OF_MONTH);

            GregorianCalendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(endDate);
            int endYears = endCalendar.get(Calendar.YEAR);
            int endMonths = endCalendar.get(Calendar.MONTH);
            int endDays = endCalendar.get(Calendar.DAY_OF_MONTH);

            int yearBetween = endYears - startYears;
            int monthBetween = endMonths - startMonths;
            if (endDays > startDays) {
                monthBetween++;
            }
            if (ACCURACY_YEAR == unit) {
                BigDecimal year = BigDecimal.ZERO;
                if (monthBetween > 0) {
                    BigDecimal month = new BigDecimal(monthBetween);
                    year = month.divide(new BigDecimal("12"), RoundingMode.CEILING);
                }
                result = year.intValue();
            }
            if (ACCURACY_MONTH == unit) {
                result = (yearBetween * 12 + monthBetween);
            }
        }
        return result;
    }

    /**
     * 内部方法，计算时间点的差距
     *
     * @param startDate 起始时间
     * @param endDate   终止时间
     * @param unit      时间单位
     * @return 时间差
     */
    private static int getDistanceByUnit(Date startDate, Date endDate, int unit) {
        if (null == startDate || null == endDate) {
            throw new IllegalArgumentException("illegal arguments, date is null");
        }

        int result = 0;
        long millisecondPerUnit = MILLISECONDS_PER_DAY;
        switch (unit) {
            case ACCURACY_HOUR:
                millisecondPerUnit = MILLISECONDS_PER_HOUR;
                break;
            case ACCURACY_MINUTE:
                millisecondPerUnit = MILLISECONDS_PER_MINUTE;
                break;
            case ACCURACY_SECOND:
                millisecondPerUnit = MILLISECONDS_PER_SECOND;
                break;
            default:
                break;
        }
        long start = startDate.getTime();
        long end = endDate.getTime();
        long distance = end - start;
        result = Integer.valueOf((distance / millisecondPerUnit) + "");
        return result;
    }

    /**
     * 返回指定日期是当年的第几周
     *
     * @param date 指定日期
     * @return 周数（从1开始）
     */
    public static int getWeekOfYear(Date date) {
        return getCalendar(date).get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取指定日期是星期几
     *
     * @param date 指定日期
     * @return 星期日--1; 星期一--2; 星期二--3; 星期三--4; 星期四--5; 星期五--6; 星期六--7;
     */
    public static int getWeekOfDate(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 判断指定年份日期的年份是否为闰年
     *
     * @param date 日期
     * @return 闰年ture，非闰年false
     */
    public static boolean isLeapYear(Date date) {
        int year = getCalendar(date).get(Calendar.YEAR);
        return isLeapYear(year);
    }

    /**
     * 判断指定年份日期的年份是否为闰年
     *
     * @param year 年份数字
     * @return 闰年ture，非闰年false
     */
    public static boolean isLeapYear(int year) {
        if ((year % 400) == 0) {
            return true;
        } else if ((year % 4) == 0) {
            if ((year % 100) == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 按照strFormat格式输出当前时间
     *
     * @param strFormat 格式
     * @return 指定格式的当前系统日期
     */
    public static String getCurrentDate(String strFormat) {
        return getDateStr(getCurrentDate(), strFormat);
    }

    /**
     * 校验日期数据（校验输入值是否为指定的日期格式）
     *
     * @param strDate   要校验的日期
     * @param strFormat 日期格式
     * @return true/false （符合/不符合）
     */
    public static boolean checkDate(String strDate, String strFormat) {
        Date date = null;
        if ((strDate != null) && (strDate.trim().length() != 0)) {
            DateFormat myDateFmt = new SimpleDateFormat(strFormat);
            try {
                date = myDateFmt.parse(strDate);

                if (!strDate.equals(myDateFmt.format(date))) {
                    date = null;
                    return false;
                }
            } catch (ParseException e) {
                date = null;
                return false;
            }
        }
        return true;
    }

    /**
     * Formats the given date.
     */
    public static String formatDate(Date date, Locale locale) {
        DateFormat formatter = createDateFormatter(locale);
        return formatter.format(date);
    }

    /**
     * Format a datetime into a specific pattern.
     */
    public static String formatDate(Date date, String pattern) {
        if (!StringUtils.isNotBlank(pattern)) {
            pattern = ZH_CN_DATETIME_PATTERN;
        }
        DateFormat formatter = createFormatter(pattern);
        return formatter.format(date);
    }

    public static DateFormat createFormatter(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * Format a datetime into a specific pattern.
     */
    public static String formatDate(Date date, String pattern, String timeZone) {
        if (!StringUtils.isNotBlank(pattern)) {
            pattern = ZH_CN_DATETIME_PATTERN;
        }
        if (!StringUtils.isBlank(timeZone)) {
            timeZone = "GMT+8";
        }
        DateFormat formatter = createFormatter(pattern, timeZone);
        return formatter.format(date);
    }

    public static DateFormat createFormatter(String pattern, String timeZone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return simpleDateFormat;
    }

    public static DateFormat createDateFormatter(Locale locale) {
        return DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
    }

    public static String formatDateTime(Object value, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        if (value == null) {
            return "";
        } else if (value instanceof String) {
            Date date = new Date();
            try {
                date = dateFormat.parse(StringUtil.getString(value));
            } catch (ParseException e) {
                return "";
            }
            return dateFormat.format(date);
        } else if (value instanceof Date) {
            return dateFormat.format(value);
        } else {
            return "";
        }
    }

    /**
     * 天数变更
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date adjustDay(Date date, int amount) {
        Validate.notNull(date);
        Calendar calendar = dateToCalendar(date);
        adjustCalendar(calendar, Calendar.DAY_OF_MONTH, amount);
        return calendarToDate(calendar);
    }

    /**
     * 月份变更
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date adjustMonth(Date date, int amount) {
        Validate.notNull(date);
        Calendar calendar = dateToCalendar(date);
        adjustCalendar(calendar, Calendar.MONTH, amount);
        return calendarToDate(calendar);
    }

    /**
     * 年度变更
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date adjustYear(Date date, int amount) {
        Validate.notNull(date);
        Calendar calendar = dateToCalendar(date);
        adjustCalendar(calendar, Calendar.YEAR, amount);
        return calendarToDate(calendar);
    }

    public static Calendar dateToCalendar(Date date) {
        Validate.notNull(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    public static void adjustCalendar(Calendar calendar, int field, int amount) {
        calendar.add(field, amount);
    }

    public static Date getDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();

        try {
            if (offset != null && !"".equals(offset)) {
                Date offsetDate = parseDate(offset, ZH_CN_DATETIME_PATTERN);
                calendar.setTime(offsetDate);
            }
        } catch (Exception e) {

        }

        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static Date getDateWithoutTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    /**
     * 根据配置调整时间 1d-1天, 1m-一月, 1y-一年
     *
     * @param date
     * @param scope
     * @return
     */
    public static final Date adjustDate(Date date, String scope) {
        scope = scope.trim();
        String mount = scope.substring(0, scope.length() - 1);
        char flag = scope.toUpperCase().toCharArray()[scope.length() - 1];
        Date resultDate = null;
        switch (flag) {
            case 'D':
                resultDate = DateUtils.addDays(date, Integer.parseInt(mount));
                break;
            case 'M':
                resultDate = DateUtils.addMonths(date, Integer.parseInt(mount));
                break;
            case 'Y':
                resultDate = DateUtils.addYears(date, Integer.parseInt(mount));
                break;
            default:
                throw new RuntimeException("date identifier error: " + flag);
        }
        return resultDate;
    }

    /**
     * 【方法功能描述】判断是否时间合法性
     *
     * @param str_input
     * @return
     * @comment
     * @history
     * @Version 1.0.0
     */
    public static boolean isDate(String str_input) {
        if ((str_input != null) && !"".equals(str_input)) {
            String rDateFormat = "yyyy-MM-dd";
            SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
            formatter.setLenient(false); // 如果去除这一行, "2007-10-33"将是合法日期
            try {
                formatter.format(formatter.parse(str_input));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return true;

    }

    /**
     * 获取当前月前一个月的第一天
     *
     * @return
     */
    public static Date getFirstDay(){
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, -1);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        Date beginDate = cale.getTime();
        return beginDate;
    }

    /**
     * 获取当前月的前一个月的最后一天
     *
     * @return
     */
    public static Date getLastDay(){
        Calendar cale1 = Calendar.getInstance();
        cale1.set(Calendar.DAY_OF_MONTH, 0);
        cale1.set(Calendar.HOUR_OF_DAY, 23);
        cale1.set(Calendar.MINUTE, 59);
        cale1.set(Calendar.SECOND, 59);
        Date endDay = cale1.getTime();
        return endDay;
    }


}