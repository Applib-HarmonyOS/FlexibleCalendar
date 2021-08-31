package com.pv.flexiblecalendar;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.utils.Color;
import ohos.app.Context;
import com.pv.flexiblecalendar.entity.Event;
import com.pv.flexiblecalendar.entity.SelectedDateItem;
import com.pv.flexiblecalendar.view.BaseCellView;
import com.pv.flexiblecalendar.view.ImplDateCellViewDrawer;
import com.pv.flexiblecalendar.view.MonthDisplayUtil;
import java.util.Calendar;
import java.util.List;


/**
 * FlexiCalendarGridAdapter.
 *
 * @author p-v
 */
class FlexibleCalendarGridAdapter extends BaseItemProvider {

    /**
     * SIX_WEEK_DAY_COUNT.
     */
    private static final int SIX_WEEK_DAY_COUNT = 42;
    /**
     * year.
     */
    private int year;
    /**
     * month.
     */
    private int month;
    /**
     * context.
     */
    private Context context;
    /**
     * month disply util.
     */
    private MonthDisplayUtil monthDisplayUtil;
    /**
     * calendar.
     */
    private Calendar calendar;
    /**
     * on date cell item click listener.
     */
    private OnDateCellItemClickListener onDateCellItemClickListener;
    /**
     * selected item.
     */
    private SelectedDateItem selectedItem;
    /**
     * user selected date item.
     */
    private SelectedDateItem userSelectedDateItem;
    /**
     * month event fetcher.
     */
    private MonthEventFetcher monthEventFetcher;
    /**
     * cell view drawer.
     */
    private ImplDateCellViewDrawer cellViewDrawer;
    /**
     * show dates outside month.
     */
    private boolean showDatesOutsideMonth;
    /**
     * decorate dates outside month.
     */
    private boolean decorateDatesOutsideMonth;
    /**
     * disable auto date selection.
     */
    private boolean disableAutoDateSelection;

    /**
     * flexible calendar grid adapter.
     *
     * @param context context
     *
     * @param year year
     *
     * @param month month
     *
     * @param showDatesOutsideMonth showdatesoutsidemonth
     *
     * @param decorateDatesOutsideMonth decorateDatesOutsideMonth
     *
     * @param startDayOfTheWeek startdayoftheweek
     *
     * @param disableAutoDateSelection disableautodateselection
     */
    public FlexibleCalendarGridAdapter(Context context, int year, int month,
                                       boolean showDatesOutsideMonth, boolean decorateDatesOutsideMonth,
                                       int startDayOfTheWeek, boolean disableAutoDateSelection) {
        this.context = context;
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        this.disableAutoDateSelection = disableAutoDateSelection;
        initialize(year, month, startDayOfTheWeek);
    }

    /**
     * initialize.
     *
     * @param year year
     *
     * @param month month
     *
     * @param startDayOfTheWeek stratdayoftheweek
     */
    public void initialize(int year, int month, int startDayOfTheWeek) {
        this.year = year;
        this.month = month;
        this.monthDisplayUtil = new MonthDisplayUtil(year, month, startDayOfTheWeek);
        this.calendar = FlexibleCalendarHelper.getLocalizedCalendar(context);
    }

    /**
     * get count.
     *
     * @return int val
     */
    @Override
    public int getCount() {
        int weekStartDay = monthDisplayUtil.getWeekStartDay();
        int firstDayOfWeek = monthDisplayUtil.getFirstDayOfMonth();
        int diff = firstDayOfWeek - weekStartDay;
        int toAdd = diff < 0 ? (diff + 7) : diff;
        return showDatesOutsideMonth ? SIX_WEEK_DAY_COUNT
                : monthDisplayUtil.getNumberOfDaysInMonth()
                + toAdd;
    }

    /**
     * get item.
     *
     * @param position position
     *
     * @return object
     */
    @Override
    public Object getItem(int position) {
        int row = position / 7;
        int col = position % 7;
        return monthDisplayUtil.getDayAt(row, col);
    }

    /**
     * get item id.
     *
     * @param position position
     *
     * @return position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * get component.
     *
     * @param position position
     *
     * @param convertView convertview
     *
     * @param parent parent
     *
     * @return basecellview
     */
    @Override
    public BaseCellView getComponent(int position, Component convertView, ComponentContainer parent) {
        int row = position / 7;
        int col = position % 7;
        //checking if is within current month
        boolean isWithinCurrentMonth = monthDisplayUtil.isWithinCurrentMonth(row, col);
        //compute cell type
        int cellType = BaseCellView.OUTSIDE_MONTH;
        //day at the current row and col
        int day = monthDisplayUtil.getDayAt(row, col);
        int localMonth = monthDisplayUtil.getMonth();

        if (isWithinCurrentMonth) {
            //set to REGULAR if is within current month
            cellType = BaseCellView.REGULAR;
            checkOne(localMonth, day, cellType);
            checkTwo(localMonth, day, cellType);
        }

        BaseCellView cellView = cellViewDrawer.getCellView(position, convertView, parent, cellType);
        if (cellView == null) {
            cellView = (BaseCellView) convertView;
            if (cellView == null) {
                LayoutScatter inflate = LayoutScatter.getInstance(context);
                cellView = (BaseCellView) inflate.parse(ResourceTable.Layout_square_cell_layout, parent, false);
            }
        }
        drawDateCell(cellView, day, cellType);
        return cellView;
    }

    private int checkTwo(int localMonth, int day, int cellType) {
        if (calendar.get(Calendar.YEAR) == year
                && calendar.get(Calendar.MONTH) == localMonth
                && calendar.get(Calendar.DAY_OF_MONTH) == day) {
            if (cellType == BaseCellView.SELECTED) {
                //today and selected
                cellType = BaseCellView.SELECTED_TODAY;
            } else {
                //today
                cellType = BaseCellView.TODAY;
            }
        }
        return cellType;
    }

    private int checkOne(int localMonth, int day, int cellType) {
        if (disableAutoDateSelection) {
            if (userSelectedDateItem != null && userSelectedDateItem.getYear() == year
                    && userSelectedDateItem.getMonth() == localMonth
                    && userSelectedDateItem.getDay() == day) {
                //selected
                cellType = BaseCellView.SELECTED;
            }
        } else {
            if (selectedItem != null && selectedItem.getYear() == year
                    && selectedItem.getMonth() == localMonth
                    && selectedItem.getDay() == day) {
                //selected
                cellType = BaseCellView.SELECTED;
            }
        }
        return cellType;
    }

    /**
     * draw date cell.
     *
     * @param cellView cellview
     *
     * @param day day
     *
     * @param cellType celltype
     */
    private void drawDateCell(BaseCellView cellView, int day, int cellType) {
        cellView.clearAllStates();
        if (cellType != BaseCellView.OUTSIDE_MONTH) {
            cellView.setText(String.valueOf(day));
            cellView.setTextSize(50);
            cellView.setTextColor(Color.WHITE);
            cellView.setWidth(200);
            cellView.setClickedListener(new DateClickListener(day, month, year));

            // add events
            if (monthEventFetcher != null) {
                cellView.setEvents(monthEventFetcher.getEventsForTheDay(year, month, day));
            }
            switch (cellType) {
                case BaseCellView.SELECTED_TODAY:
                    cellView.addState(BaseCellView.STATE_TODAY);
                    cellView.addState(BaseCellView.STATE_SELECTED);
                    break;
                case BaseCellView.TODAY:
                    cellView.addState(BaseCellView.STATE_TODAY);
                    break;
                case BaseCellView.SELECTED:
                    cellView.addState(BaseCellView.STATE_SELECTED);
                    break;
                default:
                    cellView.addState(BaseCellView.STATE_REGULAR);
            }
        } else {
            checkThree(cellView, day);
        }
    }

    private void checkThree(BaseCellView cellView, int day) {
        if (showDatesOutsideMonth) {
            cellView.setText(String.valueOf(day));
            cellView.setTextSize(50);
            cellView.setTextColor(Color.WHITE);
            cellView.setWidth(200);
            int[] temp = new int[2];
            //date outside month and less than equal to 12 means it belongs to next month otherwise previous
            if (day <= 12) {
                FlexibleCalendarHelper.nextMonth(year, month, temp);
            } else {
                FlexibleCalendarHelper.previousMonth(year, month, temp);

            }
            cellView.setClickedListener(new DateClickListener(day, temp[1], temp[0]));

            if (decorateDatesOutsideMonth && monthEventFetcher != null) {
                cellView.setEvents(monthEventFetcher.getEventsForTheDay(temp[0], temp[1], day));
            }

            cellView.addState(BaseCellView.STATE_OUTSIDE_MONTH);
        } else {
            cellView.setText(null);
            cellView.setTextSize(50);
            cellView.setTextColor(Color.WHITE);
            cellView.setWidth(200);
            cellView.setClickedListener(null);
        }
    }

    /**
     * get year.
     *
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * get month.
     *
     * @return month
     */
    public int getMonth() {
        return month;
    }

    /**
     * set date click listener.
     *
     * @param onDateCellItemClickListener ondatecellitemcicklistener
     */
    public void setOnDateClickListener(OnDateCellItemClickListener onDateCellItemClickListener) {
        this.onDateCellItemClickListener = onDateCellItemClickListener;
    }

    /**
     * set selected item.
     *
     * @param selectedItem selecteddateitem
     *
     * @param notify notify
     *
     * @param isUserSelected isuserselected
     */
    public void setSelectedItem(SelectedDateItem selectedItem, boolean notify, boolean isUserSelected) {
        this.selectedItem = selectedItem;
        if (disableAutoDateSelection && isUserSelected) {
            this.userSelectedDateItem = selectedItem;
        }
        if (notify) {
            notifyDataChanged();
        }
    }

    /**
     * get selected item.
     *
     * @return selected date item
     */
    public SelectedDateItem getSelectedItem() {
        return selectedItem;
    }

    /**
     * set month event fetcher.
     *
     * @param monthEventFetcher montheventfetcher
     */
    void setMonthEventFetcher(MonthEventFetcher monthEventFetcher) {
        this.monthEventFetcher = monthEventFetcher;
    }

    /**
     * set cell view drawer.
     *
     * @param cellViewDrawer cellviewdrawer
     */
    public void setCellViewDrawer(ImplDateCellViewDrawer cellViewDrawer) {
        this.cellViewDrawer = cellViewDrawer;
    }

    /**
     * set show date outside month.
     *
     * @param showDatesOutsideMonth showdatesoutsidemonth
     */
    public void setShowDatesOutsideMonth(boolean showDatesOutsideMonth) {
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        this.notifyDataChanged();
    }

    /**
     * set decorate dates outside month.
     *
     * @param decorateDatesOutsideMonth decoratedatesoutsidemont
     */
    public void setDecorateDatesOutsideMonth(boolean decorateDatesOutsideMonth) {
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        this.notifyDataChanged();
    }

    /**
     * set disable auto date selection.
     *
     * @param disableAutoDateSelection disableAutoDateSelection
     */
    public void setDisableAutoDateSelection(boolean disableAutoDateSelection) {
        this.disableAutoDateSelection = disableAutoDateSelection;
        this.notifyDataChanged();
    }

    /**
     * set first day Of the week.
     *
     * @param firstDayOfTheWeek firstDayOfTheWeek
     */
    public void setFirstDayOfTheWeek(int firstDayOfTheWeek) {
        monthDisplayUtil = new MonthDisplayUtil(year, month, firstDayOfTheWeek);
        this.notifyDataChanged();
    }

    /**
     * get user selected date item.
     *
     * @return user selected date item
     */
    public SelectedDateItem getUserSelectedItem() {
        return userSelectedDateItem;
    }

    /**
     * set user selected date item.
     *
     * @param selectedItem selecteditem
     */
    public void setUserSelectedDateItem(SelectedDateItem selectedItem) {
        this.userSelectedDateItem = selectedItem;
        notifyDataChanged();
    }

    /**
     * OnDateCellItemClickListener.
     */
    public interface OnDateCellItemClickListener {
        void onDateClick(SelectedDateItem selectedItem);
    }

    /**
     * MonthEventFetcher.
     */
    interface MonthEventFetcher {
        List<? extends Event> getEventsForTheDay(int year, int month, int day);
    }

    /**
     * DateClickListener.
     */
    private class DateClickListener implements Component.ClickedListener {

        /**
         * iday.
         */
        private int dateClassDay;
        /**
         * imonth.
         */
        private int dateClassMonth;
        /**
         * iyear.
         */
        private int dateClassYear;

        /**
         * argument constructor.
         *
         * @param day day
         *
         * @param month month
         *
         * @param year year
         */
        public DateClickListener(int day, int month, int year) {
            this.dateClassDay = day;
            this.dateClassMonth = month;
            this.dateClassYear = year;
        }

        /**
         * onclick.
         *
         * @param arg1 arg1
         */
        public void onClick(final Component arg1) {
            selectedItem = new SelectedDateItem(dateClassYear, dateClassMonth, dateClassDay);
            SelectedDateItem.newInstance(selectedItem);
            if (disableAutoDateSelection) {
                userSelectedDateItem = selectedItem;
            }
            notifyDataChanged();
            if (onDateCellItemClickListener != null) {
                onDateCellItemClickListener.onDateClick(selectedItem);
            }

        }
    }
}
