package com.pv.flexiblecalendarexample.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.app.Context;
import com.pv.flexiblecalendarexample.ResourceTable;
import java.util.ArrayList;
import java.util.List;

/**
 * CalendarListActivity.
 */
public class CalendarListActivity extends AbilitySlice {
    /**
     * Calendar List.
     */
    private List<String> calendarList;

    /**
     * onStart.
     *
     * @param intent intent
     */
    @Override
    protected void onStart(final Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_calendar_list);
        initView();
    }

    /**
     * init view.
     */
    private void initView() {
        calendarList = new ArrayList<>();
        calendarList.add("Calendar 1");
        calendarList.add("Calendar 2");
        calendarList.add("Calendar 3");
        calendarList.add("Calendar 4");
        calendarList.add("Calendar 5");
        ListContainer listContainer = (ListContainer) findComponentById(ResourceTable.Id_calendar_list_view);
        final CustomAdapter customAdapter = new CustomAdapter(this, calendarList);
        listContainer.setItemProvider(customAdapter);
    }

    /**
     * CustomAdapter.
     */
    public class CustomAdapter extends BaseItemProvider {
        /**
         * adapter context.
         */
        private Context adapterContext;
        /**
         * calendar list adapter.
         */
        private List<String> calendarListAdapter;

        /**
         * arg constructor.
         *
         * @param context context
         *
         * @param data    data
         */
        public CustomAdapter(final Context context, final List<String> data) {
            this.adapterContext = context;
            this.calendarListAdapter = data;
        }

        /**
         * get count.
         * retutn count
         */
        @Override
        public int getCount() {
            return calendarList.size();
        }

        /**
         * get item.
         *
         * @param pos index
         * @return cal val
         */
        @Override
        public String getItem(final int pos) {
            return calendarList.get(pos);
        }

        /**
         * get item id.
         *
         * @param getItemId get item id
         * @return long val
         */
        @Override
        public long getItemId(final int getItemId) {
            return Long.parseLong(calendarList.get(getItemId));
        }

        /**
         * get component.
         *
         * @param pos                pos
         * @param component          component
         * @param componentContainer componentcontainer
         * @return component
         */
        @Override
        public Component getComponent(final int pos, final Component component
                                      , final ComponentContainer componentContainer) {
            final Component calList = LayoutBoost.inflate(this.adapterContext, ResourceTable
                                                 .Layout_mylist, componentContainer, false);
            final Text text = (Text) calList.findComponentById(ResourceTable.Id_text_view);
            text.setText(calendarListAdapter.get(pos));
            text.setId(pos);
            text.setClickedListener(component1 -> {
                int id = component1.getId();
                checkIntent(id);
            });
            return calList;
        }

        /**
         * check navigation to calendar.
         *
         * @param id id
         */
        private void checkIntent(final int id) {
            switch (id) {
                case 0:
                    present(new CalendarActivity(), new Intent());
                    break;
                case 1:
                    present(new CalendarActivity2(), new Intent());
                    break;
                case 2:
                    present(new CalendarActivity3(), new Intent());
                    break;
                case 3:
                    present(new CalendarActivity4(), new Intent());
                    break;
                case 4:
                    present(new CalendarActivity5(), new Intent());
                    break;
                default:
                    break;
            }
        }
    }
}


