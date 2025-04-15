/*Update CTS requirement for screen aspect ratio

Since Android CDD specifies screen aspect ratio must between
4:3 and 16:9, it should be accepatable if we limit application
running in exactly 16:9 restricted area even the actual screen
aspect ratio is out of definition. Give a second chance to call
getDisplayMetrics() in order to get the current display metrics
that are in effect for resources while running aspect ratio test.

Change-Id:Ia9cc16281864ce7432c1b563fcf2dafc1841f7d6*/
//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index 624f35a..6d8a1ee 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
* Test for verifying a device's screen configuration.
*/
public class ConfigurationTest extends AndroidTestCase {

public void testScreenConfiguration() {
WindowManager windowManager =
//Synthetic comment -- @@ -43,11 +45,18 @@
double density = 160.0d * metrics.density;
assertTrue("Screen density must be at least 100 dpi: " + density, density >= 100.0d);

        int max = Math.max(metrics.widthPixels, metrics.heightPixels);
        int min = Math.min(metrics.widthPixels, metrics.heightPixels);
        boolean format16x9 = Math.floor(max * 9.0d / 16.0d) <= min;
        boolean format4x3 = Math.ceil(max * 3.0d / 4.0d) >= min;
        assertTrue("Aspect ratio must be between 4:3 and 16:9. It was " + max + ":" + min,
                format4x3 && format16x9);
}
}







