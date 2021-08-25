package com.pv.flexiblecalendar.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.app.Context;
import com.pv.flexiblecalendar.entity.Event;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BaseCellView.
 *
 * @author p-v
 */
public abstract class BaseCellView extends Text implements Component.EstimateSizeListener {

    /**
     * TODAY.
     */
    public static final int TODAY = 0;
    /**
     * SELECTED.
     */
    public static final int SELECTED = 1;
    /**
     * REGULAR.
     */
    public static final int REGULAR = 3;
    /**
     * SELECTED TODAY.
     */
    public static final int SELECTED_TODAY = 4;
    /**
     * OUTSIDE MONTH.
     */
    public static final int OUTSIDE_MONTH = 5;
    /**
     * STATE TODAY.
     */
    public static final int STATE_TODAY = 100;
    /**
     * STATE REGULAR.
     */
    public static final int STATE_REGULAR = 200;
    /**
     * STATE SELECTED.
     */
    public static final int STATE_SELECTED = 300;
    /**
     * STATE OUTSIDE MONTH.
     */
    public static final int STATE_OUTSIDE_MONTH = 400;
    /**
     * STATE SET.
     */
    private Set<Integer> stateSet;

    /**
     * arg constructor.
     *
     * @param context context
     */
    protected BaseCellView(final Context context) {
        super(context);
        stateSet = new HashSet<>(3);
    }

    /**
     * arg constructor.
     *
     * @param context context
     * @param attrs   attrs
     */
    protected BaseCellView(final Context context, final AttrSet attrs) {
        super(context, attrs);
        stateSet = new HashSet<>(3);
    }

    /**
     * arg constructor.
     *
     * @param context      context
     * @param attrs        attrs
     * @param defStyleAttr defstyleattrs
     */
    protected BaseCellView(final Context context, final AttrSet attrs, final String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        stateSet = new HashSet<>(3);
    }

    /**
     * add state.
     *
     * @param state state
     */
    public void addState(final int state) {
        stateSet.add(state);
    }

    /**
     * clear state.
     */
    public void clearAllStates() {
        stateSet.clear();
    }

    /**
     * set events.
     *
     * @param colorList colorlist
     */
    public abstract void setEvents(final List<? extends Event> colorList);

    /**
     * get state set.
     *
     * @return set of int
     */
    public Set<Integer> getStateSet() {
        return stateSet;
    }

    /**
     * interface CellType.
     */
    //    @IntDef({TODAY, SELECTED, REGULAR, SELECTED_TODAY, OUTSIDE_MONTH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CellType {
    }

    /**
     * onestimatesize.
     *
     * @param i  i
     * @param i1 i1
     */
    @Override
    public boolean onEstimateSize(final int i, final int i1) {
        return false;
    }
}
