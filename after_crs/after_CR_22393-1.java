/*added a ScreenConfiguration to SUPPORTED_SCREEN_CONFIGS

added a 1024x600, NORMAL screen layout size to SUPPORTED_SCREEN_CONFIGS

Change-Id:Ifce25fb5c3b94526551605557ae607e82767133e*/




//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index 069cf80..eb948a6 100644

//Synthetic comment -- @@ -205,6 +205,7 @@
new ScreenConfiguration(960, 540, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_NORMAL),
// WSVGA     | high (191-250)   | large
new ScreenConfiguration(1024, 600, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_LARGE),
		new ScreenConfiguration(1024, 600, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_NORMAL),

// VGA       | medium (141-190) | large
new ScreenConfiguration(640, 480, Density.MEDIUM, Configuration.SCREENLAYOUT_SIZE_LARGE),







