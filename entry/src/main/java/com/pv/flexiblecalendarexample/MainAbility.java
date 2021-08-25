package com.pv.flexiblecalendarexample;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import com.pv.flexiblecalendarexample.slice.CalendarListActivity;

/**
 * Default MainAbility class to launch the Application.
 */
public class MainAbility extends Ability {
    /**
     * onStart.
     *
     * @param intent intent
     */
    @Override
    public void onStart(final Intent intent) {
        super.onStart(intent);
        super.setMainRoute(CalendarListActivity.class.getName());
    }
}
