package com.pv.flexiblecalendar.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.app.Context;

/**
 * Created by p-v on 15/07/15.
 */
public class SquareCellView extends CircularEventCellView implements Component.EstimateSizeListener {

    /**
     * arg constructor.
     *
     * @param context context
     */
    public SquareCellView(final Context context) {
        super(context);
        setListenersSqCellView();
    }

    /**
     * arg constructor.
     *
     * @param context context
     * @param attrs attrs
     */
    public SquareCellView(Context context, AttrSet attrs) {
        super(context, attrs);
        setListenersSqCellView();
    }

    /**
     * arg constructor.
     *
     * @param context context
     * @param attrs attrs
     * @param defStyleAttr defstyleattrs
     */
    public SquareCellView(Context context, AttrSet attrs, String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setListenersSqCellView();
    }

    /**
     * set listeners.
     */
    private void setListenersSqCellView() {
        setEstimateSizeListener(this);
    }


    /**
     * on estimate size.
     *
     * @param widthMeasureSpec width
     * @param heightMeasureSpec height
     */
    @Override
    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        //making sure the cell view is a square
        super.setEstimatedSize(widthMeasureSpec, widthMeasureSpec);
        return true;
    }
}
