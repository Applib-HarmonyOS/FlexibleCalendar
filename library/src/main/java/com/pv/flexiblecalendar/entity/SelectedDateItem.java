package com.pv.flexiblecalendar.entity;

/**
 * To select the Years,Months and Days.
 *
 * @author p-v
 */
public class SelectedDateItem {

    /**
     * day.
     */
    private int day;
    /**
     * month.
     */
    private int month;
    /**
     * year.
     */
    private int year;

    /**
     * To initialize Year,Month and Day.
     *
     * @param year year
     *
     * @param  month month
     *
     * @param day day
     */
    public SelectedDateItem(final int year, final int month, final int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * clone.
     *
     * @param selectedDateItem date
     */
    public SelectedDateItem(final SelectedDateItem selectedDateItem) {
        this.year = selectedDateItem.year;
        this.month = selectedDateItem.month;
        this.day = selectedDateItem.day;
    }

    /**
     * copy factory.
     *
     * @param slecSelectedDateItem selected date item
     *
     * @return selected date
     */
    public static SelectedDateItem newInstance(final SelectedDateItem slecSelectedDateItem) {
        return new SelectedDateItem(slecSelectedDateItem);
    }

    /**
     * get day.
     *
     * @return get day
     */
    public int getDay() {
        return day;
    }

    /**
     * set day.
     *
     * @param day day
     */
    public void setDay(final int day) {
        this.day = day;
    }

    /**
     * get month.
     *
     * @return get month
     */
    public int getMonth() {
        return month;
    }

    /**
     * set month.
     *
     * @param month month
     */
    public void setMonth(final int month) {
        this.month = month;
    }

    /**
     * get year.
     *
     * @return get year
     */
    public int getYear() {
        return year;
    }

    /**
     * set year.
     *
     * @param year year
     */
    public void setYear(final int year) {
        this.year = year;
    }

    /**
     * equals.
     *
     * @return bool val
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SelectedDateItem that = (SelectedDateItem) o;

        if (day != that.day) {
            return false;
        }
        if (month != that.month) {
            return false;
        }
        return year == that.year;

    }

    /**
     * hashcode.
     *
     * @return int val
     */
    @Override
    public int hashCode() {
        int result = day;
        result = 31 * result + month;
        result = 31 * result + year;
        return result;
    }
}
