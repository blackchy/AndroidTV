package com.example.core.date;

import android.text.TextUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JCalendar为Calendar的增强类
 *
 */
public class JCalendar extends GregorianCalendar {


    public static final String[] CHINESE_WEEK = {"日", "一", "二", "三", "四", "五",
            "六"};
    // 中文数字
    public static final String[] CHINESE_NUM = {"零", "一", "二", "三", "四", "五",
            "六", "七", "八", "九", "十"};

    public static final long DAY = 24 * 60 * 60 * 1000;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public JCalendar(JCalendar calendar) {
        this();
        setTimeInMillis(calendar.getTimeInMillis());
    }

    public JCalendar(int year, int offsetTerm) {
        this();
        setYear(year);
        set(Calendar.DAY_OF_YEAR, 1);
        add(Calendar.DATE, offsetTerm);
    }

    public JCalendar setDate(int year, int mouth, int day) {
        set(year, mouth - 1, day);
        return this;
    }

    /**
     * 从文本创建
     *
     * @param str
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static JCalendar createFromString(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return new JCalendar(sdf.parse(str).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static JCalendar createFromString(String str, String timeZone) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            return new JCalendar(simpleDateFormat.parse(str).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 得到当前是星期几（第几周）
     *
     * @return
     */
    public String getCurrentWeekDay() {
        return getDayOfWeekAsString() + "(第" + getWeekOfYear() + "周)";
    }

    public String getFormatDate(String format) {
        Date date = new Date(getTimeInMillis());
        SimpleDateFormat s = new SimpleDateFormat(format, Locale.CHINA);
        return s.format(date);
    }

    public String getFormatDateWithTimeZone(String format, TimeZone timeZone) {
        Date date = new Date(getTimeInMillis());
        SimpleDateFormat s = new SimpleDateFormat(format, Locale.CHINA);
        s.setTimeZone(timeZone);
        return s.format(date);
    }


    /**
     * @param calendar
     */
    public JCalendar(Calendar calendar) {
        this();
        setTimeInMillis(calendar.getTimeInMillis());
    }

    public JCalendar(Date date) {
        this();
        setTimeInMillis(date.getTime());
    }

    public JCalendar() {
        super();
    }

    /**
     * Jcalendar
     *
     * @param year
     * @param month
     * @param day
     */
    public JCalendar(int year, int month, int day) {
        this();
        set(year, month - 1, day);
    }

    public long getTimes() {
        long curtTime = getTimeInMillis();
        JCalendar calendar = JCalendar.getInstance();
        calendar.setTimeInMillis(this.getTimeInMillis());
        calendar.clearTime();
        long clearTime = calendar.getTimeInMillis();
        return curtTime - clearTime;
    }


    /**
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param min
     * @param seconds
     */
    public JCalendar(int year, int month, int day, int hour, int min, int seconds) {
        this();
        set(year, month - 1, day, hour, min, seconds);
    }

    public JCalendar(long time) {
        this();
        setTimeInMillis(time);
    }

    public JCalendar(int solarYear, int dayOffset, int hour, int minutes, int seconds) {
        this();
        setYear(solarYear);
        setDayOfYear(dayOffset);
        setTime(hour, minutes, seconds);
    }

    /**
     * 判断当前日期是否有效
     * 1901~2099
     *
     * @return
     */
    public boolean isAllowed() {
        if (getYear() < 1901) {
            return false;
        } else if (getYear() > 2099) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置在当前年中的天
     *
     * @param days from 0
     */
    public void setDayOfYear(int days) {
        set(Calendar.DAY_OF_YEAR, days);
    }

    /**
     * 设置时间
     *
     * @param hour
     * @param minutes
     * @param seconds
     */
    public void setTime(int hour, int minutes, int seconds) {
        set(Calendar.HOUR_OF_DAY, hour);
        set(Calendar.MINUTE, minutes);
        set(Calendar.SECOND, seconds);
        set(Calendar.MILLISECOND, 0);
    }

    @Override
    public void set(int field, int value) {
        super.set(field, value);
        mLunarInfo = null;
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public static JCalendar getInstance() {
        return new JCalendar();
    }

    /**
     * 用于缓存SimpleDateFormat
     */
    private static ConcurrentHashMap<String, SimpleDateFormat> mDateFormatMap = new ConcurrentHashMap<String, SimpleDateFormat>();

    /**
     * 根据字符串及格式获取JCalendar实例
     *
     * @param dateString
     * @param format
     * @return
     */
    public static JCalendar parseString(String dateString, String format) throws ParseException {
        SimpleDateFormat sdf = mDateFormatMap.get(format);
        if (sdf == null) {
            sdf = new SimpleDateFormat(format);
            mDateFormatMap.put(format, sdf);
        }
        if (dateString.equals("现在")) {
            return JCalendar.getInstance();
        } else {
            long time = sdf.parse(dateString).getTime();
            return new JCalendar(time);
        }
    }


    public static JCalendar parseString(String dateString, String format, String timeZone) throws ParseException {

        SimpleDateFormat sdf = mDateFormatMap.get(format);
        if (sdf == null) {
            sdf = new SimpleDateFormat(format, Locale.CHINA);
            sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        if (dateString.equals("现在")) {
            return JCalendar.getInstance();
        } else {
            long time = sdf.parse(dateString).getTime();
            return new JCalendar(time);
        }
    }

    /*public static JCalendar parseString(String dateString, String format, String timeZone) throws ParseException {
        SimpleDateFormat sdf = mDateFormatMap.get(format);
        if (sdf == null) {
            sdf = new SimpleDateFormat(format, Locale.CHINA);
            sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        long time = sdf.parse(dateString).getTime();
        JCalendar calendar = new JCalendar(time);
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        return calendar;
    }*/

    /**
     * 清除时间信息
     * <p/>
     * 比如清除小时分钟
     */
    public JCalendar clearTime() {
        set(HOUR_OF_DAY, 0);
        set(MINUTE, 0);
        set(SECOND, 0);
        set(MILLISECOND, 0);
        return this;
    }

    /**
     * 一天开始的时间
     *
     * @return
     */
    public JCalendar getBeginOfDay() {
        return clone().clearTime();
    }

    public long getBeginOfDayMs() {
        return getBeginOfDay().getTimeInMillis();
    }

    public long getEndOfDayMs() {
        JCalendar _calendar = clone();
        _calendar.clearTime();
        _calendar.addDays(1);
        return _calendar.getTimeInMillis() - 1;
    }

    /**
     * 获取一天结束的时间
     *
     * @return
     */
    public JCalendar getEndOfDay() {
        JCalendar _instance = clone();
        _instance.clearTime();
        _instance.add(DATE, 1);
        _instance.add(SECOND, -1);
        return _instance;
    }

    /**
     * 获取与calendar中相差多少天
     *
     * @param calendar
     * @return
     */
    public long getIntervalDays(JCalendar calendar) {
//        public static long getIntervalDays(Calendar source, Calendar target) {
//            int toffset = target.get(Calendar.DST_OFFSET) + target.get(Calendar.ZONE_OFFSET);
//            int soffset = source.get(Calendar.DST_OFFSET) + source.get(Calendar.ZONE_OFFSET);
//            return (JCalendar.clearTime(target.getTimeInMillis()) + toffset) / DAYS - (JCalendar.clearTime(source.getTimeInMillis()) + soffset) / DAYS;
//        }
        if (calendar == null) {
            return 0;
        }
        return (clearTime(getTimeInMillis()) + getUTCOffset()) / DAY - (clearTime(calendar.getTimeInMillis()) + calendar.getUTCOffset()) / DAY;
        //return (clearTime(getTimeInMillis()) ) / DAY - (clearTime(calendar.getTimeInMillis())) / DAY;
    }

    public long getIntervalDaysWithOutTimeZone(JCalendar calendar) {
//        public static long getIntervalDays(Calendar source, Calendar target) {
//            int toffset = target.get(Calendar.DST_OFFSET) + target.get(Calendar.ZONE_OFFSET);
//            int soffset = source.get(Calendar.DST_OFFSET) + source.get(Calendar.ZONE_OFFSET);
//            return (JCalendar.clearTime(target.getTimeInMillis()) + toffset) / DAYS - (JCalendar.clearTime(source.getTimeInMillis()) + soffset) / DAYS;
//        }
        if (calendar == null) {
            return 0;
        }
        this.setHour(0);
        this.setMin(0);
        this.set(SECOND, 0);
        calendar.setHour(0);
        calendar.setMin(0);
        calendar.set(SECOND, 0);
        return (getTimeInMillis()) / DAY - (calendar.getTimeInMillis()) / DAY;
        //return (clearTime(getTimeInMillis()) ) / DAY - (clearTime(calendar.getTimeInMillis())) / DAY;
    }


    public static long getIntervalDays1(Calendar source, Calendar target) {
        int toffset = target.get(Calendar.DST_OFFSET) + target.get(Calendar.ZONE_OFFSET);
        int soffset = source.get(Calendar.DST_OFFSET) + source.get(Calendar.ZONE_OFFSET);
        return (JCalendar.clearTime(target.getTimeInMillis()) + toffset) / DAY - (JCalendar.clearTime(source.getTimeInMillis()) + soffset) / DAY;
    }

    public static long clearTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 相差多少个自然月
     *
     * @param calendar
     * @return
     */
    public int getIntervalMonths(JCalendar calendar) {
        int sy = getYear();
        int dy = calendar.getYear();
        int sm = getMonth();
        int dm = calendar.getMonth();
        return (sy - dy) * 12 + (sm - dm);
    }

    /**
     * 加多少天
     *
     * @param days
     */
    public void addDays(int days) {
        add(Calendar.DATE, days);
    }

    @Override
    public void add(int field, int value) {
        super.add(field, value);
        mLunarInfo = null;
    }

    @Override
    public void setTimeInMillis(long milliseconds) {
        super.setTimeInMillis(milliseconds);
        mLunarInfo = null;
    }

    /**
     * 设置时间
     *
     * @param milliseconds
     * @param updateLunar
     */
    public JCalendar setTimeInMillis(long milliseconds, boolean updateLunar) {
        super.setTimeInMillis(milliseconds);
        mLunarInfo = null;
        return this;
    }

    /**
     * 获取天
     *
     * @return
     */
    public String getDayAsString() {
        int day = getDay();
        String res = day + "";
        if (day < 10) {
            res = "0" + day;
        }

        return res;
    }


    /**
     * 获取天
     *
     * @return
     */
    public int getDay() {
        return get(Calendar.DATE);
    }

    /**
     * 设置Date
     *
     * @param day
     */
    public void setDay(int day) {
        set(Calendar.DATE, day);
    }

    /**
     * 获取月
     *
     * @return
     */
    public int getMonth() {
        return get(Calendar.MONTH) + 1;
    }

    /**
     * 设置月份
     *
     * @param month
     */
    public void setMonth(int month) {
        set(Calendar.MONTH, month - 1);
    }

    /**
     * 获取年
     *
     * @return
     */
    public int getYear() {
        return get(Calendar.YEAR);
    }

    /**
     * 设置年份
     *
     * @param year
     */
    public void setYear(int year) {
        set(Calendar.YEAR, year);
    }

    /**
     * 获取星期几
     *
     * @return
     */
    public int getDayOfWeek() {
        return get(Calendar.DAY_OF_WEEK);
    }

    public String getDayOfWeekAsString() {

        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

        return weeks[getDayOfWeek() - 1];
    }

    public String getDayOfWeekAsString2() {

        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        return weeks[getDayOfWeek() - 1];
    }

    public String getWeekAsString(int index) {

        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

        return weeks[index - 1];
    }


    /**
     * 获取本年度的多少周
     *
     * @return
     */
    public int getWeekOfYear() {
        return get(Calendar.WEEK_OF_YEAR);
    }


    /**
     * 是否为今天
     *
     * @return
     */
    public boolean isToday() {
        return eqDay(getNOW());
    }


    /**
     * 是否为当月
     *
     * @return
     */
    public boolean isToMonth() {
        return eqMonth(getNOW());
    }

    /**
     * 是否为当星期
     *
     * @return
     */
    public boolean isToWeek() {
        return eqWeek(getNOW());
    }

    /**
     * 是否为当年
     *
     * @return
     */
    public boolean isToYear() {
        return eqYear(getNOW());
    }

    /**
     * 同年
     *
     * @param now
     * @return
     */
    public boolean eqYear(JCalendar now) {
        return getYear() == now.getYear();
    }


    /**
     * 同月
     *
     * @param now
     * @return
     */
    public boolean eqMonth(JCalendar now) {
        return (getYear() == now.getYear() && getMonth() == now.getMonth());
    }

    /**
     * 同星期
     *
     * @param now
     * @return
     */
    public boolean eqWeek(JCalendar now) {
        return (getYear() == now.getYear()) && getWeekOfYear() == now.getWeekOfYear();
    }

    /**
     * 同星X
     *
     * @param now
     * @return
     */
    public boolean eqWeekDay(JCalendar now) {
        return (getYear() == now.getYear()) && getWeekOfYear() == now.getWeekOfYear() && getDayOfWeek() == now.getDayOfWeek();
    }

    /**
     * 是否是相同的星期几
     *
     * @param calendar
     * @return
     */
    public boolean isSameWeekDay(JCalendar calendar) {
        return getDayOfWeek() == calendar.getDayOfWeek();
    }

    /**
     * 是否周末
     *
     * @return
     */
    public boolean isWeekEnd() {
        return getDayOfWeek() == SUNDAY || getDayOfWeek() == SATURDAY;
    }


    /**
     * 获取当月的第一天
     *
     * @param firstOfWeek 周首日
     * @return
     */
    public JCalendar getFirstDayInMonth(int firstOfWeek) {
        JCalendar _instance = clone();
        _instance.setFirstDayOfWeek(firstOfWeek);
        _instance.set(Calendar.DAY_OF_MONTH, 1);
        _instance.set(Calendar.DAY_OF_WEEK, firstOfWeek);
        return _instance;
    }


    /**
     * 复制一个新JCalendar对象
     *
     * @return
     */
    public JCalendar clone() {
        JCalendar _instance = new JCalendar(getTimeInMillis());
        return _instance;
    }


    private final static JCalendar NOW = new JCalendar();

    /**
     * 获取当前时间
     *
     * @return
     */
    public static final JCalendar getNOW() {
        NOW.setTimeInMillis(System.currentTimeMillis());
        return NOW;
    }


    public boolean eqDay(JCalendar target) {
        return (getYear() == target.getYear() && getMonth() == target.getMonth() && getDay() == target.getDay());
    }


    /**
     * 同年
     *
     * @param calendar
     * @return
     */
    public boolean sameYear(JCalendar calendar) {
        return getYear() == calendar.getYear();
    }

    /**
     * 同一个农历年
     *
     * @param calendar
     * @return
     */
    public boolean sameLunarYear(JCalendar calendar) {
        return getLunarYear() == calendar.getLunarYear();
    }

    /**
     * 同一个农历月
     *
     * @param calendar
     * @return
     */
    public boolean sameLunarMonth(JCalendar calendar) {
        return sameLunarYear(calendar) && getLunarMonth() == calendar.getLunarMonth() && isLunarLeapMonth() == calendar.isLunarLeapMonth();
    }

    /**
     * 同一个农历日
     *
     * @param calendar
     * @return
     */
    public boolean sameLunarDay(JCalendar calendar) {
        return sameLunarMonth(calendar) && calendar.getLunarDate() == getLunarDate();
    }


    public String getLunarShow() {
        return getYear() + "年" + getLunarMonthAsString() + getLunarDateAsString();
    }

    /**
     * 是否为同月
     *
     * @param calendar
     * @return
     */
    public boolean sameMonth(JCalendar calendar) {
        return sameYear(calendar) && getMonth() == calendar.getMonth();
    }

    /**
     * 同天
     *
     * @param calendar
     * @return
     */
    public boolean sameDay(JCalendar calendar) {
        return sameMonth(calendar) && getDay() == calendar.getDay();
    }


    public boolean sameDayWidthTimeZone(JCalendar calendar) {
        calendar.setTimeZone(this.getTimeZone());
        return sameDay(calendar);
    }

    /**
     * 同小时
     *
     * @param calendar
     * @return
     */
    public boolean sameHour(JCalendar calendar) {
        return sameDay(calendar) && getHour() == calendar.getHour();
    }

    public boolean sameMin(JCalendar calendar) {
        return sameDay(calendar) && getMinutes() == calendar.getMinutes();
    }


    public boolean sameHourWidthTimeZone(JCalendar calendar) {
        calendar.setTimeZone(this.getTimeZone());
        return sameDay(calendar) && getHour() == calendar.getHour();
    }

    /**
     * 是否是同一天（号数相同）
     *
     * @param calendar
     * @return
     */
    public boolean isSameDay(JCalendar calendar) {
        return getDay() == calendar.getDay();
    }

    /**
     * 设置年份
     *
     * @param year
     * @param jump 是否自动跳日
     */
    public void setYear(int year, boolean jump) {
        int day = getDay();
        setDay(1);
        setYear(year);
        setDay(day, jump);
    }

    /**
     * 设置月份
     *
     * @param month
     * @param jump  自动跳
     */
    public void setMonth(int month, boolean jump) {
        int day = getDay();
        setDay(1);
        setMonth(month);
        setDay(day, jump);
    }

    /**
     * 设置Date
     *
     * @param day
     * @param jump 是否自动跳到下一月
     */
    public void setDay(int day, boolean jump) {
        if (!jump) {
            day = fixDay(day);
        }
        setDay(day);
    }


    private int fixDay(int day) {
        return day > getMaxDaysInMonth() ? getMaxDaysInMonth() : day;
    }


    /**
     * 当前月最多的天数
     *
     * @return
     */
    public int getMaxDaysInMonth() {
        return getActualMaximum(Calendar.DATE);
    }

    /**
     * 当年最多多少月
     *
     * @return
     */
    public int getMaxMonthsInYear() {
        return getActualMaximum(Calendar.MONTH);
    }


    /**
     * 是否在calendar之前
     *
     * @param calendar
     * @param ignoretime 是否忽略时间
     * @return
     */
    public boolean before(JCalendar calendar, boolean ignoretime) {
        long source = getTimeInMillis();
        long target = calendar.getTimeInMillis();
        if (ignoretime) {
            source = source / DAY * DAY;
            target = target / DAY * DAY;
        }
        return source <= target;
    }

    /**
     * 是否在Calendar 之后
     *
     * @param calendar
     * @param ignoretime
     * @return
     */
    public boolean after(JCalendar calendar, boolean ignoretime) {
        long source = getTimeInMillis();
        long target = calendar.getTimeInMillis();
        if (ignoretime) {
            source = source / DAY * DAY;
            target = target / DAY * DAY;
        }
        return source >= target;
    }

    /**
     * 设置分钏
     *
     * @param min
     */
    public void setMin(int min) {
        set(Calendar.MINUTE, min);
    }

    /**
     * return hour
     *
     * @return
     */
    public int getHour() {
        return get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 设置时间
     *
     * @param hour
     */
    public JCalendar setHour(int hour) {
        set(Calendar.HOUR_OF_DAY, hour);
        return this;
    }

    /**
     * return minutes;
     *
     * @return
     */
    public int getMinutes() {
        return get(Calendar.MINUTE);
    }

    /**
     * @param days
     * @return
     */
    public JCalendar getNextDay(int days) {
        JCalendar _instance = clone();
        _instance.addDays(days);
        return _instance;
    }

    public JCalendar getNextHour(int hours) {
        JCalendar _instance = clone();
        _instance.add(Calendar.HOUR_OF_DAY, hours);
        return _instance;
    }

    public long getUTCTime() {
        JCalendar _calendar = clone();
        int dstoffset = _calendar.get(Calendar.DST_OFFSET);
        int toneOffset = _calendar.get(Calendar.ZONE_OFFSET);
        _calendar.add(Calendar.MILLISECOND, (dstoffset + toneOffset));
        return _calendar.getTimeInMillis();
    }

    /**
     * 添加月
     *
     * @param month
     */
    public void addMonths(int month) {
        add(MONTH, month);
    }

    /**
     * @param months
     * @return
     */
    public JCalendar getNextMonth(int months) {
        JCalendar _instance = clone();
        _instance.addMonths(months);
        return _instance;
    }


    public JCalendar getNextYear(int years) {
        JCalendar _instance = clone();
        _instance.addYears(years);
        return _instance;
    }

    /**
     * 添加年
     *
     * @param years
     */
    public void addYears(int years) {
        add(Calendar.YEAR, years);
    }


    /**
     * @return
     */
    public int getMaxDays() {
        return getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取一年最多有多少天
     *
     * @param ly
     * @return
     */
    public static int getMaxDayAtYear(int ly) {
        JCalendar calendar = new JCalendar();
        calendar.setYear(ly);
        return (calendar.isLeapYear(ly) ? 1 : 0) + 365;
    }

    private JLunar mLunarInfo = null;//农历缓存对象


    //是否需要更新农历
    private boolean mNeedUpdateLunar = false;


    /**
     * 获取当年最多有多少天
     *
     * @return
     */
    public int getMaxDayInYear() {
        return 365 + (isLeapYear(getYear()) ? 1 : 0);
    }


    /**
     * 获取农历信息
     *
     * @return
     */
    public synchronized JLunar getLunarInfo() {
        if (!hasLunarInfo()) {
            mLunarInfo = null;
            return null;
        }
        if (mLunarInfo == null) {
            mLunarInfo = JLunar.getLunarFromSolar(this, null);
            mStemsBranch = StemsBranch.getStemsBranch(this);
        }
        return mLunarInfo;
    }

    /**
     * 获取节气信息
     *
     * @return
     */
    public StemsBranch getStemsBranch() {
        if (mStemsBranch == null || mLunarInfo == null) {
            mLunarInfo = JLunar.getLunarFromSolar(this, null);
            mStemsBranch = StemsBranch.getStemsBranch(this);
        }
        return mStemsBranch;
    }


    public String getExpandDate() {

        Date tasktime = getTime();
        //设置日期输出的格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
        //格式化输出
        return df.format(tasktime);
    }


    /**
     * 得到当天所以的值神信息
     *
     * @return
     */
    public ArrayList<String> getShen12Array(JCalendar calendar) {
        return getStemsBranch().get12ShenArray(calendar);
    }


    /**
     * 得到当天的时辰宜忌
     *
     * @param calendar
     * @return
     */
    public List<String> getScArray(JCalendar calendar) {
        return getStemsBranch().getScArray(calendar);
    }

    /**
     * 是否有农历信息
     *
     * @return
     */
    public boolean hasLunarInfo() {
        return JLunar.hasLunarInfo(this);
    }

    public int getLunarYear() {
        if (hasLunarInfo()) {
            return getLunarInfo().getLunarYear();
        }
        return -1;
    }

    public int getLunarMonth() {
        if (hasLunarInfo()) {
            return getLunarInfo().getLunarMonth();
        }
        return -1;
    }

    /**
     * 是否在闰月或者闰月之后
     *
     * @return
     */
    public boolean isAfterLunarLeap() {
        return ((getLunarMonth() > getLunarLeapMonth()) || isLunarLeapMonth()) && (getLunarLeapMonth() > 0);
    }

    public int getLunarDate() {
        if (hasLunarInfo()) {
            return getLunarInfo().getLunarDate();
        }
        return -1;
    }


    public boolean isLunarLeapMonth() {
        if (hasLunarInfo()) {
            return getLunarInfo().isLeap();
        }
        return false;
    }

    public String getLunarYearAsString() {
        if (!hasLunarInfo()) {
            return "";
        }
        return getLunarInfo().getLunarYearAsString();
    }


    public String getLunarMonthAsString() {
        if (!hasLunarInfo()) {
            return "";
        }
        return getLunarInfo().getLunarMonthAsString();
    }

    public String getLunarDateAsString() {
        if (!hasLunarInfo()) {
            return "";
        }
        return getLunarInfo().getLunarDateAsString();
    }

    public String getLunarDateAsString1() {
        if (!hasLunarInfo()) {
            return "";
        }
        return getLunarInfo().getLunarDateAsString1();
    }

    public String toLunarString() {
        if (!hasLunarInfo()) {
            return "";
        }
        return getLunarInfo().toLunarString();
    }

    /**
     * 获取当天为当年的第几天
     * <p/>
     * 第一天为0
     *
     * @return
     */
    public int getDayofYear() {
        return get(Calendar.DAY_OF_YEAR) - 1;
    }

    /**
     * 获取当前年的农历润月
     *
     * @return
     */
    public int getLunarLeapMonth() {
        return getLunarLeapMonth(getLunarYear());
    }


    /**
     * 获取农历润月
     *
     * @param lunarYear
     * @return <=0表示没有
     * >0表示润月月份
     */
    public static int getLunarLeapMonth(int lunarYear) {
        return JLunar.getLunarLeapMonth(lunarYear);
    }


    /**
     * 获取两个日期的间隔天数
     *
     * @param source
     * @param target
     * @return
     */
    public static int getIntervalDays(JCalendar source, JCalendar target) {
        int dayCounts = -1;
        boolean reverted = false;
        JCalendar tmp = null;
        if (source.compareTo(target) < 0) {
            tmp = source;
            source = target;
            target = tmp;
            reverted = true;
        }
        if (source.getYear() == target.getYear())// 如果为同年
        {
            dayCounts += source.getDayofYear() - target.getDayofYear() + 1;
        } else {
            dayCounts += target.getMaxDayInYear() - target.getDayofYear()
                    + source.getDayofYear() + 1;
            for (int i = target.getYear() + 1; i < source.getYear(); i++) {
                dayCounts += getMaxDayAtYear(i);
            }
        }
        return (reverted ? (0 - dayCounts) : dayCounts);
    }


    /**
     * 获取秒
     */
    public int getSeconds() {
        return get(Calendar.SECOND);
    }


    public static final int LUNAR_YEAR = 101;
    public static final int LUNAR_MONTH = 102;
    public static final int LUNAR_DATE = 103;
    public static final int LUNAR_LEAP = 104;

    private boolean mNeedUpdateSolar = false;

    public void setLunar(int field, int value) {
        JLunar lunar = getLunarInfo();
        switch (field) {
            case LUNAR_YEAR:
                lunar.setLunarYear(value);
                break;
            case LUNAR_MONTH:
                lunar.setLunarMonth(value);
                break;
            case LUNAR_DATE:
                lunar.setLunarDate(value);
                break;
            case LUNAR_LEAP:
                lunar.setLeap(value == 1);
                break;
        }
        updateSolar(lunar);
    }

    /**
     * 更新公历信息
     */
    public JCalendar updateSolar(JLunar lunar) {
        JLunar.updateSolarFromLunar(lunar, this);
        return this;
    }

    @Override
    public int get(int field) {
        return super.get(field);
    }

    public void setLunarYear(int lunarYear) {
        setLunar(LUNAR_YEAR, lunarYear);
    }

    public void setLunarMonth(int lunarMonth, boolean isleap) {

        JLunar lunar = getLunarInfo();
        lunar.setLunarMonth(lunarMonth, isleap);
        updateSolar(lunar);
    }

    public void setLunarDate(int lunarDate) {
        setLunar(LUNAR_DATE, lunarDate);
    }

    private StemsBranch mStemsBranch = null;


    /**
     * 获取节气信息
     *
     * @return
     */
    public StemsBranch getStemsBranch(boolean force) {
        if (mStemsBranch == null || force) {
            mStemsBranch = StemsBranch.getStemsBranch(this);
        }
        return mStemsBranch;
    }

    public String getStemsBranchAsString() {
        return getStemsBranchYearAsString() + "年 " + getStemsBranchMonthAsString() + "月 " + getStemsBranchDayAsString() + "日 " + getStemsBranchHourAsString() + "时";
    }

    /**
     * 获取生肖
     *
     * @return
     */
    public String getAnimal() {
        if (getStemsBranch() == null) {
            return "";
        }
        return getStemsBranch().getAnim();
    }

    public int getStemsBranchMonth() {
        if (getStemsBranch() == null) {
            return -1;
        }
        return getStemsBranch().getStemsBranchMonth();
    }

    public int getStemsBranchYear() {
        if (getStemsBranch() == null) {
            return -1;
        }
        return getStemsBranch().getStemsBranchYear();
    }

    public int getStemsBranchDay() {
        if (getStemsBranch() == null) {
            return -1;
        }
        return getStemsBranch().getStemsBranchDay();
    }

    public int getStemsBranchHour() {
        if (getStemsBranch() == null) {
            return -1;
        }
        return getStemsBranch().getStemsBranchHour();
    }

    public String getStemsBranchHourAsString() {
        if (getStemsBranch() == null) {
            return "";
        }
        return getStemsBranch().getStemsBranchHourAsString();
    }

    public String getStemsBranchYearAsString() {
        if (getStemsBranch() == null) {
            return "";
        }
        return getStemsBranch().getStemsBranchYearAsString();
    }

    public String getStemsBranchMonthAsString() {
        if (getStemsBranch() == null) {
            return "";
        }
        return getStemsBranch().getStemsBranchMonthAsString();
    }

    public String getStemsBranchDayAsString() {
        if (getStemsBranch() == null) {
            return "";
        }
        return getStemsBranch().getStemsBranchDayAsString();
    }

    public String getTermsAsString() {
        if (getStemsBranch() == null) {
            return "";
        }
        return getStemsBranch().getTermsAsString();
    }

    public String getTermsAlarmString() {
        if (getStemsBranch() == null) {
            return "";
        }
        return getStemsBranch().getTermsAlarmSentence();
    }


    /**
     * 得到显示月日时间
     *
     * @return
     */
    public String getMonthAndDayStr() {
        return getMonth() + "月" + getDay() + "日";
    }


    /**
     * 得到日期
     *
     * @return
     */
    public String getString() {
        return getYear() + "" + getMonth() + "" + getDay();
    }

    public String getLocalSolarString() {
        return getYear() + "年" + getMonth() + "月" + getDay() + "日";
    }

    public String getLocalLunarString() {
        return getYear() + "年" + getLunarMonthAsString() + getLunarDateAsString();
    }


    public int getTerms() {
        if (getStemsBranch() == null) {
            return -1;
        }
        return getStemsBranch().getTerms();
    }

    public int getAnimYear() {
        if (getStemsBranch() == null) {
            return -1;
        }
        return getStemsBranch().getAnimYear();
    }

    /**
     * 得到生肖年
     *
     * @return
     */
    public String getAnimalYearAsString() {
        if (getStemsBranch() == null) {
            return null;
        }
        return getStemsBranch().getAnim();
    }

    /**
     * 得到农历月是大月还是小月
     *
     * @return
     */
    public String getLunarDateBigOrSmall() {
        if (getDaysInLunar() > 29) {
            return " (大) ";
        } else {
            return " (小) ";
        }
    }


    /**
     * 得到中国星座简介
     *
     * @param strStar
     * @return
     */
    public String getChineseConstellationInfo(String strStar) {


        if ("星纪".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[0];
        } else if ("玄拐".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[1];
        } else if ("娵訾".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[2];
        } else if ("降娄".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[3];
        } else if ("大梁".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[4];
        } else if ("实沉".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[5];
        } else if ("鹑首".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[6];
        } else if ("鹑火".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[7];
        } else if ("鹑尾".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[8];
        } else if ("寿星".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[9];
        } else if ("大火".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[10];
        } else if ("析木".equals(strStar)) {
            return StemsBranch.CHINESES_STAR_ARR[11];
        } else {
            return "暂无相关星座介绍";
        }

    }


    /**
     * 得到中国星座
     *
     * @return
     */
    public String getChineseConstellation() {
        int lunarMonth = getLunarMonth();
        int day = getLunarDate();
        String name = null;
        switch (lunarMonth) {
            case 1:
                if (day <= 5) {
                    name = "星纪";
                } else {
                    name = "玄拐";
                }
                break;
            case 2:
                if (day <= 3) {
                    name = "玄拐";
                } else {
                    name = "娵訾";
                }
                break;
            case 3:
                if (day <= 5) {
                    name = "娵訾";
                } else {
                    name = "降娄";
                }
                break;
            case 4:
                if (day <= 4) {
                    name = "降娄";
                } else {
                    name = "大梁";
                }
                break;
            case 5:
                if (day <= 5) {
                    name = "大梁";
                } else {
                    name = "实沉";
                }
                break;
            case 6:
                if (day <= 5) {
                    name = "实沉";
                } else {
                    name = "鹑首";
                }
                break;
            case 7:
                if (day <= 6) {
                    name = "鹑首";
                } else {
                    name = "鹑火";
                }
                break;
            case 8:
                if (day <= 7) {
                    name = "鹑火";
                } else {
                    name = "鹑尾";
                }
                break;
            case 9:
                if (day <= 7) {
                    name = "鹑尾";
                } else {
                    name = "寿星";
                }
                break;
            case 10:
                if (day <= 7) {
                    name = "寿星";
                } else {
                    name = "大火";
                }
                break;
            case 11:
                if (day <= 8) {
                    name = "大火";
                } else {
                    name = "析木";
                }
                break;
            case 12:
                if (day <= 6) {
                    name = "析木";
                } else {
                    name = "星纪";
                }
                break;
            default:
                break;
        }
        return name;
    }

    /**
     * 获取在当前农历年中一共有多少个月
     *
     * @return
     */
    public int getMonthsInLunar() {
        return JLunar.getMonthsInLunar(getLunarYear());
    }

    /**
     * 获取当前月中最多的农历日
     *
     * @return
     */
    public int getDaysInLunar() {
        return JLunar.getLunarMaxDayInMonth(getLunarYear(), getLunarMonth(), isLunarLeapMonth());
    }


    public static final String[] constellationArr = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
            "天蝎座", "射手座", "摩羯座"};

    public static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};


    /**
     * 获取西方星座解释
     *
     * @return
     */
    public String getWestStarIntroduction() {

        int month = get(Calendar.MONTH);
        int day = get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return StemsBranch.WEST_STAR_ARR[month];
        }

        return StemsBranch.WEST_STAR_ARR[11];

    }

    /**
     * 根据日期获取星座
     *
     * @return
     */
    public int getConstellationIndex() {
        int month = get(Calendar.MONTH);
        int day = get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return month;
        }
        //default to return 魔羯
        return 11;
    }


    /**
     * 根据日期获取星座
     *
     * @return
     */
    public String getConstellation() {
        int month = get(Calendar.MONTH);
        int day = get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        //default to return 魔羯
        return constellationArr[11];
    }

    public int getDayOfWeekInMonth() {
        return get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    public int getLunarHour() {
        return (getHour() + 1) / 2 % 12;
    }

    /**
     * 更新时间
     */
    public void updateTime() {
        JCalendar now = getNOW();
        set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        set(Calendar.MINUTE, now.get(Calendar.MINUTE));
        set(Calendar.SECOND, now.get(Calendar.SECOND));
        set(Calendar.MILLISECOND, now.get(Calendar.MILLISECOND));
    }

    public static final int MIN_YEAR = 1901;

    public static final int MAX_YEAR = 2099;


    public boolean hasPreDay() {
        return getYear() > MIN_YEAR || (getMonth() >= 1 && getDay() > 1 && getYear() == MIN_YEAR);
    }

    public boolean hasNextDay() {
        return getYear() < MAX_YEAR || (getMonth() <= 12 && getDay() < 31 && getYear() == MAX_YEAR);
    }

    public boolean isValid() {
        return getYear() >= MIN_YEAR && getYear() <= MAX_YEAR;
    }

    public static JCalendar from(Calendar date) {
        return new JCalendar(date);
    }

    public boolean canPreMonth() {
        return getYear() > MIN_YEAR || (getMonth() >= 1 && getYear() == MIN_YEAR);
    }

    public boolean canNextMonth() {
        return getYear() < MAX_YEAR || (getMonth() <= 12 && getYear() == MAX_YEAR);
    }

    public void updateSolar() {
//        updateSolar(getLunarInfo());
    }

    public String get39Name() {
        return getDogDayName() + getColdName();
    }

    /**
     * 获取三伏九九信息
     *
     * @return
     */
    public String get39Info() {
        return getColdInfo() + getDogDayInfo();
    }

    public String getDogDayName() {
        JCalendar[] dogDays = getDogDaysBeginDates(getLunarYear());
        if (dogDays == null || dogDays.length < 3) {
            return "";
        }
        int interval1 = (int) getIntervalDays(dogDays[0]);
        int interval2 = (int) getIntervalDays(dogDays[1]);
        int interval3 = (int) getIntervalDays(dogDays[2]);
        if (interval1 >= 0 && interval2 < 0 && interval1 < 1) {
            return "初伏";
        } else if (interval2 >= 0 && interval3 < 0 && interval2 < 1) {
            return "中伏";
        } else if (interval3 >= 0 && interval3 < 1) {
            return "末伏";
        }
        return "";
    }

    public String getColdName() {
        int daysInterval = (int) (getIntervalDays(getColdBeginDate()));
        if (daysInterval >= 0) {
            int section = (daysInterval / 9);
            int row = (daysInterval % 9 + 1);
            if (section >= 0 && section < 9 && row == 1) {
                return JLunar.CHINESE_NUM[section + 1] + "九";
            }
        }
        return "";
    }

    static final String DOG_FIRST_FORMATE = "初伏第%d天";

    static final String DOG_SECOND_FORMATE = "中伏第%d天";

    static final String DOG_THIRD_FORMATE = "末伏第%d天";

    static final String COLD_FORMATE = "%s九第%s天";

    /**
     * 获取当前时间九九的信息
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getColdInfo() {
        int daysInterval = (int) (getIntervalDays(getColdBeginDate()));
        if (daysInterval >= 0) {
            int section = (daysInterval / 9);
            int row = (daysInterval % 9 + 1);
            if (section >= 0 && section < 9) {
                return String.format(Locale.getDefault(), COLD_FORMATE,
                        JLunar.CHINESE_NUM[section + 1], JLunar.CHINESE_NUM[row]);
            }
        }
        return "";
    }


    /**
     * 获取伏天信息
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getDogDayInfo() {
        JCalendar[] dogDays = getDogDaysBeginDates(getLunarYear());
        if (dogDays == null || dogDays.length < 3) {
            return "";
        }
        int interval1 = (int) getIntervalDays(dogDays[0]);
        int interval2 = (int) getIntervalDays(dogDays[1]);
        int interval3 = (int) getIntervalDays(dogDays[2]);
        if (interval1 >= 0 && interval2 < 0) {
            return String.format(Locale.getDefault(), DOG_FIRST_FORMATE,
                    interval1 + 1);
        } else if (interval2 >= 0 && interval3 < 0) {
            return String.format(Locale.getDefault(), DOG_SECOND_FORMATE,
                    interval2 + 1);
        } else if (interval3 >= 0 && interval3 < 10) {
            return String.format(Locale.getDefault(), DOG_THIRD_FORMATE,
                    interval3 + 1);
        }
        return "";
    }

    /**
     * 获取三伏开始时间
     *
     * @param year
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static JCalendar[] getDogDaysBeginDates(int year) {
        int offsetTerm = StemsTable.getOffsetByTerm(year, StemsBranch.TERMS_XZ);
        if (offsetTerm < 0) {
            return null;
        }


        JCalendar[] begindates = new JCalendar[3];

        JCalendar termDate = new JCalendar(year, offsetTerm);

        int baseDay = StemsBranch.getStemsDay(termDate);

        begindates[0] = termDate.getNextDay(20 + (baseDay > 6 ? 16 - baseDay
                : 6 - baseDay));
        begindates[1] = begindates[0].getNextDay(10);
        offsetTerm = StemsTable.getOffsetByTerm(year, StemsBranch.TERMS_LQ);
        if (offsetTerm < 0) {
            return null;
        }
        termDate = new JCalendar(year, offsetTerm);
        baseDay = StemsBranch.getStemsDay(termDate);
        begindates[2] = termDate.getNextDay((baseDay > 6) ? (16 - baseDay)
                : (6 - baseDay));
        return begindates;
    }

    /**
     * 获取九九的开始时间
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public JCalendar getColdBeginDate() {
        int year = getYear();
        if (year >= MIN_YEAR && year <= MAX_YEAR) {
            int days = getDayofYear();
            int offset = 0;
            if (days < 100) {
                offset = StemsTable.getOffsetByTerm(year - 1, StemsBranch.STEMS_DZ);// 23表示九九开始
                return new JCalendar(getYear() - 1, offset);
            } else {
                offset = StemsTable.getOffsetByTerm(year, StemsBranch.STEMS_DZ);
                return new JCalendar(getYear(), offset);
            }
        }
        return null;
    }


    public int getUTCOffset() {
        return get(Calendar.DST_OFFSET) + get(Calendar.ZONE_OFFSET);
    }


    public String getAlarmBirthdaySolarDate() {
        return "(阳历)" + getYear() + "年" + getMonth() + "月" + getDay() + "日";
    }


    public String getAlarmBirthdayLunarDate() {
        return "(阴历)" + getLunarYearAsString() + getLunarMonthAsString() + getLunarDateAsString();
    }

    public String getAlarmBirthdaySolarDate1() {
        return "(阳历)" + getMonth() + "月" + getDay() + "日";
    }


    public String getAlarmBirthdayLunarDate1() {
        return "(阴历)" + getLunarMonthAsString() + getLunarDateAsString();
    }


    public JCalendar getNextLeepYear() {
        JCalendar calendar = null;
        for (int i = 1; i < 4; i++) {
            calendar = getInstance().getNextYear(i);
            if (calendar.isLeapYear(calendar.getYear())) {
                return calendar;
            } else {
                continue;
            }
        }
        return calendar;
    }


    /**
     * @param anotherCalendar
     * @return
     */
    public long compares(JCalendar anotherCalendar) {
        return getTimeInMillis() - anotherCalendar.getTimeInMillis();
    }


    /**
     * 得到时辰凶吉
     */
    public String getShiChenXiongJi() {
        String str[] = StemsBranch.get12Shen(getStemsBranchDay(), getStemsBranchHour()).split(",");

        return getStemsBranchHourAsString() + "时" + str[1];
    }


    /**
     * 是否是重丧日
     *
     * @return
     */
    public boolean isChongSangRi() {
        boolean flag = false;

        String month_dz = getStemsBranch().getStemsBranchMonthAsString().substring(1, 2);

        String dat_tg = getStemsBranch().getStemsBranchDayAsString().substring(0, 1);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("寅", "庚");
        map.put("卯", "辛");
        map.put("辰", "戊");
        map.put("巳", "丙");
        map.put("午", "丁");
        map.put("未", "己");
        map.put("申", "甲");
        map.put("酉", "乙");
        map.put("戌", "戊");
        map.put("亥", "壬");
        map.put("子", "癸");
        map.put("丑", "己");


        String value = map.get(month_dz);

        if (value.equals(dat_tg)) {
            flag = true;
        }

        return flag;
    }


}

