/*Fix for CTS testScreenConfiguration

The previous implementation of testScreenConfiguration() failed to
pass some acceptable aspect ratios.
Example: A screen in 16:9 screen ratio that is 480 pixels wide,
would have to be 853,333 pixels high to be exactly 16:9. This is
of course not possible, so we must allow both 853 and 854 pixels
in this example. The previous implementation would only accept
853.

Change-Id:Ic5e7fa523869d1036821bb29c602ee82ec750238*/
//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index cb420a6..624f35a 100644

//Synthetic comment -- @@ -43,9 +43,11 @@
double density = 160.0d * metrics.density;
assertTrue("Screen density must be at least 100 dpi: " + density, density >= 100.0d);

        double aspectRatio = (double) Math.max(metrics.widthPixels, metrics.heightPixels)
                / (double) Math.min(metrics.widthPixels, metrics.heightPixels);
        assertTrue("Aspect ratio must be between 1.333 (4:3) and 1.779 (16:9): " + aspectRatio,
                aspectRatio >= 1.333d && aspectRatio <= 1.779d);
}
}







