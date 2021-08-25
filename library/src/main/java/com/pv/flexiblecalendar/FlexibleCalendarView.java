package com.pv.flexiblecalendar;

import ohos.agp.components.AttrSet;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.PageSlider;
import ohos.agp.components.TableLayoutManager;
import ohos.app.Context;
import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.pv.flexiblecalendar.entity.Event;
import com.pv.flexiblecalendar.entity.SelectedDateItem;
import com.pv.flexiblecalendar.exception.HighValueException;
import com.pv.flexiblecalendar.view.BaseCellView;
import com.pv.flexiblecalendar.view.impl.DateCellViewImpl;
import com.pv.flexiblecalendar.view.impl.WeekdayCellViewImpl;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.List;

/**
 * FlexibleCalendarView.
 *
 * @author p-v
 */
public class FlexibleCalendarView extends DirectionalLayout implements
        FlexibleCalendarGridAdapter.OnDateCellItemClickListener,
        FlexibleCalendarGridAdapter.MonthEventFetcher, Component.EstimateSizeListener {
    /*
     * RIGHT.
     */
    public static final int RIGHT = 0;
    /*
     * LEFT.
     */
    public static final int LEFT = 1;
    /*
     * HIGH VALUE.
     */
    private static final int HIGH_VALUE = 20000;
    /*
     * infinite page adapter.
     */
    private InfinitePagerAdapter monthInfPagerAdapter;
    /*
     * weekday name display adapter.
     */
    private WeekdayNameDisplayAdapter weekdayDisplayAdapter;
    /*
     * month view pager adapter.
     */
    private MonthViewPagerAdapter monthViewPagerAdapter;
    /*
     * context.
     */
    private Context context;
    /**
     * View pager for the month view.
     */
    private MonthViewPager monthViewPager;
    /**
     * month change listener.
     */
    private OnMonthChangeListener onMonthChangeListener;
    /**
     * date click listener.
     */
    private OnDateClickListener onDateClickListener;
    /**
     * event data provider.
     */
    private EventDataProvider eventDataProvider;
    /**
     * calendar view.
     */
    private CalendarView calendarView;
    /**
     * display year.
     */
    private int displayYear;
    /*
     * start display day.
     */
    private int displayMonth;
    /*
     * start display day.
     */
    private int startDisplayDay;
    /*
     * month day horizontal spacing.
     */
    private int monthDayHorizontalSpacing;
    /*
     * month day vertical spacing.
     */
    private int monthDayVerticalSpacing;
    /*
     * row ht.
     */
    int rowHeight = 0;
    /*
     * numb of rows.
     */
    int numOfRows = 0;
    /*
     * show date outside month.
     */
    private boolean showDatesOutsideMonth;
    /*
     * decorate dates outside month.
     */
    private boolean decorateDatesOutsideMonth;
    /*
     * disable auto date selection.
     */
    private boolean disableAutoDateSelection;
    /**
     * Reset adapters flag used internally.
     * For tracking go to current month
     */
    private boolean resetAdapters;
    /**
     * Currently selected date item.
     */
    private SelectedDateItem selectedDateItem;
    /*
     * Currently user selected item.
     */
    private SelectedDateItem userSelectedItem;
    /**
     * Internal flag to override the computed date on month change.
     */
    private boolean shouldOverrideComputedDate;
    /**
     * First day of the week in the calendar.
     */
    private int startDayOfTheWeek;
    /*
     * last position.
     */
    private int lastPosition;

    /**
     * paramater constructor.
     *
     * @param context context
     */
    public FlexibleCalendarView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * paramater constructor.
     *
     * @param context context
     *
     * @param attrs attrs
     */
    public FlexibleCalendarView(Context context, AttrSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    /**
     * paramater constructor.
     *
     * @param context context
     *
     * @param attrs attrs
     *
     * @param defStyleAttr defStyleAttr
     */
    public FlexibleCalendarView(Context context, AttrSet attrs, String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /**
     * init.
     */
    private void init() {
        setAttributes();
        setOrientation(VERTICAL);
        setEstimateSizeListener(this);

        //initialize the default calendar view
        calendarView = new DefaultCalendarView();

        //create week view header
        ListContainer weekDisplayView = new ListContainer(context);
        TableLayoutManager tableLayoutManager = new TableLayoutManager();
        tableLayoutManager.setColumnCount(7);
        tableLayoutManager.setRowCount(7);
        weekDisplayView.setLayoutManager(tableLayoutManager);
        weekDisplayView.verifyLayoutConfig(new ComponentContainer.LayoutConfig(
                ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
        weekdayDisplayAdapter = new WeekdayNameDisplayAdapter(getContext(), startDayOfTheWeek, 200);
        //setting default week cell view
        weekdayDisplayAdapter.setCellView(new WeekdayCellViewImpl(calendarView));
        weekDisplayView.setItemProvider(weekdayDisplayAdapter);
        this.addComponent(weekDisplayView);

        //setup month view
        monthViewPager = new MonthViewPager(context);
        numOfRows = showDatesOutsideMonth ? 6 : FlexibleCalendarHelper.getNumOfRowsForTheMonth(displayYear,
                displayMonth);
        monthViewPagerAdapter = new MonthViewPagerAdapter(context, displayYear, displayMonth, this,
                showDatesOutsideMonth, decorateDatesOutsideMonth, startDayOfTheWeek, disableAutoDateSelection);
        monthViewPagerAdapter.setMonthEventFetcher(this);
        monthViewPagerAdapter.setSpacing(monthDayHorizontalSpacing, monthDayVerticalSpacing);

        //set the default cell view
        monthViewPagerAdapter.setCellViewDrawer(new DateCellViewImpl(calendarView));

        monthInfPagerAdapter = new InfinitePagerAdapter(monthViewPagerAdapter, context);
        //Initializing with the offset value
        lastPosition = monthInfPagerAdapter.getRealCount() * 100;
        monthViewPager.setProvider(monthInfPagerAdapter);
        monthViewPager.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT,
                ComponentContainer.LayoutConfig.MATCH_CONTENT));
        monthViewPager.addPageChangedListener(new MonthChangeListener());
        monthViewPager.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
        monthViewPager.setHeight(ComponentContainer.LayoutConfig.MATCH_PARENT);

        //initialize with the current selected item
        selectedDateItem = new SelectedDateItem(displayYear, displayMonth, startDisplayDay);
        monthViewPagerAdapter.setSelectedItem(selectedDateItem);
        this.addComponent(monthViewPager);
    }

    /**
     * set attributes.
     */
    private void setAttributes() {
        Calendar cal = Calendar.getInstance(FlexibleCalendarHelper.getLocale(context));
        displayMonth = cal.get(Calendar.MONTH);
        displayYear = cal.get(Calendar.YEAR);
        startDisplayDay = cal.get(Calendar.DAY_OF_MONTH);
        monthDayHorizontalSpacing = 5;
        monthDayVerticalSpacing = 5;
        showDatesOutsideMonth = false;
        decorateDatesOutsideMonth = false;
        disableAutoDateSelection = false;
        startDayOfTheWeek = 1;
        if (startDayOfTheWeek < 1 || startDayOfTheWeek > 7) {
            // setting the start day to sunday in case of invalid input
            startDayOfTheWeek = Calendar.SUNDAY;
        }
    }

    /**
     * Expand the view with animation.
     */
    public void expand() {
        estimateSize(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
        getLayoutConfig().height = 0;
        setVisibility(Component.VISIBLE);
    }

    /**
     * Collapse the view with animation.
     */
    public void collapse() {
        setVisibility(Component.HIDE);
    }

    /**
     * set on month change listener.
     *
     * @param onMonthChangeListener onMonthChangeListener
     */
    public void setOnMonthChangeListener(OnMonthChangeListener onMonthChangeListener) {
        this.onMonthChangeListener = onMonthChangeListener;
    }

    /**
     * set on date click listener.
     *
     * @param onDateClickListener onDateClickListener
     */
    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    /**
     * set event data listener.
     *
     * @param eventDataProvider eventDataProvider
     */
    public void setEventDataProvider(EventDataProvider eventDataProvider) {
        this.eventDataProvider = eventDataProvider;
    }

    /**
     * set on date click.
     *
     * @param selectedItem selectedItem
     */
    @Override
    public void onDateClick(SelectedDateItem selectedItem) {
        if (selectedDateItem.getYear() != selectedItem.getYear() || selectedDateItem.getMonth()
                != selectedItem.getMonth()) {
            shouldOverrideComputedDate = true;
            //different month
            int monthDifference = FlexibleCalendarHelper.getMonthDifference(selectedItem.getYear(),
                    selectedItem.getMonth(), selectedDateItem.getYear(), selectedDateItem.getMonth());
            this.selectedDateItem = selectedItem;
            //move back or forth based on the monthDifference
            if (monthDifference > 0) {
                moveToPreviousMonth();
            } else {
                moveToNextMonth();
            }
        } else {
            //do nothing if same month
            this.selectedDateItem = selectedItem;
        }

        // redraw current month grid as the events were getting disappeared for selected day
        redrawMonthGrid(lastPosition % MonthViewPagerAdapter.VIEWS_IN_PAGER);

        // set user selected date item
        if (disableAutoDateSelection) {
            this.userSelectedItem = SelectedDateItem.newInstance(selectedItem);
        }

        if (onDateClickListener != null) {
            onDateClickListener.onDateClick(selectedItem.getYear(), selectedItem.getMonth(), selectedItem.getDay());
        }
    }

    /**
     * redraw month grid.
     *
     * @param position position
     */
    private void redrawMonthGrid(int position) {
        if (position == -1) {
            //redraw all
            for (int i = 0; i <= 3; i++) {
                Component view = monthViewPager.findComponentById(Integer.parseInt(
                        MonthViewPagerAdapter.GRID_TAG_PREFIX + i));
                reAddAdapter(view);
            }
        } else {
            Component view = monthViewPager.findComponentById(Integer.parseInt(
                    MonthViewPagerAdapter.GRID_TAG_PREFIX + position));
            reAddAdapter(view);
        }
    }

    /**
     * re add adapter.
     *
     * @param view view
     */
    private void reAddAdapter(Component view) {
        if (view != null) {
            BaseItemProvider adapter = ((ListContainer) view).getItemProvider();
            ((ListContainer) view).setItemProvider(adapter);
        }
    }

    /**
     * get Selected date item.
     *
     * @return currently selected date
     */
    public SelectedDateItem getSelectedDateItem() {
        if (disableAutoDateSelection) {
            return userSelectedItem == null ? null : SelectedDateItem.newInstance(userSelectedItem);
        }
        return SelectedDateItem.newInstance(selectedDateItem);
    }

    /**
     * Move the selection to the next day.
     */
    public void moveToPreviousDate() {
        // in case when auto selection is disabled
        // do nothing if there is nothing selected by the user
        if (disableAutoDateSelection && userSelectedItem == null) {
            return;
        }

        if (selectedDateItem != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(selectedDateItem.getYear(), selectedDateItem.getMonth(), selectedDateItem.getDay());
            cal.add(Calendar.DATE, -1);

            if (selectedDateItem.getMonth() != cal.get(Calendar.MONTH)) {
                //update selected date item
                selectedDateItem.setDay(cal.get(Calendar.DAY_OF_MONTH));
                selectedDateItem.setMonth(cal.get(Calendar.MONTH));
                selectedDateItem.setYear(cal.get(Calendar.YEAR));

                //set true to override the computed date in onPageSelected method
                shouldOverrideComputedDate = true;

                //scroll to previous month
                moveToPreviousMonth();
            } else {
                selectedDateItem.setDay(cal.get(Calendar.DAY_OF_MONTH));
                selectedDateItem.setMonth(cal.get(Calendar.MONTH));
                selectedDateItem.setYear(cal.get(Calendar.YEAR));
                monthViewPagerAdapter.setSelectedItem(selectedDateItem);
            }
        }
    }

    /**
     * Move the selection to the previous day.
     */
    public void moveToNextDate() {
        // in case when auto selection is disabled
        // do nothing if there is nothing selected by the user
        if (disableAutoDateSelection && userSelectedItem == null) {
            return;
        }

        if (selectedDateItem != null) {

            Calendar cal = Calendar.getInstance();
            cal.set(selectedDateItem.getYear(), selectedDateItem.getMonth(), selectedDateItem.getDay());
            cal.add(Calendar.DATE, 1);

            if (selectedDateItem.getMonth() != cal.get(Calendar.MONTH)) {
                moveToNextMonth();
            } else {
                selectedDateItem.setDay(cal.get(Calendar.DAY_OF_MONTH));
                selectedDateItem.setMonth(cal.get(Calendar.MONTH));
                selectedDateItem.setYear(cal.get(Calendar.YEAR));
                monthViewPagerAdapter.setSelectedItem(selectedDateItem);
            }
        }
    }

    /**
     * get events for the day.
     *
     * @param year year
     *
     * @param month month
     *
     * @param day day
     */
    @Override
    public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
        return eventDataProvider == null
                ? null : eventDataProvider.getEventsForTheDay(year, month, day);
    }

    /**
     * Set the customized calendar view for the calendar for customizing cells.
     *
     * @param calendar calendar
     */
    public void setCalendarView(CalendarView calendar) {
        this.calendarView = calendar;
        monthViewPagerAdapter.getCellViewDrawer().setCalendarView(calendarView);
        weekdayDisplayAdapter.getCellViewDrawer().setCalendarView(calendarView);
    }

    /**
     * Sets the month view cells horizontal spacing.
     *
     * @param spacing spacing
     */
    public void setMonthViewHorizontalSpacing(int spacing) {
        this.monthDayHorizontalSpacing = spacing;
        monthViewPagerAdapter.setSpacing(monthDayHorizontalSpacing, monthDayVerticalSpacing);
    }

    /**
     * Sets the month view cells vertical spacing.
     *
     * @param spacing spacing
     */
    public void setMonthViewVerticalSpacing(int spacing) {
        this.monthDayVerticalSpacing = spacing;
        monthViewPagerAdapter.setSpacing(monthDayHorizontalSpacing, monthDayVerticalSpacing);
    }

    /**
     * move to next month.
     */
    public void moveToNextMonth() {
        moveToPosition(1);
    }

    /**
     * move to position with respect to current position.
     */
    private void moveToPosition(int position) {
        monthViewPager.setCurrentPage(lastPosition + position - monthInfPagerAdapter.getRealCount() * 100,
                true);
    }

    /**
     * move to previous month.
     */
    public void moveToPreviousMonth() {
        moveToPosition(-1);
    }

    /**
     * move the position to the current month.
     */
    public void goToCurrentMonth() {
        //check has to go left side or right
        int monthDifference = FlexibleCalendarHelper
                .getMonthDifference(displayYear, displayMonth);
        if (monthDifference != 0) {
            resetAdapters = true;
            if (monthDifference < 0) {
                monthInfPagerAdapter.notifyDataChanged();
            }
            moveToPosition(monthDifference);
        }
    }

    /**
     * move the position to today's date.
     */
    public void goToCurrentDay() {
        //current date
        Calendar cal = Calendar.getInstance();
        //update selected date item
        selectedDateItem.setDay(cal.get(Calendar.DAY_OF_MONTH));
        selectedDateItem.setMonth(cal.get(Calendar.MONTH));
        selectedDateItem.setYear(cal.get(Calendar.YEAR));

        if (disableAutoDateSelection) {
            this.userSelectedItem = SelectedDateItem.newInstance(selectedDateItem);
        }
        //check has to go left side or right
        int monthDifference = FlexibleCalendarHelper
                .getMonthDifference(displayYear, displayMonth);
        if (monthDifference != 0) {
            resetAdapters = true;
            if (monthDifference < 0) {
                //set fake count to avoid freezing in InfiniteViewPager as it iterates to Integer.MAX_VALUE
                monthInfPagerAdapter.setFakeCount(lastPosition);
                monthInfPagerAdapter.notifyDataChanged();
            }
            //set true to override the computed date in onPageSelected method
            shouldOverrideComputedDate = true;
            moveToPosition(monthDifference);
        } else {
            FlexibleCalendarGridAdapter currentlyVisibleAdapter = monthViewPagerAdapter
                    .getMonthAdapterAtPosition(lastPosition % MonthViewPagerAdapter.VIEWS_IN_PAGER);
            currentlyVisibleAdapter.notifyDataChanged();
        }
    }

    /**
     * Get the show dates outside month flag.
     *
     * @return true if showDatesOutsideMonth is enable, false otherwise
     */
    public boolean getShowDatesOutsideMonth() {
        return showDatesOutsideMonth;
    }

    /**
     * Flag to show dates outside the month. Default value is false which will show only the dates.
     *
     * @param showDatesOutsideMonth set true to show the dates outside month
     */
    public void setShowDatesOutsideMonth(boolean showDatesOutsideMonth) {
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        monthViewPager.invalidate();
        monthViewPagerAdapter.setShowDatesOutsideMonth(showDatesOutsideMonth);
    }

    /**
     * Refresh the calendar view. Invalidate and redraw all the cells.
     */
    public void refresh() {
        redrawMonthGrid(-1);
    }

    /**
     * Set the start day of week.
     *
     * <p>Set the start day of week.</p>
     * SUNDAY = 1,
     * MONDAY = 2,
     * TUESDAY = 3,
     * WEDNESDAY = 4,
     * THURSDAY = 5,
     * FRIDAY = 6,
     * SATURDAY = 7
     *
     * @param startDayOfTheWeek Add values between 1 to 7. Defaults to 1 if entered outside boundary.
     */
    public void setStartDayOfTheWeek(int startDayOfTheWeek) {
        this.startDayOfTheWeek = startDayOfTheWeek;
        if (startDayOfTheWeek < 1 || startDayOfTheWeek > 7) {
            startDayOfTheWeek = 1;
        }
        monthViewPagerAdapter.setStartDayOfTheWeek(startDayOfTheWeek);
        weekdayDisplayAdapter.setStartDayOfTheWeek(startDayOfTheWeek);
    }

    /**
     * Select the date in the FlexibleCalendar.
     *
     * @param calendar calendar
     */
    public void selectDate(Calendar calendar) {
        if (calendar == null) {
            return;
        }
        selectDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Select the date in the FlexibleCalendar.
     *
     * @param newYear new year
     * @param newMonth new month
     * @param newDay new day
     */
    public void selectDate(int newYear, int newMonth, int newDay) {
        int monthDifference = FlexibleCalendarHelper
                .getMonthDifference(selectedDateItem.getYear(), selectedDateItem.getMonth(),
                        newYear, newMonth);

        if (Math.abs(monthDifference) > HIGH_VALUE) {
            //throw exception for high values
            throw new HighValueException("Difference too high to make the change");
        }

        selectedDateItem.setDay(newDay);
        selectedDateItem.setMonth(newMonth);
        selectedDateItem.setYear(newYear);

        if (disableAutoDateSelection) {
            this.userSelectedItem = SelectedDateItem.newInstance(selectedDateItem);
        }

        if (monthDifference != 0) {
            //different month
            resetAdapters = true;
            if (monthDifference < 0) {
                //set fake count to avoid freezing in InfiniteViewPager as it iterates to Integer.MAX_VALUE
                monthInfPagerAdapter.setFakeCount(lastPosition);
                monthInfPagerAdapter.notifyDataChanged();
            }
            //set true to override the computed date in onPageSelected method
            shouldOverrideComputedDate = true;
            moveToPosition(monthDifference);
            // select the user selected date item
            if (disableAutoDateSelection) {
                monthViewPagerAdapter
                        .getMonthAdapterAtPosition(lastPosition % MonthViewPagerAdapter.VIEWS_IN_PAGER)
                        .setSelectedItem(selectedDateItem, true, true);
            }
        } else {
            monthViewPagerAdapter
                    .getMonthAdapterAtPosition(lastPosition % MonthViewPagerAdapter.VIEWS_IN_PAGER)
                    .setSelectedItem(selectedDateItem, true, true);
        }

    }

    /**
     * Customize Calendar using this interface.
     */
    public interface CalendarView {
        /**
         * Cell view for the month.
         *
         * @param position position
         * @param convertView convert view
         * @param parent parent view
         * @param cellType cell type
         * @return BaseCellView object
         */
        BaseCellView getCellView(int position, Component convertView, ComponentContainer parent,
                                 @BaseCellView.CellType int cellType);

        /**
         * Cell view for the weekday in the header.
         *
         * @param position position
         * @param convertView convert view
         * @param parent parent view
         * @return BaseCellView object
         */
        BaseCellView getWeekdayCellView(int position, Component convertView, ComponentContainer parent);

        /**
         * Get display value for the day of week.
         *
         * @param dayOfWeek    the value of day of week where 1 is SUNDAY, 2 is MONDAY ... 7 is SATURDAY
         * @param defaultValue the default value for the day of week
         * @return String object
         */
        String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue);
    }

    /**
     * Event Data Provider used for displaying events for a particular date.
     */
    public interface EventDataProvider {
        List<? extends Event> getEventsForTheDay(int year, int month, int day);
    }

    /**
     * Listener for month change.
     */
    public interface OnMonthChangeListener {
        /**
         * Called whenever there is a month change.
         *
         * @param year      the selected month's year
         * @param month     the selected month
         * @param direction LEFT or RIGHT
         */
        void onMonthChange(int year, int month, @Direction int direction);
    }

    /**
     * Click listener for date cell.
     */
    public interface OnDateClickListener {
        /**
         * Called whenever a date cell is clicked.
         *
         * @param day   selected day
         * @param month selected month
         * @param year  selected year
         */
        void onDateClick(int year, int month, int day);
    }

    /**
     * Direction for movement of FlexibleCalendarView left or right.
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }

    /**
     * Default calendar view for internal usage.
     */
    private class DefaultCalendarView implements CalendarView {

        @Override
        public BaseCellView getCellView(int position, Component convertView, ComponentContainer parent,
                                        int cellType) {
            BaseCellView cellView = (BaseCellView) convertView;
            if (cellView == null) {
                LayoutScatter scatter = LayoutScatter.getInstance(context);
                cellView = (BaseCellView) scatter.parse(ResourceTable.Layout_square_cell_layout, parent,
                        false);
            }
            return cellView;
        }

        @Override
        public BaseCellView getWeekdayCellView(int position, Component convertView, ComponentContainer parent) {
            BaseCellView cellView = (BaseCellView) convertView;
            if (cellView == null) {
                LayoutScatter scatter = LayoutScatter.getInstance(context);
                cellView = (BaseCellView) scatter.parse(ResourceTable.Layout_square_cell_layout, parent,
                        false);
            }
            return cellView;
        }

        @Override
        public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
            return "";
        }
    }

    private class MonthChangeListener implements PageSlider.PageChangedListener {

        private SelectedDateItem computeNewSelectedDateItem(int difference) {

            Calendar cal = Calendar.getInstance();
            cal.set(displayYear, displayMonth, 1);
            cal.add(Calendar.MONTH, -difference);

            return new SelectedDateItem(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), 1);

        }

        @Override
        public void onPageSliding(int i, float v, int i1) {
            // TO-DO
        }

        @Override
        public void onPageSlideStateChanged(int i) {
            // TO-DO
        }

        @Override
        public void onPageChosen(int position) {
            int direction = position > lastPosition ? RIGHT : LEFT;

            //refresh the previous adapter and deselect the item
            monthViewPagerAdapter.getMonthAdapterAtPosition(lastPosition % MonthViewPagerAdapter.VIEWS_IN_PAGER)
                    .setSelectedItem(null, true, false);
            if (disableAutoDateSelection) {
                monthViewPagerAdapter.refreshUserSelectedItem(userSelectedItem);
            }

            SelectedDateItem newDateItem;
            if (shouldOverrideComputedDate) {
                //set the selectedDateItem as the newDateItem
                newDateItem = selectedDateItem;
                shouldOverrideComputedDate = false;
            } else {
                //compute the new SelectedDateItem based on the difference in position
                newDateItem = computeNewSelectedDateItem(lastPosition - position);
            }


            //the month view pager adater will update here again
            monthViewPagerAdapter.refreshDateAdapters(position % MonthViewPagerAdapter.VIEWS_IN_PAGER,
                    newDateItem, resetAdapters);

            //update last position
            lastPosition = position;

            //update the currently selected date item
            FlexibleCalendarGridAdapter adapter = monthViewPagerAdapter.getMonthAdapterAtPosition(position
                    % MonthViewPagerAdapter.VIEWS_IN_PAGER);
            selectedDateItem = adapter.getSelectedItem();

            displayYear = adapter.getYear();
            displayMonth = adapter.getMonth();
            if (onMonthChangeListener != null) {
                //fire on month change event
                onMonthChangeListener.onMonthChange(displayYear, displayMonth, direction);
            }

            if (resetAdapters) {
                resetAdapters = false;
                monthViewPager.postLayout();
            }
        }
    }

    @Override
    public boolean onEstimateSize(int widthScreen, int heightScreen) {
        return false;
    }
}
