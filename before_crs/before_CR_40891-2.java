/*Add case with DisplayMetrics.DENSITY_XXHIGH to cover fullHD resolution
and adjust the bound of condition.

Change-Id:Ia3acf488aff45366d1104edf315f9f925c75aef3*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/ActivityManagerMemoryClassTest.java b/tests/tests/app/src/android/app/cts/ActivityManagerMemoryClassTest.java
//Synthetic comment -- index 1db04ea..622055c 100644

//Synthetic comment -- @@ -90,6 +90,10 @@
expectedMinimumMemory = isXLarge ? 128 : 64;
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
assertTrue("DisplayMetrics#densityDpi must be one of the DisplayMetrics.DENSITY_* values: "
+ allowedDensities, allowedDensities.contains(metrics.densityDpi));









//Synthetic comment -- diff --git a/tests/tests/util/src/android/util/cts/DisplayMetricsTest.java b/tests/tests/util/src/android/util/cts/DisplayMetricsTest.java
//Synthetic comment -- index 821bb09..d7c1895 100644

//Synthetic comment -- @@ -37,8 +37,8 @@
assertEquals(0, outMetrics.widthPixels);
assertEquals(0, outMetrics.heightPixels);
// according to Android enmulator doc UI -scale confine density should between 0.1 to 3
        assertTrue((0.1 < outMetrics.density) && (outMetrics.density < 3));
        assertTrue((0.1 < outMetrics.scaledDensity) && (outMetrics.scaledDensity < 3));
assertTrue(0 < outMetrics.xdpi);
assertTrue(0 < outMetrics.ydpi);

//Synthetic comment -- @@ -49,8 +49,8 @@
assertEquals(display.getHeight(), metrics.heightPixels);
assertEquals(display.getWidth(), metrics.widthPixels);
// according to Android enmulator doc UI -scale confine density should between 0.1 to 3
        assertTrue((0.1 < metrics.density) && (metrics.density < 3));
        assertTrue((0.1 < metrics.scaledDensity) && (metrics.scaledDensity < 3));
assertTrue(0 < metrics.xdpi);
assertTrue(0 < metrics.ydpi);
}







