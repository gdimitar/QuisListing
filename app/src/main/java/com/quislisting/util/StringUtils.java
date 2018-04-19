package com.quislisting.util;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    private static final String STRING_SEPARATOR = "T";
    public static final String EMPTY_STRING = "";
    public static final String SEPARATOR = " ";
    public static final String THREE_DOTS_SEPARATOR = "...";
    public static final String UNKNOWN_VALUE = "Unknown";

    private StringUtils() {

    }

    public static boolean isEmpty(final String str) {
        return str == null || EMPTY_STRING.equals(str);
    }

    public static boolean isNotEmpty(final String str) {
        return !isEmpty(str);
    }

    public static String convertZonedDateTime(final Object dateField, final boolean isStringField) {
        if (isStringField) {
            final String dateString = (String) dateField;
            final int tIndex = dateString.indexOf(STRING_SEPARATOR);

            if (tIndex != -1) {
                return dateString.substring(0, tIndex);
            }

            return EMPTY_STRING;
        } else {
            return convertLongTimestampToString((Long) dateField);
        }
    }

    public static String convertLongTimestampToString(final Long timestamp) {
        final Date date = new Date(timestamp);

        final Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String convertTimestampToString(final Timestamp timestamp) {
        final Date date = new Date(timestamp.getTime());

        final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }
}
