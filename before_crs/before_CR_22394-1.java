/*added a ScreenConfiguration to SUPPORTED_SCREEN_CONFIGS

added a 1024x600, NORMAL screen layout size to SUPPORTED_SCREEN_CONFIGS

Change-Id:Ic5b6f8d9c5869dfbab5ffcf48c414de611974374*/
//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index 069cf80..a014ac6 100644

//Synthetic comment -- @@ -205,6 +205,8 @@
new ScreenConfiguration(960, 540, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_NORMAL),
// WSVGA     | high (191-250)   | large
new ScreenConfiguration(1024, 600, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_LARGE),

// VGA       | medium (141-190) | large
new ScreenConfiguration(640, 480, Density.MEDIUM, Configuration.SCREENLAYOUT_SIZE_LARGE),







