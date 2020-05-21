package com.alphago365.octopus.persistence;

import androidx.room.TypeConverter;

import com.alphago365.octopus.mvp.model.Match;
import com.alphago365.octopus.mvp.model.WDL;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Stream;

public class Converters {

    public static String PATTERN_DATE_DEFAULT = "yyyy-MM-dd";

    @TypeConverter
    public static Instant instantFromDateString(String strDate) {
        return LocalDate.parse(strDate, DateTimeFormatter.ofPattern(PATTERN_DATE_DEFAULT, Locale.getDefault()))
                .atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    @TypeConverter
    public static String instantToDateString(Instant instant) {
        return instant
                .atZone(ZoneId.systemDefault())
                .toLocalDate().format(DateTimeFormatter.ofPattern(PATTERN_DATE_DEFAULT));
    }

    public static String formatToPrettyDate(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        LocalDate localDate = zonedDateTime.toLocalDate();
        return localDate.toString();
    }

    public static String formatToPrettyDateTime(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        LocalDate localDate = zonedDateTime.toLocalDate();
        LocalTime localTime = zonedDateTime.toLocalTime();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        return String.format(Locale.getDefault(), "%d-%d %2s:%2s",
                localDate.getMonthValue(), localDate.getDayOfMonth(),
                (hour < 10 ? String.format(Locale.getDefault(), "0%d", hour) : String.format(Locale.getDefault(), "%d", hour)),
                (minute < 10 ? String.format(Locale.getDefault(), "0%d", minute) : String.format(Locale.getDefault(), "%d", minute)));
    }

    public static String formatToPrettyScore(Match match) {
        Integer finalHomeGoals = match.getFinalHomeGoals();
        Integer halfHomeGoals = match.getHalfHomeGoals();
        Integer halfAwayGoals = match.getHalfAwayGoals();
        Integer finalAwayGoals = match.getFinalAwayGoals();
        boolean flag = Stream.of(finalHomeGoals, halfHomeGoals, halfAwayGoals, finalAwayGoals).anyMatch(goal -> goal < 0);
        if (flag) {
            return "VS";
        }
        return String.format(Locale.getDefault(), "%d(%d) : (%d)%d", finalHomeGoals, halfHomeGoals, halfAwayGoals, finalAwayGoals);
    }

    @TypeConverter
    public static WDL wdlFromInteger(Integer score) {
        switch (score) {
            case 3:
                return WDL.WIN;
            case 1:
                return WDL.DRAW;
            case 0:
                return WDL.LOSE;
            default:
                return WDL.UNKNOWN;
        }
    }

    @TypeConverter
    public static Integer wdlToInteger(WDL wdl) {
        return wdl.getScore();
    }
}