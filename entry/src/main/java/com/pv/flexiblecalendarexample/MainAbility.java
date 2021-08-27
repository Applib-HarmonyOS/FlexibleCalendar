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
