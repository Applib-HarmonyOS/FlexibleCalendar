/*
 * Copyright (C) 2020-21 Application Library Enigineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
