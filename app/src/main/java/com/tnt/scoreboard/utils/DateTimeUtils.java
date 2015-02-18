package com.tnt.scoreboard.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ocpsoft.prettytime.PrettyTime;

public final class DateTimeUtils {

    private static final DateTimeFormatter formatter = DateTimeFormat
            .forPattern(Constants.SQLITE_DATE_FORMAT).withZoneUTC();

    public static DateTime now() {
        return new DateTime();
    }

    /**
     * @param localDate DateTime with local timezone
     * @return String with utc timezone
     */
    public static String formatUtc(DateTime localDate) {
        return formatter.print(localDate);
    }

    /**
     * @param utcString String with utc timezone
     * @return DateTime with local timezone
     */
    public static DateTime parseLocalDate(String utcString) {
        return formatter.parseDateTime(utcString).withZone(DateTimeZone.getDefault());
    }

    public static String formatPretty(DateTime dateTime) {
        PrettyTime p = new PrettyTime();
        return p.format(dateTime.toDate());
    }
}
