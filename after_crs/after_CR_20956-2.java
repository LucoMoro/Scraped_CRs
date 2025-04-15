/*Update Screen Configuration Tests

Bug 2715720

Update ConfigurationTest to be flexible in regards to the new CDD
policies regarding screen sizes and resolutions by getting rid
of the static list of allowed resolutions and replacing it with
some looser checks.

Change-Id:If01457d7dd33bc58e32f1ef77552916006bb5cdf*/




//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index 413f6a7..cb420a6 100644

//Synthetic comment -- @@ -17,235 +17,35 @@
package android.dpi.cts;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Test for verifying a device's screen configuration.
*/
public class ConfigurationTest extends AndroidTestCase {

    public void testScreenConfiguration() {
        WindowManager windowManager =
            (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        double xInches = (double) metrics.widthPixels / metrics.xdpi;
        double yInches = (double) metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt(Math.pow(xInches, 2) + Math.pow(yInches, 2));
        assertTrue("Screen diagonal must be at least 2.5 inches: " + diagonalInches,
                diagonalInches >= 2.5d);

        double density = 160.0d * metrics.density;
        assertTrue("Screen density must be at least 100 dpi: " + density, density >= 100.0d);

        double aspectRatio = (double) Math.max(metrics.widthPixels, metrics.heightPixels)
                / (double) Math.min(metrics.widthPixels, metrics.heightPixels);
        assertTrue("Aspect ratio must be between 1.333 (4:3) and 1.779 (16:9): " + aspectRatio,
                aspectRatio >= 1.333d && aspectRatio <= 1.779d);
}
}







