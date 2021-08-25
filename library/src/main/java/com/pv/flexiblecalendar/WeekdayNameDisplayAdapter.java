package com.pv.flexiblecalendar;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.agp.utils.Color;
import ohos.app.Context;
import com.pv.flexiblecalendar.view.BaseCellView;
import com.pv.flexiblecalendar.view.ImplWeekCellViewDrawer;
import java.text.DateFormatSymbols;

/**
 * WeekdayNameDisplayAdapter class.
 *
 * @author p-v
 */
public class WeekdayNameDisplayAdapter extends BaseItemProvider {

    /**
     * cellViewDrawer.
     */
    private ImplWeekCellViewDrawer cellViewDrawer;
    /**
     * weekDayArray.
     */
    private WeekDay[] weekDayArray;
    /**
     * mContext.
     */
    private Context weekAdapterContext;
    /**
     * viewWidth for text.
     */
    private int viewWidth;

    /**
     * 2 arg-constructor.
     *
     * @param context context
     *
     * @param startDayOfTheWeek startDayOfTheWeek
     *
     * @param viewWidth viewWidth
     */
    public WeekdayNameDisplayAdapter(final Context context, final int startDayOfTheWeek, final int viewWidth) {
        super();
        weekAdapterContext = context;
        initializeWeekDays(startDayOfTheWeek);
        this.viewWidth = viewWidth;
    }

    /**
     * get count.
     *
     * @return count
     */
    @Override
    public int getCount() {
        return weekDayArray.length;
    }

    /**
     * get item.
     *
     * @return item
     */
    @Override
    public Object getItem(final int position) {
        return weekDayArray[position];
    }

    /**
     * get item id.
     *
     * @return id
     */
    @Override
    public long getItemId(final int position) {
        return position;
    }

    /**
     * get component.
     *
     * @return component
     */
    @Override
    public Component getComponent(final int position, final Component component,
                                  final ComponentContainer componentContainer) {
        BaseCellView cellView = cellViewDrawer.getCellView(position, component, componentContainer);
        if (cellView == null) {
            cellView = (BaseCellView) LayoutBoost.inflate(weekAdapterContext, ResourceTable.Layout_square_cell_layout,
                    null, false);
        }
        WeekDay weekDay = (WeekDay) getItem(position);
        //adding 1 as week days starts from 1 in Calendar
        String weekdayName = cellViewDrawer.getWeekDayName(weekDay.index, weekDay.displayValue);
        if ((weekdayName == null) || weekdayName.equals("")) {
            weekdayName = weekDay.displayValue;
        }
        cellView.setTextSize(50);
        cellView.setTextColor(Color.WHITE);
        cellView.setWidth(this.viewWidth);
        cellView.setText(weekdayName);
        return cellView;
    }

    /**
     * Weekday class.
     */
    public class WeekDay {
        /**
         * index.
         */
        private int index;
        /**
         * displayValue.
         */
        private String displayValue;
    }

    /**
     * intialize wee days.
     *
     * @param startDayOfTheWeek startDayOfTheWeek
     */
    private void initializeWeekDays(final int startDayOfTheWeek) {
        DateFormatSymbols symbols = new DateFormatSymbols(FlexibleCalendarHelper.getLocale(weekAdapterContext));
        String[] weekDayList = symbols.getShortWeekdays(); // weekday list has 8 elements
        weekDayArray = new WeekDay[7];
        //reordering array based on the start day of the week
        for (int i = 1; i < weekDayList.length; i++) {
            WeekDay weekDay = new WeekDay();
            weekDay.index = i;
            weekDay.displayValue = weekDayList[i];
            int tempVal = i - startDayOfTheWeek;
            weekDayArray[tempVal < 0 ? 7 + tempVal : tempVal] = weekDay;
        }
    }

    /**
     * set cell view.
     *
     * @param cellView cellView
     */
    public void setCellView(final ImplWeekCellViewDrawer cellView) {
        this.cellViewDrawer = cellView;
    }

    /**
     * get cell view.
     *
     * @return cell view
     */
    public ImplWeekCellViewDrawer getCellViewDrawer() {
        return cellViewDrawer;
    }


    /**
     * set start day of the week.
     *
     * @param startDayOfTheWeek startDayOfTheWeek
     */
    public void setStartDayOfTheWeek(final int startDayOfTheWeek) {
        initializeWeekDays(startDayOfTheWeek);
        this.notifyDataChanged();
    }
}
