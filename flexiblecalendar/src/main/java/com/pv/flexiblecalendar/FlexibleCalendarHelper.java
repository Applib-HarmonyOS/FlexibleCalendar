package com.pv.flexiblecalendar;

import ohos.app.Context;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * FlexiCalendarHelper.
 *
 * @author p-v
 */
public class FlexibleCalendarHelper {

    /**
     * M_OFFSET.
     */
    private static final int M_OFFSET = 3;

    /**
     * default constructor.
     */
    private FlexibleCalendarHelper() {
    }

    /**
     * Set the next month for the details passed.
     *
     * @param year year
     *
     * @param month month
     *
     * @param nextDate next month empty array
     */
    public static void nextMonth(int year, int month, int[] nextDate) {
        if (month == 11) {
            year++;
            month = 0;
        } else {
            month++;
        }
        nextDate[0] = year;
        nextDate[1] = month;
    }

    /**
     * Set the previous month for the details passed.
     *
     * @param year year
     *
     * @param month month
     *
     * @param previousDate previous month empty array
     */
    public static void previousMonth(int year, int month, int[] previousDate) {
        if (month == 0) {
            year--;
            month = 11;
        } else {
            month--;
        }
        previousDate[0] = year;
        previousDate[1] = month;
    }

    /**
     * Get the array for week days for the current locale.
     *
     * @param context context
     *
     * @return Get the array for week days for the current locale
     */
    public static String[] getWeekDaysList(Context context) {
        DateFormatSymbols symbols = new DateFormatSymbols(getLocale(context));
        return Arrays.copyOfRange(symbols.getShortWeekdays(), 1, 8);
    }

    /**
     * Get the current locale.
     *
     * @param context context
     *
     * @return locale
     */
    public static Locale getLocale(Context context) {
        return context.getResourceManager().getConfiguration().getFirstLocale();
    }

    /**
     * get localized calendar.
     *
     * @param context context
     *
     * @return the localized calendar instance
     */
    public static Calendar getLocalizedCalendar(Context context) {
        return Calendar.getInstance(getLocale(context));
    }

    /**
     * Get the number of rows for the provided month.
     *
     * @param year  year
     *
     * @param month month
     *
     * @return number of rows
     */
    public static int getNumOfRowsForTheMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return getRowOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH)) + 1;
    }

    /**
     * get row.
     *
     * @param day day
     *
     * @return Which row day is in
     */
    public static int getRowOf(int day) {
        return (day + M_OFFSET - 1) / 7;
    }

    /**
     * Get number of month difference with the current month.
     *
     * @param year year
     *
     * @param month month
     *
     * @return int val
     */
    public static int getMonthDifference(int year, int month) {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        return  (currentYear - year) * 12 + currentMonth - month;
    }

    /**
     * Get number of month difference between two the start and end month/year.
     *
     * @param startYear start year
     *
     * @param startMonth start month
     *
     * @param endYear end year
     *
     * @param endMonth end month
     *
     * @return int val
     */
    public static int getMonthDifference(int startYear, int startMonth, int endYear, int endMonth) {
        return (endYear - startYear) * 12 + endMonth - startMonth;
    }

}
