/*Update CTS requirement for screen aspect ratio

Since Android CDD specifies screen aspect ratio must between
4:3 and 16:9, it should be accepatable if we limit application
running in exactly 16:9 restricted area even the actual screen
aspect ratio is out of definition. So instead of using
getDefaultDisplay(), call getDisplayMetrics() to get the current
display metrics that are in effect for resources.

Change-Id:Ia9cc16281864ce7432c1b563fcf2dafc1841f7d6*/




//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index 624f35a..0e3a2a7 100644

//Synthetic comment -- @@ -28,11 +28,7 @@
public class ConfigurationTest extends AndroidTestCase {

public void testScreenConfiguration() {
        final DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

double xInches = (double) metrics.widthPixels / metrics.xdpi;
double yInches = (double) metrics.heightPixels / metrics.ydpi;







