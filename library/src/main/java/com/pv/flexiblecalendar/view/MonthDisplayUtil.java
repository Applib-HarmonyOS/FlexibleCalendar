package com.pv.flexiblecalendar.view;

import java.util.Calendar;

/**
 * Month display util.
 */
public class MonthDisplayUtil {

    /**
     * week start day.
     */
    private final int weekStartDay;
    /**
     * calendar.
     */
    private Calendar calendar;
    /**
     * num days in month.
     */
    private int numDaysInMonth;
    /**
     * num days in prev month.
     */
    private int numDaysInPrevMonth;
    /**
     * offset.
     */
    private int offset;

    /**
     * arg constructor.
     *
     * @param year  The year.
     * @param month The month.
     */
    public MonthDisplayUtil(final int year, final int month) {
        this(year, month, Calendar.SUNDAY);
    }

    /**
     * arg constructor.
     *
     * @param year         The year.
     * @param month        The month.
     * @param weekStartDay What day of the week the week should start.
     */
    public MonthDisplayUtil(final int year, final int month, final int weekStartDay) {

        if (weekStartDay < Calendar.SUNDAY || weekStartDay > Calendar.SATURDAY) {
            throw new IllegalArgumentException();
        }
        this.weekStartDay = weekStartDay;

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.getTimeInMillis();
        recalculate();
    }

    /**
     * get year.
     *
     * @return year
     */
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * get month.
     *
     * @return month
     */
    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * get week start day.
     *
     * @return week start day
     */
    public int getWeekStartDay() {
        return weekStartDay;
    }

    /**
     * get first day of the month.
     *
     * @return The first day of the month using a constants such as {@link Calendar#SUNDAY}.
     */
    public int getFirstDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * get number days in the month.
     *
     * @return The number of days in the month.
     */
    public int getNumberOfDaysInMonth() {
        return numDaysInMonth;
    }

    /**
     * get offset.
     *
     * @return The offset from displaying everything starting on the very first box.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * get digits.
     *
     * @param row Which row (0-5).
     * @return the digits of the month to display in one of the 6 rows of a calendar month display.
     */
    public int[] getDigitsForRow(final int row) {
        if (row < 0 || row > 5) {
            throw new IllegalArgumentException("row " + row
                    + " out of range (0-5)");
        }

        int[] result = new int[7];
        for (int column = 0; column < 7; column++) {
            result[column] = getDayAt(row, column);
        }

        return result;
    }

    /**
     * getDayAt at specified row & col.
     *
     * @param row    The row, 0-5, starting from the top.
     * @param column The column, 0-6, starting from the left.
     * @return The day at a particular row, column
     */
    public int getDayAt(final int row, final int column) {
        if (row == 0 && column < offset) {
            return numDaysInPrevMonth + column - offset + 1;
        }
        int day = 7 * row + column - offset + 1;
        return (day > numDaysInMonth)
                ? day - numDaysInMonth : day;
    }

    /**
     * is within current month.
     *
     * @param row    row
     * @param column col
     * @return Whether the row and column fall within the month.
     */
    public boolean isWithinCurrentMonth(final int row, final int column) {

        if (row < 0 || column < 0 || row > 5 || column > 6) {
            return false;
        }

        if (row == 0 && column < offset) {
            return false;
        }

        int day = 7 * row + column - offset + 1;
        if (day > numDaysInMonth) {
            return false;
        }
        return true;
    }

    /**
     * re calculate.
     */
    private void recalculate() {
        numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, -1);
        numDaysInPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, 1);
        int firstDayOfMonth = getFirstDayOfMonth();
        int localOffset = firstDayOfMonth - weekStartDay;
        if (localOffset < 0) {
            localOffset += 7;
        }
        this.offset = localOffset;
    }
}
