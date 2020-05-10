package com.alphago365.octopus.util;

import javax.swing.text.DateFormatter;
import javax.validation.constraints.NotNull;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate parse(String date, String pattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime asLocalDateTime(long milliseconds) {
        return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return asLocalDateTime(date.getTime());
    }

    public static LocalDateTime parse(String str, String pattern, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
        return LocalDateTime.parse(str, formatter);
    }

    public static String getPrettyDurationTillNow(@NotNull LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        boolean before = dateTime.isBefore(now);
        Duration duration = Duration.between(dateTime, now);
        long days = duration.toDays();
        if (days > 0) {
            return String.format(before ? "[-%dD]" : "[+%dD]", days);
        }
        long hours = duration.toHours();
        if (hours > 0) {
            return String.format(before ? "[-%dH]" : "[%dH]", hours);
        }
        long minutes = duration.toHours();
        return String.format(before ? "[-%dMin]" : "[+%dMin]", minutes);
    }

    public static String getPrettyDurationTillNow(long milliseconds) {
        LocalDateTime dateTime = asLocalDateTime(milliseconds);
        return getPrettyDurationTillNow(dateTime);
    }
}
