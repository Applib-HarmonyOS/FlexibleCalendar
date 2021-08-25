package com.pv.flexiblecalendarexample.slice;

import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import com.pv.flexiblecalendar.FlexibleCalendarView;
import com.pv.flexiblecalendar.entity.CalendarEvent;
import com.pv.flexiblecalendar.view.BaseCellView;
import com.pv.flexiblecalendar.view.SquareCellView;
import com.pv.flexiblecalendarexample.ResourceTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * CalendarActivityFragment.
 */
public class CalendarActivityFragment extends Fraction implements FlexibleCalendarView.OnMonthChangeListener
                                              , FlexibleCalendarView.OnDateClickListener {
    /**
     * container.
     */
    private ComponentContainer container;
    /**
     * context.
     */
    private Context context;
    /**
     * calendar view.
     */
    private FlexibleCalendarView calendarView;
    /**
     * text view.
     */
    private Text someTextView;
    /**
     * calendar instance.
     */
    private Calendar calendar = Calendar.getInstance();

    /**
     * arg constructor.
     *
     * @param context context
     */
    public CalendarActivityFragment(final Context context) {
        calendarView.setOnMonthChangeListener((FlexibleCalendarView.OnMonthChangeListener) context);
        calendarView.setOnDateClickListener((FlexibleCalendarView.OnDateClickListener) context);
    }

    /**
     * arg constructor.
     *
     * @param context            context
     * @param componentContainer component container
     */
    public CalendarActivityFragment(final Context context, final ComponentContainer componentContainer) {
        this.context = context;
        this.container = componentContainer;
    }

    /**
     * component attached.
     *
     * @param scatter   scatter
     * @param container container
     * @param intent    intent
     */
    @Override
    protected Component onComponentAttached(final LayoutScatter scatter, final ComponentContainer container
                                            , final Intent intent) {
        scatter.parse(ResourceTable.Layout_fragment_calendar, container, false);
        updateTitle(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth());
        return super.onComponentAttached(scatter, container, intent);
    }

    /**
     * get component.
     *
     * @return component
     */
    @Override
    public Component getComponent() {
        Component component = LayoutBoost.inflate(this.context, ResourceTable.Layout_fragment_calendar, this.container
                                                 , false);
        initView(component);
        return super.getComponent();
    }

    /**
     * init.
     *
     * @param component component
     */
    private void initView(final Component component) {
        calendarView = (FlexibleCalendarView) component.findComponentById(ResourceTable.Id_calendar_view);
        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {

            @Override
            public BaseCellView getCellView(final int position, final Component convertView
                                            , final ComponentContainer parent, final int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;

                if (cellView == null) {
                    LayoutScatter scatter = LayoutScatter.getInstance(getContext());
                    cellView = (BaseCellView) scatter.parse(ResourceTable.Layout_calendar1_date_cell_view, parent
                                , false);
                }

                if (cellType == BaseCellView.OUTSIDE_MONTH) {
                    try {
                        cellView.setTextColor(new Color(getResourceManager()
                                .getElement(ResourceTable.Color_date_outside_month_text_color_activity_1).getColor()));
                    } catch (IOException | NotExistException | WrongTypeException e) {
                        e.printStackTrace();
                    }
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(final int position, final Component convertView
                                                   , final ComponentContainer parent) {
                BaseCellView cellView = (BaseCellView) convertView;

                if (cellView == null) {
                    LayoutScatter scatter = LayoutScatter.getInstance(getContext());
                    cellView = (SquareCellView) scatter.parse(ResourceTable.Layout_calendar1_week_cell_view, parent
                                , false);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(final int dayOfWeek, final String defaultValue) {
                return String.valueOf(defaultValue.charAt(0));
            }
        });
        calendarView.setOnMonthChangeListener((FlexibleCalendarView.OnMonthChangeListener) context);
        calendarView.setOnDateClickListener((FlexibleCalendarView.OnDateClickListener) context);
        fillEvents();
        final Button nextDateBtn = (Button) component.findComponentById(ResourceTable.Id_move_to_next_date);
        final Button prevDateBtn = (Button) component.findComponentById(ResourceTable.Id_move_to_previous_date);
        final Button nextMonthBtn = (Button) component.findComponentById(ResourceTable.Id_move_to_next_month);
        final Button prevMonthBtn = (Button) component.findComponentById(ResourceTable.Id_move_to_previous_month);
        final Button goToCurrentDayBtn = (Button) component.findComponentById(ResourceTable.Id_go_to_current_day);
        final Button shwDtsOut = (Button) component.findComponentById(ResourceTable.Id_show_dates_outside_month);
        nextDateBtn.setClickedListener((Component view) -> {
            calendarView.moveToNextDate();
        });
        prevDateBtn.setClickedListener((Component view) -> {
            calendarView.moveToPreviousDate();
        });
        nextMonthBtn.setClickedListener((Component view) -> {
            calendarView.moveToNextMonth();
        });
        prevMonthBtn.setClickedListener((Component view) -> {
            calendarView.moveToPreviousMonth();
        });
        goToCurrentDayBtn.setClickedListener((Component view) -> {
            calendarView.goToCurrentDay();
        });
        shwDtsOut.setClickedListener((Component view) -> {

            if (calendarView.getShowDatesOutsideMonth()) {
                calendarView.setShowDatesOutsideMonth(false);
                ((Button) view).setText("Show dates outside month");
            } else {
                ((Button) view).setText("Hide dates outside month");
                calendarView.setShowDatesOutsideMonth(true);
            }
        });
    }

    /**
     * fill events.
     */
    private void fillEvents() {
        calendarView.setEventDataProvider((int year, int month, int day) -> {
            List<CalendarEvent> eventColors = null;
            calcOne(year, month, day, eventColors);
            calcTwo(year, month, day, eventColors);
            calcThree(year, month, day, eventColors);
            calcFour(year, month, day, eventColors);
            return eventColors;
        });
    }

    private List<CalendarEvent> calcFour(int year, int month, int day, List<CalendarEvent> eventColors) {
        if (year == 2016 && month == 10 && day == 31
                || year == 2016 && month == 10 && day == 22
                || year == 2016 && month == 10 && day == 18
                || year == 2016 && month == 10 && day == 11) {
            eventColors = new ArrayList<>(3);
            eventColors.add(new CalendarEvent(ResourceTable.Color_holo_red_dark));
            eventColors.add(new CalendarEvent(ResourceTable.Color_holo_orange_light));
            eventColors.add(new CalendarEvent(ResourceTable.Color_holo_purple));
            return eventColors;
        }
        return eventColors;
    }

    private List<CalendarEvent> calcThree(int year, int month, int day, List<CalendarEvent> eventColors) {
        if (year == 2016 && month == 10 && day == 7
                || year == 2016 && month == 10 && day == 29
                || year == 2016 && month == 10 && day == 5
                || year == 2016 && month == 10 && day == 9) {
            eventColors = new ArrayList<>(1);
            eventColors.add(new CalendarEvent(ResourceTable.Color_holo_blue_light));
            return eventColors;
        }
        return eventColors;
    }

    private List<CalendarEvent> calcTwo(int year, int month, int day, List<CalendarEvent> eventColors) {
        if (year == 2016 && month == 10 && day == 12) {
            eventColors = new ArrayList<>(2);
            eventColors.add(new CalendarEvent(ResourceTable.Color_holo_blue_light));
            eventColors.add(new CalendarEvent(ResourceTable.Color_holo_purple));
            return eventColors;
        }
        return eventColors;
    }

    private List<CalendarEvent> calcOne(int year, int month, int day, List<CalendarEvent> eventColors) {
        if (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && day == calendar
                .get(Calendar.DAY_OF_MONTH)) {
            eventColors = new ArrayList<>(2);
            eventColors.add(new CalendarEvent(ResourceTable.Color_holo_blue_light));
            eventColors.add(new CalendarEvent(ResourceTable.Color_holo_purple));
            return eventColors;
        }
        return eventColors;
    }

    /**
     * update title.
     *
     * @param year  year
     * @param month month
     */
    private void updateTitle(final int year, final int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        someTextView.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
                this.getResourceManager().getConfiguration().getFirstLocale()) + " " + year);
    }

    /**
     * month change.
     *
     * @param year      year
     * @param month     month
     * @param direction direction
     */
    @Override
    public void onMonthChange(final int year, final int month, final int direction) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        updateTitle(year, month);
    }

    /**
     * on date click.
     *
     * @param year  year
     * @param month month
     * @param day   day
     */
    @Override
    public void onDateClick(final int year, final int month, final int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        ToastDialog toastDialog = new ToastDialog(getContext());
        toastDialog.setText(cal.getTime().toString() + " Clicked");
        toastDialog.show();
    }
}