package com.sdex.webteb.utils;

/*
 * $Id$
 *
 * Copyright (C) 2006 Josh Guilfoyle <jasta@devtcg.org>
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * Code lifted from Informa <http://informa.sourceforge.net>.
 */

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for formatting and parsing the various date formats we expect
 * to encounter.
 */
public class DateUtil {
    private static final String TAG = "DateUtils";

    private static final SimpleDateFormat[] dateFormats;
    private static final int dateFormat_default = 9;

    private DateUtil() {
    }

    static {
        final String[] possibleDateFormats =
                {
                        "EEE, dd MMM yyyy HH:mm:ss z", // RFC_822
                        "EEE, dd MMM yyyy HH:mm zzzz",
                        "yyyy-MM-dd'T'HH:mm:ssZ",
                        "yyyy-MM-dd'T'HH:mm:ss.SSSzzzz", // Blogger Atom feed has millisecs also
                        "yyyy-MM-dd'T'HH:mm:sszzzz",
                        "yyyy-MM-dd'T'HH:mm:ss z",
                        "yyyy-MM-dd'T'HH:mm:ssz", // ISO_8601
                        "yyyy-MM-dd'T'HH:mm:ss",
                        "yyyy-MM-dd'T'HHmmss.SSSz",
                        "yyyy-MM-dd",
                        "MMM dd, yyyy"
                };

        dateFormats = new SimpleDateFormat[possibleDateFormats.length];
//        TimeZone gmtTZ = TimeZone.getTimeZone("GMT");

        for (int i = 0; i < possibleDateFormats.length; i++) {
        /* TODO: Support other locales? */
            dateFormats[i] = new SimpleDateFormat(possibleDateFormats[i], Locale.US);
        }
    }

    /**
     * Parse a date string.  The format of RSS/Atom dates come in many
     * different forms, so this method is extremely flexible and attempts to
     * understand many different formats.
     * <p/>
     * Copied verbatim from Informa 0.7.0-alpha2, ParserUtils.java.
     *
     * @param strdate Date string to attempt to parse.
     * @return If successful, returns a {@link Date} object representing the parsed
     * date; otherwise, null.
     */
    public static Date parseDate(String strdate) {
        Date result = null;
        strdate = strdate.trim();
        if (strdate.endsWith("Z")) {
            strdate = strdate.substring(0, strdate.length() - 1) + "GMT";
        }

        if (strdate.length() > 10) {

            // TODO deal with +4:00 (no zero before hour)
            if ((strdate.substring(strdate.length() - 5).indexOf("+") == 0 || strdate
                    .substring(strdate.length() - 5).indexOf("-") == 0)
                    && strdate.substring(strdate.length() - 5).indexOf(":") == 2) {

                String sign = strdate.substring(strdate.length() - 5,
                        strdate.length() - 4);

                strdate = strdate.substring(0, strdate.length() - 5) + sign + "0"
                        + strdate.substring(strdate.length() - 4);
                // logger.debug("CASE1 : new date " + strdate + " ? "
                //    + strdate.substring(0, strdate.length() - 5));

            }

            String dateEnd = strdate.substring(strdate.length() - 6);

            // try to deal with -05:00 or +02:00 at end of date
            // replace with -0500 or +0200
            if ((dateEnd.indexOf("-") == 0 || dateEnd.indexOf("+") == 0)
                    && dateEnd.indexOf(":") == 3) {
                // TODO deal with GMT-00:03
                if ("GMT".equals(strdate.substring(strdate.length() - 9, strdate
                        .length() - 6))) {
                    Log.d(TAG, "General time zone with offset, no change");
                } else {
                    // continue treatment
                    String oldDate = strdate;
                    String newEnd = dateEnd.substring(0, 3) + dateEnd.substring(4);
                    strdate = oldDate.substring(0, oldDate.length() - 6) + newEnd;
                    // logger.debug("!!modifying string ->"+strdate);
                }
            }
        }
        int i = 0;
        int dateFormatsLength = dateFormats.length;
        while (i < dateFormatsLength) {
            try {
                result = dateFormats[i].parse(strdate);
                // logger.debug("******Parsing Success "+strdate+"->"+result+" with
                // "+dateFormats[i].toPattern());
                break;
            } catch (java.text.ParseException eA) {
                i++;
            }
        }

        return result;
    }

    /**
     * Format a date in a manner that would be most suitable for serialized
     * storage.
     *
     * @param date {@link Date} object to format.
     * @return Robust, formatted date string.
     */
    public static String formatDate(Date date) {
        return dateFormats[dateFormat_default].format(date);
    }

    public static String formatDate(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat, Locale.US).format(date);
    }

    public static boolean compareDatesWithToday(Date from, Date to, boolean isBorn) {

        if (!isBorn) {
            long diff = to.getTime() - from.getTime();
            long oneDay = 24 * 60 * 60 * 1000;
            int elapsedDays = (int) (diff / oneDay);
            if(elapsedDays > 280) {
                return true;
            }
        }

        Calendar c = Calendar.getInstance();

        c.setTime(from);
        int yearFrom = c.get(Calendar.YEAR);
        int monthFrom = c.get(Calendar.MONTH);
        int dayFrom = c.get(Calendar.DAY_OF_MONTH);

        c.setTime(to);
        int yearTo = c.get(Calendar.YEAR);
        int monthTo = c.get(Calendar.MONTH);
        int dayTo = c.get(Calendar.DAY_OF_MONTH);

        if (yearFrom > yearTo) {
            return true;
        } else if (yearFrom < yearTo) {
            return false;
        }

        if (monthFrom > monthTo) {
            return true;
        } else if (monthFrom < monthTo) {
            return false;
        }

        if (dayFrom > dayTo) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean compareDatesForLastPeriod(Date from, Date to) {
        long diff = to.getTime() - from.getTime();
        if (diff < 0) {
            return true;
        } else {
            long oneDay = 24 * 60 * 60 * 1000;
            int elapsedDays = (int) (diff / oneDay);
//            last period should be more than 7 and less than 280 days before today
            return elapsedDays < 8 || elapsedDays > 280;
        }
    }

    public static Date getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Calendar getCurrentCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Calendar getCalendarFromDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
