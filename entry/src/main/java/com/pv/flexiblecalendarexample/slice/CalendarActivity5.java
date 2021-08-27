/*
 * Copyright (C) 2020-21 Application Library Enigineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pv.flexiblecalendarexample.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.render.layoutboost.LayoutBoost;
import com.pv.flexiblecalendar.FlexibleCalendarView;
import com.pv.flexiblecalendar.entity.Event;
import com.pv.flexiblecalendar.view.BaseCellView;
import com.pv.flexiblecalendarexample.ResourceTable;
import java.util.ArrayList;
import java.util.List;

/**
 * CalendarActivity5 class.
 */
public class CalendarActivity5 extends AbilitySlice {
    /**
     * onStart.
     *
     * @param intent intent
     */
    @Override
    protected void onStart(final Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_calendar5);
        initView();
    }

    /**
     * IntiView.
     */
    private void initView() {
        FlexibleCalendarView view = (FlexibleCalendarView) findComponentById(ResourceTable.Id_calendar_view);
        view.setCalendarView(new FlexibleCalendarView.CalendarView() {

            @Override
            public BaseCellView getCellView(final int position, final Component convertView,
                                            final ComponentContainer parent, final int cellType) {
                BaseCellView cellView = null;

                if (convertView != null) {
                    cellView = (BaseCellView) convertView;
                }
                if (cellView == null) {
                    cellView = (BaseCellView) LayoutBoost.inflate(CalendarActivity5.this,
                            ResourceTable.Layout_calendar5_date_cell_view, null, false);
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(final int position, final Component convertView,
                                                   final ComponentContainer parent) {
                return null;
            }

            @Override
            public String getDayOfWeekDisplayValue(final int dayOfWeek, final String defaultValue) {
                return null;
            }
        });

        view.setEventDataProvider((int year, int month, int day) -> {
            if (day % 7 == 0) {
                List<EventW> eventList = new ArrayList<>();
                eventList.add(new EventW());
                return eventList;
            }
            return null;
        });
    }

    /**
     * Event.
     */
    public static class EventW implements Event {
        /**
         * get color.
         *
         * @return color
         */
        @Override
        public int getColor() {
            return 0;
        }
    }
}