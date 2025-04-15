/*fix for code review Mohammad Shamsi

Change-Id:If41b27b7d548e9b150011d3ae1a4853197707701*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/TouchScreenTest.java b/tests/tests/hardware/src/android/hardware/cts/TouchScreenTest.java
//Synthetic comment -- index 9700029..c1a150e 100644

//Synthetic comment -- @@ -16,17 +16,14 @@

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







