/*Change the test case about SCREENLAYOUT_LONG
 depending whether the navigation bar is displayed or not.

For example
  1. The device has WXGA (1280x800) display.
  2. The navigation bar is displayed
  3. The height of navigation bar is 64pt.

At landscape mode, the height is 736 (=800-64) and  the width is 1280,
then the device is considered as wider than WVGA.
(i.e. SCREENLAYOUT_LONG_YES)

At portrait mode, the height is 1216 (=1280-64) and the width is 800,
then the device is not considered as wider than WVGA.
(i.e. SCREENLAYOUT_LONG_NO)

I modified to check the navigation bar is displayed or not.*/
//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationScreenLayoutTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationScreenLayoutTest.java
//Synthetic comment -- index d4c3611..f0372e9 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.test.ActivityInstrumentationTestCase2;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ConfigurationScreenLayoutTest
//Synthetic comment -- @@ -45,6 +46,9 @@
int expectedSize = expectedScreenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
int expectedLong = expectedScreenLayout & Configuration.SCREENLAYOUT_LONG_MASK;

// Check that all four orientations report the same configuration value.
for (int i = 0; i < ORIENTATIONS.length; i++) {
Activity activity = startOrientationActivity(ORIENTATIONS[i]);
//Synthetic comment -- @@ -52,6 +56,15 @@
int actualSize = mConfig.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
int actualLong = mConfig.screenLayout & Configuration.SCREENLAYOUT_LONG_MASK;

assertEquals("Expected screen size value of " + expectedSize + " but got " + actualSize
+ " for orientation " + ORIENTATIONS[i], expectedSize, actualSize);
assertEquals("Expected screen long value of " + expectedLong + " but got " + actualLong
//Synthetic comment -- @@ -80,6 +93,11 @@
return screenLayout;
}

private Activity startOrientationActivity(int orientation) {
Intent intent = new Intent();
intent.putExtra(OrientationActivity.EXTRA_ORIENTATION, orientation);







