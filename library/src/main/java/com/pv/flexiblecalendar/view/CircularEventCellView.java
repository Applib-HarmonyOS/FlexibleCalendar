package com.pv.flexiblecalendar.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Component.DrawTask;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import com.pv.flexiblecalendar.entity.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * CircularEventCellView.
 *
 * @author p-v
 */
public class CircularEventCellView extends BaseCellView implements DrawTask, Component.EstimateSizeListener {

    /**
     * event circle.
     */
    private int eventCircleY;
    /**
     * radius.
     */
    private int radius;
    /**
     * padding.
     */
    private int padding;
    /**
     * left most position.
     */
    private int leftMostPosition = Integer.MIN_VALUE;
    /**
     * paint list.
     */
    private List<Paint> paintList;
    /**
     * default val rad.
     */
    public static final int DEFAULT_VAL_RAD = 5;
    /**
     * default val cir.
     */
    public static final int DEFAULT_VAL_CIR = 1;

    /**
     * arg constructor.
     *
     * @param context context
     */
    public CircularEventCellView(final Context context) {
        super(context);
        setListeners();
    }

    /**
     * arg constructor.
     *
     * @param context context
     * @param attrs attrs
     */
    public CircularEventCellView(final Context context, final AttrSet attrs) {
        super(context, attrs);
        setListeners();
    }

    /**
     * arg constructor.
     *
     * @param context context
     * @param attrs attrs
     * @param defStyleAttr defstyleattrs
     */
    public CircularEventCellView(final Context context, final AttrSet attrs, final String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setListeners();
    }

    /**
     * set listeners.
     */
    private void setListeners() {
        addDrawTask(this);
        setEstimateSizeListener(this);
    }

    /**
     * calc start point.
     *
     * @param offset offset
     * @return int val
     */
    private int calculateStartPoint(final int offset) {
        return leftMostPosition + offset * (2 * (radius + padding));
    }

    /**
     * set events.
     *
     * @param colorList color
     */
    @Override
    public void setEvents(final List<? extends Event> colorList) {
        if (colorList != null) {
            paintList = new ArrayList<>(colorList.size());
            for (Event e : colorList) {
                Paint eventPaint = new Paint();
                eventPaint.setStyle(Paint.Style.FILL_STYLE);
                eventPaint.setColor(Color.GREEN);
                paintList.add(eventPaint);
            }
            invalidate();
            postLayout();
        }
    }

    /**
     * on draw.
     *
     * @param component component
     * @param canvas canvas
     */
    @Override
    public void onDraw(final Component component, final Canvas canvas) {

        Set<Integer> stateSet = getStateSet();

        // draw only if there is no state or just one state i.e. the regular day state
        if ((stateSet == null || stateSet.isEmpty() || (stateSet.size() == 1
                && stateSet.contains(STATE_REGULAR))) && paintList != null) {
            int num = paintList.size();
            for (int i = 0; i < num; i++) {
                canvas.drawCircle(calculateStartPoint(i), eventCircleY, radius, paintList.get(i));
            }
        }
    }

    /**
     * onestimatesize.
     *
     * @param i i
     * @param i1 i1
     */
    @Override
    public boolean onEstimateSize(final int i, final int i1) {
        Set<Integer> stateSet = getStateSet();

        //initialize paint objects only if there is no state or just one state i.e. the regular day state
        if ((stateSet == null || stateSet.isEmpty()
                || (stateSet.size() == 1 && stateSet.contains(STATE_REGULAR))) && paintList != null) {
            int num = paintList.size();

            Paint p = new Paint();
            p.setTextSize(getTextSize());
            Rect rect = new Rect();
            p.getTextBounds("31");

            eventCircleY = (3 * getHeight() + rect.getHeight()) / 4;

            //calculate left most position for the circle
            if (leftMostPosition == Integer.MIN_VALUE) {
                leftMostPosition = (getWidth() / 2) - (num / 2) * 2 * (padding + radius);
                if (num % 2 == 0) {
                    leftMostPosition = leftMostPosition + radius + padding;
                }
            }
        }
        return false;
    }
}
