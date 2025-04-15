/*Removed Quick launch menu when no HW Search key exists

The "Quick launch" menu should only be visible if there
is both a HW keyboard and a HW "Search" key, since the
functionality cannot be used if either is missing.

Change-Id:Ia770d40919d92d0e7a32d1a65a0fe73404af578a*/




//Synthetic comment -- diff --git a/src/com/android/settings/ApplicationSettings.java b/src/com/android/settings/ApplicationSettings.java
//Synthetic comment -- index a919ae8..1aecb86 100644

//Synthetic comment -- @@ -27,6 +27,8 @@
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

public class ApplicationSettings extends PreferenceActivity implements
DialogInterface.OnClickListener {
//Synthetic comment -- @@ -76,8 +78,9 @@
});
}

        if (getResources().getConfiguration().keyboard == Configuration.KEYBOARD_NOKEYS
                || !KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_SEARCH)) {
            // No hard keyboard or search key, remove the setting for quick launch
Preference quickLaunchSetting = findPreference(KEY_QUICK_LAUNCH);
getPreferenceScreen().removePreference(quickLaunchSetting);
}








//Synthetic comment -- diff --git a/tests/src/com/android/settings/QuickLaunchTests.java b/tests/src/com/android/settings/QuickLaunchTests.java
new file mode 100644
//Synthetic comment -- index 0000000..342035a

//Synthetic comment -- @@ -0,0 +1,71 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.settings;

import com.android.settings.ApplicationSettings;
import android.test.ActivityInstrumentationTestCase2;

import android.content.res.Configuration;
import android.preference.Preference;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

/**
 * Tests related to the quick launch menu item in the application menu.
 */
public class QuickLaunchTests extends ActivityInstrumentationTestCase2<ApplicationSettings> {

    private static final String KEY_QUICK_LAUNCH = "quick_launch";

    ApplicationSettings mApplicationSettings;

    public QuickLaunchTests() {
        super(ApplicationSettings.class.getPackage().getName(), ApplicationSettings.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mApplicationSettings = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mApplicationSettings = null;
    }

    /**
     * Test that the QuickLaunch menu is only available if there
     * is both a HW keyboard and a HW search button.
     */
    public void testQuickLaunchMenuAvailability() {
        Preference quickLaunchSetting = mApplicationSettings.findPreference(KEY_QUICK_LAUNCH);

        if (mApplicationSettings.getResources().getConfiguration().keyboard ==
                    Configuration.KEYBOARD_NOKEYS
                    || !KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_SEARCH)) {
            // If HW keyboard or search key is missing, the setting for
            // quick launch should not be available
            assertNull("QuickLaunch menu item is available even though it should not be",
                    quickLaunchSetting);
        } else {
            assertNotNull("QuickLaunch menu item is not available even though it should be",
                    quickLaunchSetting);
        }
    }
}







