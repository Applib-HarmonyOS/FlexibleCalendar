package com.pv.flexiblecalendarexample.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import com.pv.flexiblecalendar.FlexibleCalendarView;
import com.pv.flexiblecalendar.view.BaseCellView;
import com.pv.flexiblecalendarexample.ResourceTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * CalendarActivity.
 */
public class CalendarActivity2 extends AbilitySlice {
    /**
     * calendarview.
     */
    FlexibleCalendarView calendarView;

    /**
     * onStart.
     *
     * @param intent intent
     */
    @Override
    protected void onStart(final Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_main);
        initView();
    }

    /**
     * Intiview.
     */
    private void initView() {
        calendarView = (FlexibleCalendarView) findComponentById(ResourceTable.Id_month_view);
        setMonthChangeListener();
        setCalendarView();
        setDataEventProvider();
        setClick();
    }

    /**
     * set Click.
     */
    private void setClick() {
        Button button = (Button) findComponentById(ResourceTable.Id_button);
        button.setClickedListener(component -> {
            if (calendarView.isComponentDisplayed()) {
                calendarView.setVisibility(Component.HIDE);
                calendarView.collapse();
            } else {
                calendarView.expand();
            }
        });
    }

    /**
     * set data event provider.
     */
    private void setDataEventProvider() {
        calendarView.setEventDataProvider((int year, int month, int day) -> {
            List<CustomEvent> colorLst1 = null;
            if (year == 2015 && month == 7 && day == 25) {
                colorLst1 = new ArrayList<>();
                colorLst1.add(new CustomEvent(ResourceTable.Color_holo_green_dark));
                colorLst1.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
                colorLst1.add(new CustomEvent(ResourceTable.Color_holo_purple));
                return colorLst1;
            }
            if (year == 2015 && month == 7 && day == 8) {
                colorLst1 = new ArrayList<>();
                colorLst1.add(new CustomEvent(ResourceTable.Color_holo_green_dark));
                colorLst1.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
                colorLst1.add(new CustomEvent(ResourceTable.Color_holo_purple));
                return colorLst1;
            }
            if (year == 2015 && month == 7 && day == 5) {
                colorLst1 = new ArrayList<>();
                colorLst1.add(new CustomEvent(ResourceTable.Color_holo_purple));
                return colorLst1;
            }
            return colorLst1;
        });
    }

    /**
     * set calendar view.
     */
    private void setCalendarView() {
        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(final int position, final Component convertView
                    , final ComponentContainer parent, final int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter scatter = LayoutScatter.getInstance(CalendarActivity2.this);
                    cellView = (BaseCellView) scatter.parse(ResourceTable.Layout_calendar2_date_cell_view, parent
                            , false);
                }
                try {
                    if (cellType == BaseCellView.TODAY) {
                        cellView.setTextColor(new Color(getResourceManager()
                                .getElement(ResourceTable.Color_holo_red_dark).getColor()));
                        cellView.setTextSize(15);
                    } else {
                        cellView.setTextColor(new Color(getResourceManager()
                                .getElement(ResourceTable.Color_color_white).getColor()));
                        cellView.setTextSize(12);
                    }
                } catch (IOException | NotExistException | WrongTypeException e) {
                    e.printStackTrace();
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(final int position, final Component convertView
                    , final ComponentContainer parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity2.this);
                    cellView = (BaseCellView) inflater.parse(ResourceTable.Layout_calendar2_week_cell_view, parent
                            , false);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(final int dayOfWeek, final String defaultValue) {
                return null;
            }
        });
    }

    /**
     * set month change listener.
     */
    private void setMonthChangeListener() {
        calendarView.setOnMonthChangeListener((int year, int month, int direction) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, 1);
            ToastDialog toastDialog = new ToastDialog(getContext());
            toastDialog.setText(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH) + " " + year);
            toastDialog.show();

        });
    }
}

