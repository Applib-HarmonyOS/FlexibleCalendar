package com.pv.flexiblecalendar;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ListContainer;
import ohos.agp.components.PageSliderProvider;
import ohos.agp.components.TableLayoutManager;
import ohos.agp.database.DataSetSubscriber;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.app.Context;
import com.pv.flexiblecalendar.entity.SelectedDateItem;
import com.pv.flexiblecalendar.view.ImplCellViewDrawer;
import com.pv.flexiblecalendar.view.ImplDateCellViewDrawer;
import java.util.ArrayList;
import java.util.List;

/**
 * MonthViewPagerAdapter class.
 *
 * @author p-v
 */
public class MonthViewPagerAdapter extends PageSliderProvider {

    /**
     * views in pager.
     */
    public static final int VIEWS_IN_PAGER = 4;
    /**
     * tag.
     */
    static final String GRID_TAG_PREFIX = "111";
    /**
     * context.
     */
    private Context context;
    /**
     * date adapter.
     */
    private List<FlexibleCalendarGridAdapter> dateAdapters;
    /**
     * date cell item click listener.
     */
    private FlexibleCalendarGridAdapter.OnDateCellItemClickListener onDateCellItemClickListener;
    /**
     * month event fetcher.
     */
    private FlexibleCalendarGridAdapter.MonthEventFetcher monthEventFetcher;
    /**
     * cell View Drawer.
     */
    private ImplDateCellViewDrawer cellViewDrawer;
    /**
     * show dates outside month.
     */
    private boolean showDatesOutsideMonth;
    /**
     * refresh month view adapter.
     */
    private boolean refreshMonthViewAdpater;
    /**
     * start day of the week.
     */
    private int startDayOfTheWeek;
    /**
     * decorate dates outside month.
     */
    private boolean decorateDatesOutsideMonth;
    /**
     * disable auto date selection.
     */
    private boolean disableAutoDateSelection;

    /**
     * arg-constructor.
     *
     * @param context context
     *
     * @param year year
     *
     * @param month month
     *
     * @param onDateCellItemClickListener onDateCellItemClickListener
     *
     * @param showDatesOutsideMonth showDatesOutsideMonth
     *
     * @param decorateDatesOutsideMonth decorateDatesOutsideMonth
     *
     * @param startDayOfTheWeek startDayOfTheWeek
     *
     * @param disableAutoDateSelection disableAutoDateSelection
     */
    public MonthViewPagerAdapter(Context context, int year, int month,
                                 FlexibleCalendarGridAdapter.OnDateCellItemClickListener onDateCellItemClickListener,
                                 boolean showDatesOutsideMonth, boolean decorateDatesOutsideMonth,
                                 int startDayOfTheWeek, boolean disableAutoDateSelection) {
        this.context = context;
        this.dateAdapters = new ArrayList<>(VIEWS_IN_PAGER);
        this.onDateCellItemClickListener = onDateCellItemClickListener;
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        this.startDayOfTheWeek = startDayOfTheWeek;
        this.disableAutoDateSelection = disableAutoDateSelection;
        initializeDateAdapters(year, month);
    }

    /**
     * initialize date adapters.
     *
     * @param year year
     *
     * @param month month
     */
    private void initializeDateAdapters(int year, int month) {
        int primaryYear;
        int primaryMonth;
        if (month == 0) {
            primaryYear = year - 1;
            primaryMonth = 11;
        } else {
            primaryYear = year;
            primaryMonth = month - 1;
        }
        for (int i = 0; i < VIEWS_IN_PAGER - 1; i++) {
            dateAdapters.add(new FlexibleCalendarGridAdapter(context, year, month, showDatesOutsideMonth,
                    decorateDatesOutsideMonth, startDayOfTheWeek, disableAutoDateSelection));
            if (month == 11) {
                year++;
                month = 0;
            } else {
                month++;
            }
        }
        dateAdapters.add(new FlexibleCalendarGridAdapter(context, primaryYear, primaryMonth, showDatesOutsideMonth,
                decorateDatesOutsideMonth, startDayOfTheWeek, disableAutoDateSelection));
    }

    /**
     * destroy container.
     *
     * @param componentContainer componentContainer
     *
     * @param pos pos
     *
     * @param obj obj
     */
    @Override
    public void destroyPageFromContainer(final ComponentContainer componentContainer, final int pos, final Object obj) {
        componentContainer.removeAllComponents();
    }

    /**
     * page matcher.
     *
     * @param component component
     *
     * @param obj obj
     */
    @Override
    public boolean isPageMatchToObject(final Component component, final Object obj) {
        return component == obj;
    }

    /**
     * refresh date adapter.
     *
     * @param position position
     *
     * @param selectedDateItem selectedDateItem
     *
     * @param refreshAll refreshAll
     */
    public void refreshDateAdapters(int position, SelectedDateItem selectedDateItem, boolean refreshAll) {
        FlexibleCalendarGridAdapter currentAdapter = dateAdapters.get(position);
        if (refreshAll) {
            //refresh all used when go to current month is called to refresh all the adapters
            currentAdapter.initialize(selectedDateItem.getYear(), selectedDateItem.getMonth(), startDayOfTheWeek);
        }
        //selecting the first date of the month
        currentAdapter.setSelectedItem(selectedDateItem, true, false);

        int[] nextDate = new int[2];
        FlexibleCalendarHelper.nextMonth(currentAdapter.getYear(), currentAdapter.getMonth(), nextDate);

        dateAdapters.get((position + 1) % VIEWS_IN_PAGER).initialize(nextDate[0], nextDate[1], startDayOfTheWeek);

        FlexibleCalendarHelper.nextMonth(nextDate[0], nextDate[1], nextDate);
        dateAdapters.get((position + 2) % VIEWS_IN_PAGER).initialize(nextDate[0], nextDate[1], startDayOfTheWeek);

        FlexibleCalendarHelper.previousMonth(currentAdapter.getYear(), currentAdapter.getMonth(), nextDate);
        dateAdapters.get((position + 3) % VIEWS_IN_PAGER).initialize(nextDate[0], nextDate[1], startDayOfTheWeek);

    }

    /**
     * get month adapter.
     *
     * @param position position
     */
    public FlexibleCalendarGridAdapter getMonthAdapterAtPosition(int position) {
        FlexibleCalendarGridAdapter gridAdapter = null;
        if (dateAdapters != null && position >= 0 && position < dateAdapters.size()) {
            gridAdapter = dateAdapters.get(position);
        }
        return gridAdapter;
    }

    /**
     * get count.
     *
     * @return count
     */
    @Override
    public int getCount() {
        return VIEWS_IN_PAGER;
    }

    /**
     * set selected item.
     *
     * @param selectedItem selected item
     */
    public void setSelectedItem(SelectedDateItem selectedItem) {
        for (FlexibleCalendarGridAdapter f : dateAdapters) {
            f.setSelectedItem(selectedItem, true, false);
        }
        this.notifyDataChanged();
    }

    /**
     * set set month event fetcher.
     *
     * @param monthEventFetcher monthEventFetcher
     */
    public void setMonthEventFetcher(FlexibleCalendarGridAdapter.MonthEventFetcher monthEventFetcher) {
        this.monthEventFetcher = monthEventFetcher;
    }

    /**
     * get cell view drawer.
     *
     * @return cellViewDrawer
     */
    public ImplCellViewDrawer getCellViewDrawer() {
        return cellViewDrawer;
    }

    /**
     * set cell view drawer.
     *
     * @param cellViewDrawer cellViewDrawer
     */
    public void setCellViewDrawer(ImplDateCellViewDrawer cellViewDrawer) {
        this.cellViewDrawer = cellViewDrawer;
    }

    /**
     * set spacing.
     *
     * @param horizontalSpacing horizntalSpacing
     *
     * @param verticalSpacing verticalSpacing
     */
    public void setSpacing(int horizontalSpacing, int verticalSpacing) {
        // SET SPACING
    }

    /**
     * set show dates dutside month.
     *
     * @param showDatesOutsideMonth showDatesOutsideMonth
     */
    public void setShowDatesOutsideMonth(boolean showDatesOutsideMonth) {
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
            adapter.setShowDatesOutsideMonth(showDatesOutsideMonth);
        }
    }

    /**
     * set decorate dates outside month.
     *
     * @param decorateDatesOutsideMonth decorateDatesOutsideMonth
     */
    public void setDecorateDatesOutsideMonth(boolean decorateDatesOutsideMonth) {
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
            adapter.setDecorateDatesOutsideMonth(decorateDatesOutsideMonth);
        }
    }

    /**
     * set disable auto date selection.
     *
     * @param disableAutoDateSelection disableAutoDateSelection
     */
    public void setDisableAutoDateSelection(boolean disableAutoDateSelection) {
        this.disableAutoDateSelection = disableAutoDateSelection;
        for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
            adapter.setDisableAutoDateSelection(disableAutoDateSelection);
        }
    }

    /**
     * get index page.
     *
     * @param object object
     */
    @Override
    public int getPageIndex(Object object) {
        if (refreshMonthViewAdpater) {
            return POSITION_INVALID;
        }
        return POSITION_REMAIN;
    }

    /**
     * set start day of the week.
     *
     * @param startDayOfTheWeek startDayOfTheWeek
     */
    public void setStartDayOfTheWeek(int startDayOfTheWeek) {
        this.startDayOfTheWeek = startDayOfTheWeek;
        for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
            adapter.setFirstDayOfTheWeek(startDayOfTheWeek);
        }
    }

    /**
     * refresh user selected item.
     *
     * @param selectedDateItem selectedDateItem
     */
    public void refreshUserSelectedItem(SelectedDateItem selectedDateItem) {
        for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
            if (adapter.getUserSelectedItem() != null
                    && !selectedDateItem.equals(adapter.getUserSelectedItem())) {
                adapter.setUserSelectedDateItem(selectedDateItem);
            }
        }

    }

    /**
     * MonthViewPagerDataSetObserver.
     */
    protected class MonthViewPagerDataSetObserver extends DataSetSubscriber {

        /**
         * onchange API.
         */
        @Override
        public void onChanged() {
            for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
                adapter.notifyDataChanged();
            }
        }

        /**
         * onInvalidated API.
         */
        @Override
        public void onInvalidated() {
            for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
                adapter.notifyDataInvalidated();
            }
        }
    }

    /**
     * create page container.
     *
     * @param componentContainer componentContainer
     *
     * @param pos pos
     *
     * @return componentContainer
     */
    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int pos) {
        Component component = LayoutBoost.inflate(context, ResourceTable.Layout_month_grid_layout, null, false);
        ListContainer gridList = (ListContainer) component.findComponentById(ResourceTable.Id_grid_list);

        TableLayoutManager tableLayoutManager = new TableLayoutManager();
        tableLayoutManager.setColumnCount(7);
        gridList.setLayoutManager(tableLayoutManager);

        FlexibleCalendarGridAdapter adapter = dateAdapters.get(pos);
        adapter.setOnDateClickListener(onDateCellItemClickListener);
        adapter.setMonthEventFetcher(monthEventFetcher);
        adapter.setCellViewDrawer(cellViewDrawer);

        gridList.setItemProvider(adapter);
        gridList.setTag(GRID_TAG_PREFIX + pos);
        componentContainer.addComponent(component);

        return componentContainer;
    }
}
