package com.soshiant.springbootexample.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static Date getCurrentDateTime() {
        return new Date();
    }

    public static String getCurrentDateTime(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(new Date());
    }

    public static String getCurrentTimeStamp() {
        return (new Timestamp(System.currentTimeMillis())).toString();
    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    public static String getCurrentLocalDateTime(String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(LocalDateTime.now());
    }

    public static Date toDate(String dateInString, String dateTimeFormat) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
            return formatter.parse(dateInString);
        } catch (Exception e){
            return null;
        }
    }

    public static LocalDateTime toLocalDateTime(String localDateTime, String pattern) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(localDateTime,dtf);

        } catch (Exception e){
            return null;
        }
    }

    public static Date addMinutesToCurrentDateTime(int min) {
        return DateUtils.addMinutes(new Date(),min);
    }

    public static String addMinutesToCurrentDateTime(int min,String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(DateUtils.addMinutes(new Date(),min));
    }

    public static Date addMinutes(Date dateTime, int minutes) {
        return DateUtils.addMinutes(dateTime,minutes);
    }

    public static String addMinutes(Date dateTime,int minutes, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.format(DateUtils.addMinutes(dateTime,minutes));
        } catch (Exception e){
            return null;
        }

    }

    public static String format(Date dateTime, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.format(dateTime);
        } catch (Exception e){
            return null;
        }
    }

}
