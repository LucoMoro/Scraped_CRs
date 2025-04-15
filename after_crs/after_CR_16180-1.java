/*Added touchscreen report verification

Change-Id:I12d5e79ca035b12356d7baf81ed79bf1193057d8*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/TouchScreenTest.java b/tests/tests/hardware/src/android/hardware/cts/TouchScreenTest.java
new file mode 100644
//Synthetic comment -- index 0000000..9700029

//Synthetic comment -- @@ -0,0 +1,36 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package android.hardware.cts;

import android.content.Context;
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;


public class TouchScreenReportTest extends AndroidTestCase {

    private PackageManager packageManager;
    
    public void testTouchScreenReport() {
        packageManager = getContext().getPackageManager();
        // Verifies that touchscreen is reported, as touch screen is a required feature for Froyo
        assertTrue(packageManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN));
    }

}









