package com.pv.flexiblecalendar;

import ohos.agp.components.Component;
import ohos.app.Context;
import com.antonyt.infiniteviewpager.InfiniteViewPager;

/**
 * MonthViewPager class.
 *
 * @author p-v
 */
class MonthViewPager extends InfiniteViewPager implements Component.EstimateSizeListener {

    /**
     * paramater constructor.
     *
     * @param context context
     */
    public MonthViewPager(Context context) {
        super(context);
    }

    /**
     * on estimate size.
     */
    @Override
    public boolean onEstimateSize(int widthScreen, int heightScreen) {
        return false;
    }
}
