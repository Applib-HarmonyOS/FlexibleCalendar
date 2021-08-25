package com.pv.flexiblecalendarexample.slice.widget;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Canvas;
import ohos.app.Context;
import com.pv.flexiblecalendar.entity.Event;
import com.pv.flexiblecalendar.view.BaseCellView;
import java.util.List;

/**
 * ExampleCellView3.
 *
 * @author p-v
 */
public class ExampleCellView3 extends BaseCellView implements Component.EstimateSizeListener, Component.DrawTask {

    /**
     * Boolean variable to judge the conditions.
     */
    private boolean hasEvents;

    /**
     * ExampleCellView Constructor.
     *
     * @param context context
     */
    public ExampleCellView3(final Context context) {
        super(context);
        setListeners();
    }

    /**
     * ExampleCellView constructor.
     *
     * @param context context
     *
     * @param attrs   attrs
     */
    public ExampleCellView3(final Context context, final AttrSet attrs) {
        super(context, attrs);
        setListeners();
    }

    /**
     * ExampleCellView Constructor.
     *
     * @param context      context
     *
     * @param attrs        attrs
     *
     * @param defStyleAttr defStyleAttr
     */
    public ExampleCellView3(final Context context, final AttrSet attrs, final String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setListeners();
    }

    /**
     * To set the Listeners.
     */
    private void setListeners() {
        addDrawTask(this);
        setEstimateSizeListener(this);
    }

    /**
     * set events.
     *
     * @param colorList colorlist
     */
    @Override
    public void setEvents(final List<? extends Event> colorList) {
        this.hasEvents = colorList != null && !colorList.isEmpty();
        invalidate();
        postLayout();
    }

    /**
     * onestimate size.
     *
     * @param widthMeasureSpec width
     *
     * @param heightMeasureSpec height
     */
    @Override
    public boolean onEstimateSize(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onEstimateSize(widthMeasureSpec, heightMeasureSpec);
        return true;
    }

    /**
     * ondraw.
     *
     * @param component component
     *
     * @param canvas canvas
     */
    @Override
    public void onDraw(final Component component, final Canvas canvas) {
        if (!getStateSet().contains(STATE_SELECTED) && !getStateSet().contains(SELECTED_TODAY)
                && getStateSet().contains(STATE_REGULAR) && hasEvents) {
            this.setBackground(getElementFromResourceIdBlue());
        }
        if (getStateSet().contains(STATE_SELECTED) && hasEvents) {
            this.setBackground(this.getElementFromResourceIdRed());
        }
    }

    /**
     * To get the Element from ResourceIdBlue.
     *
     * @return Element
     */
    public Element getElementFromResourceIdBlue() {
        RgbColor rgbColor = new RgbColor(3, 36, 252);
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(rgbColor);
        return shapeElement;
    }

    /**
     * To get the Element from ResourceIdRed.
     *
     * @return Element
     */
    public Element getElementFromResourceIdRed() {
        RgbColor rgbColor = new RgbColor(230, 7, 29);
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(rgbColor);
        return shapeElement;
    }
}