package com.vulner.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    /**
     * 从字符串中提取时间，并转换成 java.sql.Timestamp 类型返回
     *
     * @param timeString 含有时间的字符串
     * @param timeFormat 字符串内时间格式，比如: "yyyy/MM/dd"
     * @return java.sql.Timestamp 类型的时间
     */
    static public Timestamp parseTimeFromString(String timeString, String timeFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        Timestamp timestamp = new Timestamp(0);
        try {
            Date date = simpleDateFormat.parse(timeString);
            timestamp = new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    static public Timestamp parseExpireTimeFromString(String timeString, char splitChar) {
        String timeFormat = "yyyy/MM/dd";
        // 如果原始时间字符串中，分隔符不是'/'，则更换分隔符为'/'
        if (splitChar != '/') {
            timeFormat = timeFormat.replace(splitChar, '/');
        }

        // 从输入字符串中提取SQL的Timestamp时间
        Timestamp timestamp = parseTimeFromString(timeString, timeFormat);
        // 此时获得的时间不是当天最后一秒。对过期时间来说，需要把时间调整到当天最后一秒
        Long milliSeconds = timestamp.getTime();
        milliSeconds += (24 * 60 * 60 - 1) * 1000;
        timestamp.setTime(milliSeconds);

        return timestamp;
    }

    /**
     * 根据起始时间和有效天数，获取失效时间
     * @param start 起始时间
     * @param days 有效天数
     * @return Timestamp 格式的 失效时间
     */
    static public Timestamp calculateExpireTimeStamp(Timestamp start, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_MONTH, days);

        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * 获取当前时间 java.util.Date 类型
     *
     * @return java.util.Date currentDate
     */
    static public Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取当前系统时间，按SQL的时间戳格式
     *
     * @return java.sql.Timestamp currentTime
     */
    static public Timestamp getCurrentSystemTimestamp() {
        Date currentDate = getCurrentDate();

        return new Timestamp(currentDate.getTime());
    }
}
