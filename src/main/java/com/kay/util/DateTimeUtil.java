package com.kay.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

    private static final String STANDARD_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(STANDARD_FORMAT_STR);

    private DateTimeUtil() {}

    public static String defaultDateToStr(Date date) {
        if (date == null) {
            return "";
        }
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    public static Date defaultStrToDate(String dateStr) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DATE_TIME_FORMATTER);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String dateToStr(Date date) {
        return defaultDateToStr(date);
    }

    public static Date strToDate(String dateStr) {
        return defaultStrToDate(dateStr);
    }
}
