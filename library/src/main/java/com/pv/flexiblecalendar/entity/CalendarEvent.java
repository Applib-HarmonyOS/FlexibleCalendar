package com.pv.flexiblecalendar.entity;

/**
 * CalenderEvent class to set and get the colors.
 *
 * {@author p-v}
 */
public class CalendarEvent implements Event {

    /**
     * day.
     */
    private int color;

    /**
     * arg constructor.
     *
     * @param color color
     */
    public CalendarEvent(final int color) {
        this.color = color;
    }

    /**
     * get color.
     *
     * @return color
     */
    @Override
    public int getColor() {
        return color;
    }

    /**
     * set color.
     *
     * @param color color
     */
    public void setColor(final int color) {
        this.color = color;
    }
}
