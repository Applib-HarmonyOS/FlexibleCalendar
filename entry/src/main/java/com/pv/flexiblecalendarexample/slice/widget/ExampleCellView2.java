package com.pv.flexiblecalendarexample.slice.widget;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import com.pv.flexiblecalendar.entity.Event;
import com.pv.flexiblecalendar.view.BaseCellView;
import com.pv.flexiblecalendarexample.ResourceTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Another ExampleCellView class.
 *
 * @author p-v
 */
public class ExampleCellView2 extends BaseCellView implements Component.DrawTask, Component.EstimateSizeListener {

    /**
     * To define eventCircle.
     */
    private int eventCircleY;

    /**
     * To define radius.
     */
    private int radius;

    /**
     * To define padding.
     */
    private int padding;

    /**
     * MIN_VALUE = -2147483648.
     */
    private int leftMostPosition = Integer.MIN_VALUE;

    /**
     * List object to hold the paintList values.
     */
    private List<Paint> paintList;

    /**
     * ExampleCellView Constructor.
     *
     * @param context context
     */
    public ExampleCellView2(final Context context) {
        super(context);
        init();
    }

    /**
     * ExampleCellView Constructor.
     *
     * @param context context
     *
     * @param attrs attrs
     */
    public ExampleCellView2(final Context context, final AttrSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * ExampleCellView Constructor.
     *
     * @param context context
     *
     * @param attrs attrs
     *
     * @param defStyleAttr defStyleAttr
     */
    public ExampleCellView2(final Context context, final AttrSet attrs, final String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * init method.
     */
    private void init()  {
        try {
            radius = (int) getResourceManager().getElement(ResourceTable.Float_example_cell_view_event_radius)
                                               .getFloat();
        } catch (IOException | NotExistException | WrongTypeException e) {
            e.printStackTrace();
        }
        try {
            padding = (int) getResourceManager().getElement(ResourceTable.Float_example_cell_view_event_spacing)
                                                .getFloat();
        } catch (IOException | NotExistException | WrongTypeException e) {
            e.printStackTrace();
        }
    }

    /**
     * To calculate the Start point.
     *
     * @param offset offset
     *
     * @return integer
     */
    private int calculateStartPoint(final int offset) {
        return leftMostPosition + offset * (2 * (radius + padding));
    }

    /**
     * set events.
     *
     * @param colorList colorlist
     */
    @Override
    public void setEvents(List<? extends Event> colorList) {
        if (colorList != null) {
            paintList = new ArrayList<>(colorList.size());
            for (Event e : colorList) {
                Paint eventPaint = new Paint();
                eventPaint.setAntiAlias(true);
                eventPaint.setStyle(Paint.Style.FILL_STYLE);
                paintList.add(eventPaint);
            }
            invalidate();
            postLayout();
        }
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
        if (paintList != null) {
            int num = paintList.size();
            for (int i = 0; i < num; i++) {
                canvas.drawCircle(calculateStartPoint(i), eventCircleY, radius, paintList.get(i));
            }
        }
    }
}