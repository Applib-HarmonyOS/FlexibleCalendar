package com.antonyt.infiniteviewpager;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.PageSliderProvider;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * A PagerAdapter that wraps around another PagerAdapter to handle paging wrap-around.
 */
public class InfinitePagerAdapter extends PageSliderProvider {

    /**
     * TAG.
     */
    private static final String TAG = "InfinitePagerAdapter";
    /**
     * TYPE.
     */
    private static final int HILOG_TYPE = 3;
    /**
     * DOMAIN.
     */
    private static final int HILOG_DOMAIN = 0xD000F00;
    /**
     * LABEL.
     */
    private static final HiLogLabel LABEL = new HiLogLabel(HILOG_TYPE, HILOG_DOMAIN, TAG);
    /**
     * DEBUG.
     */
    private static final boolean DEBUG = true;
    /**
     * MAX_VAL.
     */
    private static final int MAX_VAL = 10000;
    /**
     * adapter.
     */
    private PageSliderProvider adapter;
    /**
     * fake count.
     */
    private int fakeCount;

    /**
     * To specify the adapter and context.
     *
     * @param adapter adapter
     *
     * @param context context
     */
    public InfinitePagerAdapter(final PageSliderProvider adapter, final Context context) {
        this.adapter = adapter;
        this.fakeCount = -1;
    }

    /**
     * page in container.
     *
     * @param componentContainer container
     *
     * @param position position
     *
     * @return object
     */
    @Override
    public Object createPageInContainer(final ComponentContainer componentContainer, final int position) {
        int virtualPosition = position % getRealCount();
        debug("instantiateItem: real position: " + position);
        debug("instantiateItem: virtual position: " + virtualPosition);

        // only expose virtual position to the inner adapter
        return adapter.createPageInContainer(componentContainer, virtualPosition);
    }

    /**
     * destroy container.
     *
     * @param compCont container
     *
     * @param pos pos
     *
     * @param obj obj
     */
    @Override
    public void destroyPageFromContainer(final ComponentContainer compCont, final int pos, final Object obj) {
        int virtualPosition = pos % getRealCount();
        debug("destroyItem: real pos: " + pos);
        debug("destroyItem: virtual pos: " + virtualPosition);

        // only expose virtual pos to the inner adapter
        adapter.destroyPageFromContainer(compCont, virtualPosition, obj);
    }

    /**
     * page in container.
     *
     * @param component component
     *
     * @param object object
     *
     * @return bool val
     */
    @Override
    public boolean isPageMatchToObject(final Component component, final Object object) {
        return adapter.isPageMatchToObject(component, object);
    }

    /**
     * get count.
     *
     * @return int val
     */
    @Override
    public int getCount() {
        if (getRealCount() == 0) {
            return 0;
        }
        if (fakeCount != -1) {
            return fakeCount;
        }
        return MAX_VAL;
    }

    /**
     * To get the realcount.
     *
     * @return the {@link #getCount()} result of the wrapped adapter.
     */
    public int getRealCount() {
        return adapter.getCount();
    }

    /**
     * on update finish.
     *
     * @param componentContainer container
     */
    @Override
    public void onUpdateFinished(final ComponentContainer componentContainer) {
        adapter.onUpdateFinished(componentContainer);
    }

    /**
     * start update.
     *
     * @param container container
     */
    @Override
    public void startUpdate(final ComponentContainer container) {
        adapter.startUpdate(container);
    }

    /**
     * get page title.
     *
     * @param position position
     *
     * @return page title
     */
    @Override
    public String getPageTitle(final int position) {
        int virtualPosition = position % getRealCount();
        return adapter.getPageTitle(virtualPosition);
    }

    /**
     * get page index.
     *
     * @param object object
     *
     * @return page index
     */
    @Override
    public int getPageIndex(final Object object) {
        return adapter.getPageIndex(object);
    }

    /**
     * notify data changed.
     */
    @Override
    public void notifyDataChanged() {
        adapter.notifyDataChanged();
        super.notifyDataChanged();
    }

    /*
     * End delegation.
     *
     * @param message message
     */
    private void debug(final String message) {
        if (DEBUG) {
            HiLog.info(LABEL, TAG + " " + message);
        }
    }

    /**
     * Set the count for the adapter. <br/>
     * A fake count to set limit the number of pages in the adapter
     *
     * @param fakeCount count
     */
    public void setFakeCount(final int fakeCount) {
        this.fakeCount = fakeCount;
    }
}