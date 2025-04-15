/*Check the intents for bad data.

This fix avoids NPEs that may occur if the Activity intent
does not contain expected data.

Change-Id:Ib0b7d5fc293061fcd623bce229d14c35fa56b0cc*/




//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index 3f0c02f..b4f0adb 100644

//Synthetic comment -- @@ -149,9 +149,9 @@

mFirstTime = icicle == null;

        if (Intent.ACTION_EDIT.equals(action)) {
mUri = intent.getData();
        } else if (Intent.ACTION_INSERT.equals(action)) {
if (mFirstTime || icicle.getInt(SAVED_POS) == 0) {
mUri = getContentResolver().insert(intent.getData(), new ContentValues());
} else {








//Synthetic comment -- diff --git a/src/com/android/settings/AppWidgetPickActivity.java b/src/com/android/settings/AppWidgetPickActivity.java
//Synthetic comment -- index 176ac80..3654ad6 100644

//Synthetic comment -- @@ -86,7 +86,9 @@
ArrayList<AppWidgetProviderInfo> customInfo = null;
ArrayList<Bundle> customExtras = null;
try_custom_items: {
            if (extras != null) {
                customInfo = extras.getParcelableArrayList(AppWidgetManager.EXTRA_CUSTOM_INFO);
            }
if (customInfo == null || customInfo.size() == 0) {
Log.i(TAG, "EXTRA_CUSTOM_INFO not present.");
break try_custom_items;
//Synthetic comment -- @@ -102,7 +104,9 @@
}
}

            if (extras != null) {
                customExtras = extras.getParcelableArrayList(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);
            }
if (customExtras == null) {
customInfo = null;
Log.e(TAG, "EXTRA_CUSTOM_INFO without EXTRA_CUSTOM_EXTRAS");








//Synthetic comment -- diff --git a/src/com/android/settings/applications/InstalledAppDetails.java b/src/com/android/settings/applications/InstalledAppDetails.java
//Synthetic comment -- index 912cc3e..2b28429 100644

//Synthetic comment -- @@ -396,8 +396,14 @@
}

Intent intent = getIntent();
        Uri uri = intent.getData();
        String packageName = null;
        mAppEntry = null;

        if (uri != null) {
            packageName = uri.getSchemeSpecificPart();
            mAppEntry = mState.getEntry(packageName);
        }

if (mAppEntry == null) {
return false; // onCreate must have failed, make sure to exit








//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/BluetoothPairingDialog.java b/src/com/android/settings/bluetooth/BluetoothPairingDialog.java
//Synthetic comment -- index acbb99c..5c0788a 100644

//Synthetic comment -- @@ -79,8 +79,7 @@
super.onCreate(savedInstanceState);

Intent intent = getIntent();
        if (!BluetoothDevice.ACTION_PAIRING_REQUEST.equals(intent.getAction())) {
Log.e(TAG,
"Error: this activity may be started only with intent " +
BluetoothDevice.ACTION_PAIRING_REQUEST);








//Synthetic comment -- diff --git a/tests/src/com/android/settings/UIntentMissingInformationInApnEditorTests.java b/tests/src/com/android/settings/UIntentMissingInformationInApnEditorTests.java
new file mode 100644
//Synthetic comment -- index 0000000..e3a6404

//Synthetic comment -- @@ -0,0 +1,48 @@
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

import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class UIntentMissingInformationInApnEditorTests extends ActivityUnitTestCase<ApnEditor> {

    public UIntentMissingInformationInApnEditorTests() {
        super(ApnEditor.class);
    }

    public UIntentMissingInformationInApnEditorTests(Class<ApnEditor> activityClass) {
        super(activityClass);
    }

    /**
     * Test sending an Intent missing action information to
     * AppWidgetPickActivity.
     */
    public void testIntentToApnEditor() throws Exception {
        ApnEditor testedActivity = null;
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "ApnEditor");
        intent.setAction(null);

        try {
            testedActivity = (ApnEditor)startActivity(intent, null, null);
        } catch (Exception e) {
            fail("UIntentMissingInformationInApnEditorTests: " + e);
        }
    }
}








//Synthetic comment -- diff --git a/tests/src/com/android/settings/UIntentMissingInformationInAppWidgetPickActivityTests.java b/tests/src/com/android/settings/UIntentMissingInformationInAppWidgetPickActivityTests.java
new file mode 100644
//Synthetic comment -- index 0000000..a592d9d

//Synthetic comment -- @@ -0,0 +1,49 @@
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

import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class UIntentMissingInformationInAppWidgetPickActivityTests extends
        ActivityUnitTestCase<AppWidgetPickActivity> {

    public UIntentMissingInformationInAppWidgetPickActivityTests() {
        super(AppWidgetPickActivity.class);
    }

    public UIntentMissingInformationInAppWidgetPickActivityTests(
            Class<AppWidgetPickActivity> activityClass) {
        super(activityClass);
    }

    /**
     * Test sending an Intent missing extras information to
     * AppWidgetPickActivity.
     */
    public void testIntentToAppWidgetPickActivity() throws Exception {
        AppWidgetPickActivity testedActivity = null;
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "AppWidgetPickActivity");

        try {
            testedActivity = (AppWidgetPickActivity)startActivity(intent, null, null);
        } catch (Exception e) {
            fail("UIntentMissingInformationInAppWidgetPickActivityTests: " + e);
        }
    }
}








//Synthetic comment -- diff --git a/tests/src/com/android/settings/UIntentMissingInformationInBluetoothPairingDialogTests.java b/tests/src/com/android/settings/UIntentMissingInformationInBluetoothPairingDialogTests.java
new file mode 100644
//Synthetic comment -- index 0000000..7ee460f

//Synthetic comment -- @@ -0,0 +1,52 @@
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

import com.android.settings.bluetooth.BluetoothPairingDialog;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class UIntentMissingInformationInBluetoothPairingDialogTests extends
        ActivityUnitTestCase<BluetoothPairingDialog> {

    public UIntentMissingInformationInBluetoothPairingDialogTests() {
        super(BluetoothPairingDialog.class);
    }

    public UIntentMissingInformationInBluetoothPairingDialogTests(
            Class<BluetoothPairingDialog> activityClass) {
        super(activityClass);
    }

    /**
     * Test sending an Intent missing action information to
     * BluetoothPairingDialog.
     */
    public void testIntentToBluetoothPairingDialog() throws Exception {
        BluetoothPairingDialog testedActivity = null;
        Intent intent = new Intent();
        intent.setClassName("com.android.settings.bluetooth", "BluetoothPairingDialog");
        intent.setAction(null);

        try {
            testedActivity = (BluetoothPairingDialog)startActivity(intent, null, null);
        } catch (Exception e) {
            fail("UIntentMissingInformationInBluetoothPairingDialogTests: " + e);
        }
    }
}







