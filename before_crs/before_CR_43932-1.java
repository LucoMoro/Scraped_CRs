/*Added DENSITY_XXHIGH as allowed density

Corrected the CTS for Android 4.1.1

Change-Id:I317d7596be54aa6509ec725bbb0eb0fac5702616*/
//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index 937d8f8..6f26b6e 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
allowedDensities.add(DisplayMetrics.DENSITY_TV);
allowedDensities.add(DisplayMetrics.DENSITY_HIGH);
allowedDensities.add(DisplayMetrics.DENSITY_XHIGH);
assertTrue("DisplayMetrics#densityDpi must be one of the DisplayMetrics.DENSITY_* values: "
+ allowedDensities, allowedDensities.contains(metrics.densityDpi));








