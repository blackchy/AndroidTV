package com.example.core.date;

import java.util.Calendar;

/**
 * 展示农历信息
 */
public class JLunar {


    public static final int MIN_LUNAR_YEAR = 1899;//农历表中最小的年份
    // 中文数字
    public static final String[] CHINESE_NUM = {"零", "一", "二", "三", "四", "五",
            "六", "七", "八", "九", "十", "十一", "十二"};
    private static final int NOT_FOUND_LUNAR = -1;
    // 农历月
    public static final String[] LUNAR_MONTH_NAMES = {"正月", "二月", "三月", "四月",
            "五月", "六月", "七月", "八月", "九月", "十月", "冬月", "腊月"};

    // 农历名称
    public static final String[] LUNAR_DATE_NAMES = {"初一", "初二", "初三", "初四",
            "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五",
            "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六",
            "廿七", "廿八", "廿九", "三十"};


    // 农历名称提醒专用
    public static final String[] ALARM_LUNAR_DATE_NAMES = {"初一", "初二", "初三", "初四",
            "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五",
            "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六",
            "廿七", "廿八", "廿九", "三十(大)"};


    /*
     1899~2135年 农历信息
     eg:2012年 -> 0x1754416 -> (0001 0111 0101 0100 0100 0001 0110)2

     从后往前读8位 表示 正月初一 距离 公历1月1日 的天数: (0001 0110)2 -> 22 天
     继续往前读4位 表示 闰哪个月 (0100)2 -> 4 即 闰四月 （0表示该年没有闰月）
     继续往前读13位 表示 每月天数信息 其中前12位表示正月到腊月的天数信息 第13位表示闰月的天数信息 (1 0111 0101 0100)2 -> 正月大、二月小、三月大 。。。腊月小、闰四月小

     注:农历月大30天 月小29天
     */
    //农历信息表
    public static final int LunarTable[] = {
            0x156A028, 0x97A81E, 0x95C031, 0x14AE026, 0xA9A51C, 0x1A4C02E, 0x1B2A022, 0xCAB418, 0xAD402B, 0x135A020,     //1899-1908
            0xABA215, 0x95C028, 0x14B661D, 0x149A030, 0x1A4A024, 0x1A4B519, 0x16A802C, 0x1AD4021, 0x15B4216, 0x12B6029,  //1909-1918
            0x92F71F, 0x92E032, 0x1496026, 0x169651B, 0xD4A02E, 0xDA8023, 0x156B417, 0x56C02B, 0x12AE020, 0xA5E216,      //1919-1928
            0x92E028, 0xCAC61D, 0x1A9402F, 0x1D4A024, 0xD53519, 0xB5A02C, 0x56C022, 0x10DD317, 0x125C029, 0x191B71E,     //1929-1938
            0x192A031, 0x1A94026, 0x1B1561A, 0x16AA02D, 0xAD4023, 0x14B7418, 0x4BA02B, 0x125A020, 0x1A56215, 0x152A028,  //1939-1948
            0x16AA71C, 0xD9402F, 0x16AA024, 0xA6B51A, 0x9B402C, 0x14B6021, 0x8AF317, 0xA5602A, 0x153481E, 0x1D2A030,     //1949-1958
            0xD54026, 0x15D461B, 0x156A02D, 0x96C023, 0x155C418, 0x14AE02B, 0xA4C020, 0x1E4C314, 0x1B2A027, 0xB6A71D,    //1959-1968
            0xAD402F, 0x12DA024, 0x9BA51A, 0x95A02D, 0x149A021, 0x1A9A416, 0x1A4A029, 0x1AAA81E, 0x16A8030, 0x16D4025,   //1969-1978
            0x12B561B, 0x12B602E, 0x936023, 0x152E418, 0x149602B, 0x164EA20, 0xD4A032, 0xDA8027, 0x15E861C, 0x156C02F,   //1979-1988
            0x12AE024, 0x95E51A, 0x92E02D, 0xC96022, 0xE94316, 0x1D4A028, 0xD6A81E, 0xB58031, 0x156C025, 0x12DA51B,      //1989-1998
            0x125C02E, 0x192C023, 0x1B2A417, 0x1A9402A, 0x1B4A01F, 0xEAA215, 0xAD4027, 0x157671C, 0x4BA030, 0x125A025,   //1999-2008
            0x1956519, 0x152A02C, 0x1694021, 0x1754416, 0x15AA028, 0xABA91E, 0x974031, 0x14B6026, 0xA2F61B, 0xA5602E,    //2009-2018
            0x1526023, 0xF2A418, 0xD5402A, 0x15AA01F, 0xB6A215, 0x96C028, 0x14DC61C, 0x149C02F, 0x1A4C024, 0x1D4C519,    //2019-2028
            0x1AA602B, 0xB54021, 0xED4316, 0x12DA029, 0x95EB1E, 0x95A031, 0x149A026, 0x1A1761B, 0x1A4A02D, 0x1AA4022,    //2029-2038
            0x1BA8517, 0x16B402A, 0xADA01F, 0xAB6215, 0x936028, 0x14AE71D, 0x149602F, 0x154A024, 0x164B519, 0xDA402C,    //2039-2048
            0x15B4020, 0x96D316, 0x126E029, 0x93E81F, 0x92E031, 0xC96026, 0xD1561B, 0x1D4A02D, 0xD64022, 0x14D9417,      //2049-2058
            0x155C02A, 0x125C020, 0x1A5C314, 0x192C027, 0x1AAA71C, 0x1A9402F, 0x1B4A023, 0xBAA519, 0xAD402C, 0x14DA021,  //2059-2068
            0xABA416, 0xA5A029, 0x153681E, 0x152A031, 0x1694025, 0x16D461A, 0x15AA02D, 0xAB4023, 0x1574417, 0x14B602A,   //2069-2078
            0xA56020, 0x164E315, 0xD26027, 0xE6671C, 0xD5402F, 0x15AA024, 0x96B519, 0x96C02C, 0x14AE021, 0xA9C417,       //2079-2088
            0x1A4C028, 0x1D2C81D, 0x1AA4030, 0x1B54025, 0xD5561A, 0xADA02D, 0x95C023, 0x153A418, 0x149A02A, 0x1A2A01F,   //2089-2098
            0x1E4A214, 0x1AA4027, 0x1B6471C, 0x16B402F, 0xABA025, 0x9B651B, 0x93602D, 0x1496022, 0x1A96417, 0x154A02A,   //2099-2108
            0x16AA91E, 0xDA4031, 0x15AC026, 0xAEC61C, 0x126E02E, 0x92E024, 0xD2E419, 0xA9602C, 0xD4A020, 0xF4A315,       //2109-2118
            0xD54028, 0x155571D, 0x155A02F, 0xA5C025, 0x195C51A, 0x152C02D, 0x1A94021, 0x1C95416, 0x1B2A029, 0xB5A91F,   //2119-2128
            0xAD4031, 0x14DA026, 0xA3B61C, 0xA5A02F, 0x151A023, 0x1A2B518, 0x165402B};                                //2129-2135


    private int lunarYear;
    private int lunarDate;
    private int lunarMonth;
    private boolean isLeap;
    private int hour;
    private int minutes;
    private int seconds;

    /**
     * 农历
     *
     * @param lunarYear
     * @param lunarMonth
     * @param lunarDate
     * @param isLeap
     * @param hour
     * @param minutes
     * @param seconds
     */
    public JLunar(int lunarYear, int lunarMonth, int lunarDate, boolean isLeap, int hour, int minutes, int seconds) {
        this.lunarYear = lunarYear;
        this.lunarDate = lunarDate;
        this.lunarMonth = lunarMonth;
        this.isLeap = isLeap;
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }


    /**
     * 更新农历到公历
     *
     * @param lunar
     * @param calendar
     */
    public static void updateSolarFromLunar(JLunar lunar, JCalendar calendar) {
        if (calendar == null) {
            return;
        }
        JCalendar cal = getSolarFromLunar(lunar.lunarYear, lunar.lunarMonth, lunar.isLeap, lunar.lunarDate, calendar.getHour(), calendar.getMinutes(), calendar.getSeconds());
        calendar.setTimeInMillis(cal.getTimeInMillis());
    }


    /**
     * 从农历转换到公历
     *
     * @param lunarYear
     * @param lunarMonth
     * @param lunarDate
     * @param hour
     * @param minutes
     * @param seconds
     * @return
     */
    public static JCalendar getSolarFromLunar(int lunarYear, int lunarMonth, boolean isleapmonth, int lunarDate, int hour, int minutes, int seconds) {
        int index = lunarYear - MIN_LUNAR_YEAR;
        if (index > LunarTable.length || index < 0) {
            return null;
        }
        int hexValue = LunarTable[index];
        int leapmonth = (hexValue >> 8) & 0xF;
        int loffset = hexValue & 0xFF;
        int end = lunarMonth + ((isleapmonth || (lunarMonth > leapmonth && leapmonth > 0)) ? 1 : 0) - 1;
        int cdays = 0;
        int v = 0;
        for (int i = 0; i < end; i++) {
            if (i >= leapmonth && leapmonth > 0) {
                if (i == leapmonth) {
                    v = (hexValue >> 12) & 0x1;
                } else {
                    v = (hexValue >> (24 - i + 1)) & 0x1;
                }
            } else {
                v = (hexValue >> (24 - i)) & 0x1;
            }
            cdays += (29 + v);
        }
        cdays += lunarDate + loffset;
        int solarYear = lunarYear;
        int thisYearDays = JCalendar.getMaxDayAtYear(solarYear);
        if (cdays > thisYearDays) {
            cdays -= thisYearDays;
            solarYear++;
        }
        JCalendar calendar = new JCalendar(solarYear, cdays, hour, minutes, seconds);
        return calendar;
    }

    /**
     * 计算农历信息
     *
     * @param calendar
     * @param lunarInfo
     * @return
     */
    public static JLunar getLunarFromSolar(JCalendar calendar, JLunar lunarInfo) {
        if (!hasLunarInfo(calendar))
            return null;
        int ly, lm = 0, ld = 0, llm, h;
        boolean isLunarLeap = false;
        int sy = ly = calendar.getYear();
        int doffset = calendar.getDayofYear();
        int hexvalue = LunarTable[ly - MIN_LUNAR_YEAR];
        int loffset = hexvalue & 0xFF;//农历正月的偏移
        if (loffset > doffset) {//如果当前离1月1号的天数比正月离元月的天数还小那么则应该是上一个农历年
            ly--;
            doffset += JCalendar.getMaxDayAtYear(ly);
            hexvalue = LunarTable[ly - MIN_LUNAR_YEAR];
            loffset = hexvalue & 0xFF;
        }

        int days = doffset - loffset + 1;
        llm = (hexvalue >> 8) & 0xF;//农历闰月
        int len = llm > 0 ? 13 : 12;
        //开始循环取
        int v = 0;
        int cd = 0;
        for (int i = 0; i < len; i++) {
            if (i >= llm && llm > 0) {
                if (i == llm) {
                    v = (hexvalue >> 12) & 0x1;
                } else {
                    v = (hexvalue >> (24 - i + 1)) & 0x1;
                }
            } else {

                v = (hexvalue >> (24 - i)) & 0x1;
            }
            cd = 29 + v;
            days -= cd;
            if (days <= 0) {
                ld = days + cd;
                lm = i + 1;
                if (llm > 0 && i >= llm) {
                    if (i == llm) {
                        isLunarLeap = true;
                    } else {
                        isLunarLeap = false;
                    }
                    --lm;
                }
                break;
            }
        }

        int lh = getLunarHour(calendar.getHour());

        if (lunarInfo != null) {
            lunarInfo.lunarYear = ly;
            lunarInfo.lunarMonth = lm;
            lunarInfo.lunarDate = ld;
            lunarInfo.isLeap = isLunarLeap;
            lunarInfo.hour = lh;
        } else {

            return new JLunar(ly, lm, ld, isLunarLeap, lh, calendar.getMinutes(), calendar.getSeconds());
        }
        return lunarInfo;
    }


    /*
     农历计时
         */
    public static int getLunarHour(int hour) {
        return (hour + 1) / 2 % 12;
    }


    /**
     * 是否有农历信息
     *
     * @param calendar
     * @return
     */
    public synchronized static boolean hasLunarInfo(JCalendar calendar) {
        int syear = calendar.getYear();
        int dayoffset = calendar.get(Calendar.DAY_OF_YEAR);

        int lindex = syear - MIN_LUNAR_YEAR;
        if (lindex < 0 || lindex > LunarTable.length) {
            return false;
        }
        int lyear = syear;
        int hexValue = LunarTable[lindex];
        int ldayoffset = hexValue & 0xFF;
        if (ldayoffset > dayoffset) {
            lyear--;
        }

        if (lyear < MIN_LUNAR_YEAR)
            return false;
        return true;
    }

    /**
     * 根据农历月获取闰月
     *
     * @param lunarYear
     * @return
     */
    public static int getLunarLeapMonth(int lunarYear) {
        int index = lunarYear - MIN_LUNAR_YEAR;
        if (index < 0 || index > LunarTable.length) {
            return NOT_FOUND_LUNAR;
        }

        int hexValue = LunarTable[index];
        return (hexValue >> 8) & 0xF;
    }

    /**
     * 获取农历年月中最多的天数
     *
     * @param lunarYear
     * @param lunarMonth
     * @param isLeap
     * @return
     */
    public static int getLunarMaxDayInMonth(int lunarYear, int lunarMonth, boolean isLeap) {
        int index = lunarYear - MIN_LUNAR_YEAR;
        if (index < 0 || index > LunarTable.length) {
            return NOT_FOUND_LUNAR;
        }
        int hexValue = LunarTable[index];
        isLeap = (getLunarLeapMonth(lunarYear) == lunarMonth) & isLeap;
        if (isLeap) {
            return ((hexValue >> 12) & 0x1) + 29;
        }

        return ((hexValue >> (24 - lunarMonth + 1)) & 0x1) + 29;
    }


    /**
     * 获取农历年中一共有多少个月
     *
     * @param lunarYear
     * @return
     */
    public static int getMonthsInLunar(int lunarYear) {
        return getLunarLeapMonth(lunarYear) > 0 ? 13 : 12;
    }


    /**
     * 农历年
     *
     * @return
     */
    public int getLunarYear() {
        return lunarYear;
    }

    /**
     * 农历日
     *
     * @return
     */
    public int getLunarDate() {
        return lunarDate;
    }

    /**
     * 农历月
     *
     * @return
     */
    public int getLunarMonth() {
        return lunarMonth;
    }

    /**
     * 是否闰
     *
     * @return
     */
    public boolean isLeap() {
        return isLeap;
    }

    public void setLunarYear(int lunarYear) {
        this.lunarYear = lunarYear;
        if (lunarDate > getLunarMaxDayInMonth(lunarYear, lunarMonth, isLeap)) {
            this.lunarDate = getLunarMaxDayInMonth(lunarYear, lunarMonth, isLeap);
        }
    }

    public void setLunarDate(int lunarDate) {
        this.lunarDate = lunarDate;
    }

    public void setLunarMonth(int lunarMonth) {
        this.lunarMonth = lunarMonth;
    }


    public void setLunarMonth(int lunarMonth, boolean isleap) {
        this.lunarMonth = lunarMonth;
        this.isLeap = isleap;

        if (lunarDate > getLunarMaxDayInMonth(lunarYear, lunarMonth, isleap)) {
            this.lunarDate = getLunarMaxDayInMonth(lunarYear, lunarMonth, isleap);
        }

    }

    public void setLeap(boolean isLeap) {
        this.isLeap = isLeap;
    }

    /**
     * 获取农历年
     *
     * @return
     */
    public String getLunarYearAsString() {
        return String.valueOf(lunarYear);
    }

    /**
     * 获取农历月
     *
     * @return
     */
    public String getLunarMonthAsString() {
        String str = LUNAR_MONTH_NAMES[lunarMonth - 1];
        if (isLeap) {
            str = "闰" + str;
        }
        return str;
    }

    /**
     * 获取农历日月视图
     *
     * @return
     */
    public String getLunarDateAsString1() {

        String str = LUNAR_DATE_NAMES[lunarDate - 1];
        if (str.equals("初一")) {
            str = getLunarMonthAsString();
        }
        return str;
    }


    /**
     * 获取农历日 非月视图
     *
     * @return
     */
    public String getLunarDateAsString() {

        return LUNAR_DATE_NAMES[lunarDate - 1];
    }


    /**
     * toLunarString
     *
     * @return
     */
    public String toLunarString() {
        StringBuilder sb = new StringBuilder(getLunarYearAsString()).append("年").append(getLunarMonthAsString()).append(getLunarDateAsString());
        return sb.toString();
    }
    public static boolean isValid(int tly) {
        return tly >= 1900 && tly <= 2100;
    }

}

