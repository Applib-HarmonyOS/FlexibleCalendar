package com.pv.flexiblecalendarexample.slice.widget;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.app.Context;
import com.pv.flexiblecalendar.view.CircularEventCellView;

/**
 * ExampleCellView.
 *
 * @author p-v
 */
public class ExampleCellView extends CircularEventCellView implements Component.EstimateSizeListener {

    /**
     * ExampleCellView constructor.
     *
     * @param context context
     */
    public ExampleCellView(final Context context) {
        super(context);
    }

    /**
     * ExampleCellView constructor.
     *
     * @param context context
     *
     * @param attrs attrs
     *
     */
    public ExampleCellView(final Context context, final AttrSet attrs) {
        super(context, attrs);
    }

    /**
     * ExampleCellView constructor.
     *
     * @param context context
     *
     * @param attrs attrs
     *
     * @param defStyleAttr defStyleAttr
     */
    public ExampleCellView(final Context context, final AttrSet attrs, final String defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * onestimate size.
     *
     * @param widthMeasureSpec width
     * @param heightMeasureSpec height
     */
    @Override
     public boolean onEstimateSize(final int widthMeasureSpec, final int heightMeasureSpec) {
        int width = getEstimatedWidth();
        int height = (7 * width) / 8;
        int heightMesSpec = EstimateSpec.getSizeWithMode(height, EstimateSpec.PRECISE);
        super.onEstimateSize(widthMeasureSpec, heightMesSpec);
        return true;
    }
}
