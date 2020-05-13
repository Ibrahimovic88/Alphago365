package com.alphago365.octopus.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {

    public static Instant asInstant(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    public static Instant asInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static String format(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(Instant instant, String pattern) {
        return instant.atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static Instant parseToInstant(String date, String pattern, Locale locale) {
        return asInstant(parseToDate(date, pattern, locale));
    }

    public static Instant parseToInstant(String date, String pattern) {
        return parseToInstant(date, pattern, Locale.getDefault());
    }

    public static LocalDate parseToDate(String date, String pattern, Locale locale) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern, locale));
    }

    public static LocalDateTime parseToDateTime(String dateTime, String pattern, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static LocalDateTime parseToDateTime(String dateTime, String pattern) {
        return parseToDateTime(dateTime, pattern, Locale.getDefault());
    }
}
