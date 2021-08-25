package com.p_v.flexiblecalendar.view.impl;

import android.view.View;
import android.view.ViewGroup;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.IDateCellViewDrawer;

/**
 * Default date cell view drawer
 *
 * @author p-v
 */
public class DateCellViewImpl implements IDateCellViewDrawer {

    private FlexibleCalendarView.CalendarView calendarView;

    public DateCellViewImpl(FlexibleCalendarView.CalendarView calendarView) {
        this.calendarView = calendarView;
        System.out.println(" FlexibleCalendarGridAdapter cellViewDrawer.getCellView pos1 constructor "+calendarView);
    }

    @Override
    public void setCalendarView(FlexibleCalendarView.CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    @Override
    public BaseCellView getCellView(int position, View convertView, ViewGroup parent, @BaseCellView.CellType int cellType) {
        System.out.println(" FlexibleCalendarGridAdapter cellViewDrawer.getCellView pos1 "+convertView);
        System.out.println(" FlexibleCalendarGridAdapter cellViewDrawer.getCellView pos2 "+calendarView.getCellView(position, convertView, parent, cellType));
        return calendarView.getCellView(position, convertView, parent, cellType);
    }
}
