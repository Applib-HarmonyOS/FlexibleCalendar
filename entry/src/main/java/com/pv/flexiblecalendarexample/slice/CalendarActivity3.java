package com.pv.flexiblecalendarexample.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DatePicker;
import ohos.agp.components.LayoutScatter;
import ohos.agp.window.dialog.ToastDialog;
import com.pv.flexiblecalendar.FlexibleCalendarView;
import com.pv.flexiblecalendar.view.BaseCellView;
import com.pv.flexiblecalendarexample.ResourceTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CalenderActivity.
 */
public class CalendarActivity3 extends AbilitySlice {
    /**
     * Map object to hold values in eventMap.
     */
    private Map<Integer, List<CustomEvent>> eventMap;

    /**
     * onStart.
     *
     * @param intent intent
     */
    @Override
    protected void onStart(final Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_calendar_activity3);
        initView();
        initializeEvents();
    }

    /**
     * To initializeEvents.
     */
    private void initializeEvents() {
        eventMap = new HashMap<>();
        List<CustomEvent> colorLst = new ArrayList<>();
        colorLst.add(new CustomEvent(ResourceTable.Color_holo_red_dark));
        eventMap.put(25, colorLst);

        List<CustomEvent> colorLst1 = new ArrayList<>();
        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_red_dark));
        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_purple));
        eventMap.put(22, colorLst1);

        List<CustomEvent> colorLst2 = new ArrayList<>();
        colorLst2.add(new CustomEvent(ResourceTable.Color_holo_red_dark));
        colorLst2.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
        colorLst2.add(new CustomEvent(ResourceTable.Color_holo_purple));
        eventMap.put(28, colorLst1);

        List<CustomEvent> colorLst3 = new ArrayList<>();
        colorLst3.add(new CustomEvent(ResourceTable.Color_holo_red_dark));
        colorLst3.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
        eventMap.put(29, colorLst1);
    }

    /**
     * InitView.
     */
    private void initView() {
        FlexibleCalendarView calendarView = (FlexibleCalendarView) findComponentById(ResourceTable.Id_calendar_view);
        calendarView.setMonthViewHorizontalSpacing(10);
        calendarView.setMonthViewVerticalSpacing(10);
        calendarView.setOnMonthChangeListener((int year, int month, @FlexibleCalendarView.Direction int direction) -> {
            ToastDialog toastDialog = new ToastDialog(getContext());
            toastDialog.setText(year + "" + month + 1);
            toastDialog.show();
        });

        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(final int position, final Component convertView
                    , final ComponentContainer parent, final int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity3.this);
                    cellView = (BaseCellView) inflater.parse(ResourceTable.Layout_calendar3_date_cell_view, parent
                            , false);
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(final int position, final Component convertView
                    , final ComponentContainer parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity3.this);
                    cellView = (BaseCellView) inflater.parse(ResourceTable.Layout_calendar3_week_cell_view, parent
                            , false);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(final int dayOfWeek, final String defaultValue) {
                return null;
            }
        });

        calendarView.setEventDataProvider((int year, int month, int day) -> {
            return getEvents(year, month, day);
        });

        findComponentById(ResourceTable.Id_update_events_button).setClickedListener(component -> {
            List<CustomEvent> colorLst1 = new ArrayList<>();
            colorLst1.add(new CustomEvent(ResourceTable.Color_holo_red_dark));
            colorLst1.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
            colorLst1.add(new CustomEvent(ResourceTable.Color_holo_purple));
            eventMap.put(2, colorLst1);
            calendarView.refresh();
        });
        DatePicker datePicker = (DatePicker) findComponentById(ResourceTable.Id_date_picker);
        datePicker.setValueChangedListener((DatePicker datePicking, int year, int monthOfYear, int dayOfMonth) -> {
            calendarView.selectDate(year, monthOfYear, dayOfMonth);
        });
    }

    /**
     * get events.
     *
     * @param year  year
     *
     * @param month month
     *
     * @param day   day
     *
     * @return list
     */
    public List<CustomEvent> getEvents(final int year, final int month, final int day) {
        return eventMap.get(day);
    }
}



