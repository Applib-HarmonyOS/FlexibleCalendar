package com.p_v.flexiblecalendarexample.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendarexample.R;

import java.util.List;

/**
 * @author p-v
 */
public class ExampleCellView3 extends BaseCellView {

    private boolean hasEvents;

    public ExampleCellView3(Context context) {
        super(context);
    }

    public ExampleCellView3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExampleCellView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("VEEJAY","EXCV3 ONDRAW "+!getStateSet().contains(STATE_SELECTED)+" "+!getStateSet().contains(SELECTED_TODAY)+" "+getStateSet().contains(STATE_REGULAR)+" "+hasEvents);
        if (!getStateSet().contains(STATE_SELECTED) && !getStateSet().contains(SELECTED_TODAY) &&
                getStateSet().contains(STATE_REGULAR) && hasEvents) {
            this.setBackgroundColor(Color.BLUE);
        }
        if (getStateSet().contains(STATE_SELECTED) && hasEvents) {
            this.setBackgroundResource(R.drawable.cell_red_background);
        }
    }

    @Override
    public void setEvents(List<? extends Event> colorList) {
        this.hasEvents = colorList != null && !colorList.isEmpty();
            Log.d("VEEJAY","EXCV3 SETEVENTS "+hasEvents);
        invalidate();
        requestLayout();
    }

}
