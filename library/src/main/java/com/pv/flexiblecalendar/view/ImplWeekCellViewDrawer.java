package com.pv.flexiblecalendar.view;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;

/**
 * IWeekCellViewDrawer.
 *
 * @author p-v
 */
public interface ImplWeekCellViewDrawer extends ImplCellViewDrawer {
    /**
     * Week Cell view.
     *
     * @param position position
     * @param convertView convertView
     * @param parent parent
     * @return getcellview
     */
    BaseCellView getCellView(int position, Component convertView, ComponentContainer parent);

    /**
     * Display value for the day of week.
     *
     * @param dayOfWeek dayofweek
     * @param defaultValue defaultval
     * @return getweekday
     */
    String getWeekDayName(int dayOfWeek, String defaultValue);
}
