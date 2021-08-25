package com.pv.flexiblecalendarexample.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import com.pv.flexiblecalendar.FlexibleCalendarView;
import com.pv.flexiblecalendar.view.BaseCellView;
import com.pv.flexiblecalendarexample.ResourceTable;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * CalendarActivity.
 */
public class CalendarActivity4 extends AbilitySlice {

    /**
     * onStart.
     *
     * @param intent intent
     */
    @Override
    protected void onStart(final Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_calendary_activity4);
        initView();
    }

    /**
     * initView.
     */
    private void initView() {
        final FlexibleCalendarView calendarView = (FlexibleCalendarView) findComponentById(
                                                   ResourceTable.Id_calendar_view);
        calendarView.setStartDayOfTheWeek(Calendar.MONDAY);
        Image leftArrow = (Image) findComponentById(ResourceTable.Id_left_arrow);
        Text monthTextView = (Text) findComponentById(ResourceTable.Id_month_text_view);
        Calendar cal = Calendar.getInstance();
        cal.set(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth(), 1);
        monthTextView.setText(cal.getDisplayName(Calendar.MONTH,
                Calendar.LONG, Locale.ENGLISH) + " " + calendarView.getSelectedDateItem().getYear());
        leftArrow.setClickedListener(component -> {
            calendarView.moveToPreviousMonth();
        });
        Image rightArrow = (Image) findComponentById(ResourceTable.Id_right_arrow);
        rightArrow.setClickedListener(component -> {
            calendarView.moveToNextMonth();
        });
        calendarView.setOnMonthChangeListener((int year, int month, @FlexibleCalendarView.Direction int direction) -> {
            Calendar calen = Calendar.getInstance();
            calen.set(year, month, 1);
            monthTextView.setText(calen.getDisplayName(Calendar.MONTH,
                    Calendar.LONG, Locale.ENGLISH) + " " + year);
        });
        calendarView.setShowDatesOutsideMonth(true);
        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(final int position, final Component convertView
                                            , final ComponentContainer parent, final int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity4.this);
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
                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity4.this);
                    cellView = (BaseCellView) inflater.parse(ResourceTable.Layout_calendar3_week_cell_view, parent
                                , false);
                    try {
                        ShapeElement shapeElement = new ShapeElement();
                        RgbColor rgbColor = new RgbColor(getResourceManager()
                                            .getElement(ResourceTable.Color_holo_purple).getColor());
                        shapeElement.setRgbColor(rgbColor);
                        cellView.setBackground(shapeElement);
                        cellView.setTextColor(new Color(getResourceManager()
                                            .getElement(ResourceTable.Color_holo_orange_light).getColor()));
                    } catch (IOException | NotExistException | WrongTypeException e) {
                        e.printStackTrace();
                    }
                    cellView.setTextSize(18);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(final int dayOfWeek, final String defaultValue) {
                return null;
            }
        });
        Button resetButton = (Button) findComponentById(ResourceTable.Id_reset_button);
        resetButton.setClickedListener(component -> {
            calendarView.goToCurrentMonth();

        });
    }
}