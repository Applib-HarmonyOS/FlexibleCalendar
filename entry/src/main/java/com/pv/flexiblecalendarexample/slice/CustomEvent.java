package com.pv.flexiblecalendarexample.slice;

import com.pv.flexiblecalendar.entity.Event;

/**
 * CustomEvent.
 *
 * @author p-v
 */
public class CustomEvent implements Event {

    /**
     * color.
     */
    private int color;

    /**
     * argument constructor.
     *
     * @param color color
     */
    public CustomEvent(final int color) {
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
}
