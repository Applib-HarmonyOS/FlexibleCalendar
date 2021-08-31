package com.antonyt.infiniteviewpager;

import ohos.agp.components.AttrSet;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderProvider;
import ohos.app.Context;

/**
 * A {@link PageSlider} that allows pseudo-infinite paging with a wrap-around effect. Should be used with an {@link
 * InfinitePagerAdapter}.
 */
public class InfiniteViewPager extends PageSlider {

    /**
     * constructor.
     *
     * @param context context
     */
    public InfiniteViewPager(final Context context) {
        super(context);
    }

    /**
     * 2-arg constructor.
     *
     * @param context context
     *
     * @param attrs attrs
     */
    public InfiniteViewPager(final Context context, final AttrSet attrs) {
        super(context, attrs);
    }

    /**
     * set provider.
     *
     * @param provider provider
     */
    @Override
    public void setProvider(final PageSliderProvider provider) {
        super.setProvider(provider);
        setCurrentPage(0);
    }

    /**
     * set current page.
     *
     * @param itemPos itempos
     */
    @Override
    public void setCurrentPage(final int itemPos) {
        setCurrentPage(itemPos, false);
    }

    /**
     * set current page.
     *
     * @param itemPos itempos
     *
     * @param smoothScroll smoothscroll
     */
    @Override
    public void setCurrentPage(final int itemPos, final boolean smoothScroll) {
        if (getProvider().getCount() == 0) {
            super.getCurrentPage();
            return;
        }
        int itemPosition = getOffsetAmount() + (itemPos % getProvider().getCount());
        super.setCurrentPage(itemPosition, smoothScroll);
    }

    /**
     * get current page.
     *
     * @return current page
     */
    @Override
    public int getCurrentPage() {
        if (getProvider().getCount() == 0) {
            return super.getCurrentPage();
        }
        int position = super.getCurrentPage();
        if (getProvider() instanceof InfinitePagerAdapter) {
            InfinitePagerAdapter infAdapter = (InfinitePagerAdapter) getProvider();
            // Return the actual item position in the data backing InfinitePagerAdapter
            return (position % infAdapter.getRealCount());
        } else {
            return super.getCurrentPage();
        }
    }

    /**
     * To get the offSetAmount.
     *
     * @return offset
     */
    public int getOffsetAmount() {
        if (getProvider().getCount() == 0) {
            return 0;
        }
        if (getProvider() instanceof InfinitePagerAdapter) {
            InfinitePagerAdapter infAdapter = (InfinitePagerAdapter) getProvider();
            // allow for 100 back cycles from the beginning
            // should be enough to create an illusion of infinity
            // warning: scrolling to very high values (1,000,000+) results in
            // strange drawing behaviour
            return infAdapter.getRealCount() * 100;
        } else {
            return 0;
        }
    }
}