/*Define DENSITY_XXHIGH to pass CTS test

We run CTS test our binary internaly, but fail because of these issues.

android.app#android.app.cts.ActivityManagerMemoryClassTest#testGetMemoryClass
android.dpi#android.dpi.cts.ConfigurationTest#testScreenConfiguration
android.util#android.util.cts.DisplayMetricsTest#testDisplayMetricsOp

'switch~case statement' in assertMemoryForScreenDensity() function

The reason why these happened is simple. CTS tool doesn't work in 480 DPI circumstance.
There was no 'case statement' for xxhigh. So, we add DENSITY_XXHIGH case statement.
we tested this code and works well.
but it still have some bugs. someone should fix them.

Signed-off-by: Hyangsuk Chae <neo.chae@lge.com>

Change-Id:I60cdf80083341191778c81259b427c68d9e734ad*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ActivityManagerMemoryClassTest.java b/tests/tests/app/src/android/app/cts/ActivityManagerMemoryClassTest.java
//Synthetic comment -- index 1db04ea..622055c 100644

//Synthetic comment -- @@ -90,6 +90,10 @@
expectedMinimumMemory = isXLarge ? 128 : 64;
break;

            case DisplayMetrics.DENSITY_XXHIGH:
                expectedMinimumMemory = isXLarge ? 256 : 128;
                break;

default:
throw new IllegalArgumentException("No memory requirement specified "
+ " for screen density " + screenDensity);








//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index 937d8f8..6f26b6e 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
allowedDensities.add(DisplayMetrics.DENSITY_TV);
allowedDensities.add(DisplayMetrics.DENSITY_HIGH);
allowedDensities.add(DisplayMetrics.DENSITY_XHIGH);
        allowedDensities.add(DisplayMetrics.DENSITY_XXHIGH);
assertTrue("DisplayMetrics#densityDpi must be one of the DisplayMetrics.DENSITY_* values: "
+ allowedDensities, allowedDensities.contains(metrics.densityDpi));









//Synthetic comment -- diff --git a/tests/tests/util/src/android/util/cts/DisplayMetricsTest.java b/tests/tests/util/src/android/util/cts/DisplayMetricsTest.java
//Synthetic comment -- index 821bb09..92fe4ac 100644

//Synthetic comment -- @@ -37,8 +37,9 @@
assertEquals(0, outMetrics.widthPixels);
assertEquals(0, outMetrics.heightPixels);
// according to Android enmulator doc UI -scale confine density should between 0.1 to 3
		// Android supporting xxhdpi(~480dpi), so metrics.density is allowed up to 3 
        assertTrue((0.1 < outMetrics.density) && (outMetrics.density <= 3));
        assertTrue((0.1 < outMetrics.scaledDensity) && (outMetrics.scaledDensity <= 3));
assertTrue(0 < outMetrics.xdpi);
assertTrue(0 < outMetrics.ydpi);

//Synthetic comment -- @@ -49,8 +50,8 @@
assertEquals(display.getHeight(), metrics.heightPixels);
assertEquals(display.getWidth(), metrics.widthPixels);
// according to Android enmulator doc UI -scale confine density should between 0.1 to 3
        assertTrue((0.1 < metrics.density) && (metrics.density <= 3));
        assertTrue((0.1 < metrics.scaledDensity) && (metrics.scaledDensity <= 3));
assertTrue(0 < metrics.xdpi);
assertTrue(0 < metrics.ydpi);
}







