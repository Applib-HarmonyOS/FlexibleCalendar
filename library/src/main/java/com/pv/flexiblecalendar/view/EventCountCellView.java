package com.pv.flexiblecalendar.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import com.pv.flexiblecalendar.entity.Event;
import java.util.List;

/**
 * Cell view with the event count.
 *
 * @author p-v
 */
public class EventCountCellView extends BaseCellView implements Component.DrawTask, Component.EstimateSizeListener {

    /**
     * paint.
     */
    private Paint mPaint;
    /**
     * text paint.
     */
    private Paint mTextPaint;
    /**
     * event count.
     */
    private int mEventCount;
    /**
     * event circle y.
     */
    private int eventCircleY;
    /**
     * event circle x.
     */
    private int eventCircleX;
    /**
     * texty.
     */
    private int mTextY;
    /**
     * event radius.
     */
    private int eventRadius = 15;
    /**
     * event text color.
     */
    private Color eventTextColor = Color.WHITE;
    /**
     * event b/g.
     */
    private Color eventBackground = Color.BLACK;
    /**
     * event text size.
     */
    private int eventTextSize = -1;

    /**
     * arg constructor.
     *
     * @param context context
     */
    public EventCountCellView(final Context context) {
        super(context);
        setListeners();
    }

    /**
     * arg constructor.
     *
     * @param context context
     * @param attrs attrs
     */
    public EventCountCellView(final Context context, final AttrSet attrs) {
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
    public EventCountCellView(final Context context, final AttrSet attrs, final String defStyleAttr) {
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
     * set events.
     *
     * @param colorList colorlist
     */
    @Override
    public void setEvents(final List<? extends Event> colorList) {
        if (colorList != null && !colorList.isEmpty()) {
            mEventCount = colorList.size();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL_STYLE);
            mPaint.setColor(eventBackground);
            invalidate();
            postLayout();
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
        if (mEventCount > 0) {
            Paint p = new Paint();
            p.setTextSize(getTextSize());

            Rect rect = new Rect();
            p.getTextBounds("31"); // measuring using fake text

            eventCircleY = (getHeight() - rect.getHeight()) / 4;
            eventCircleX = (3 * getWidth() + rect.getWidth()) / 4;

            mTextPaint = new Paint();
            mTextPaint.setStyle(Paint.Style.FILL_STYLE);
            mTextPaint.setTextSize(eventTextSize == -1 ? getTextSize() / 2 : eventTextSize);
            mTextPaint.setColor(eventTextColor);
            mTextPaint.setTextAlign(2);

            mTextY = eventCircleY + eventRadius / 2;
        }
        return false;
    }

    /**
     * on draw.
     *
     * @param component component
     * @param canvas canvas
     */
    @Override
    public void onDraw(final Component component, final Canvas canvas) {
        if (mEventCount > 0 && mPaint != null && mTextPaint != null) {
            canvas.drawCircle(eventCircleX, eventCircleY, eventRadius, mPaint);
            canvas.drawText(mTextPaint, String.valueOf(mEventCount), eventCircleX, mTextY);
        }
    }
}
