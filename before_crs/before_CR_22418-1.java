/*Signed-off-by: Woo-seok Jang <usok.jang@gmail.com>

added a ScreenConfiguration to SUPPORTED_SCREEN_CONFIGS
and fixed GridViewTest#testScroll to handle above changes

added a 1024x600, NORMAL screen layout size to SUPPORTED_SCREEN_CONFIGS
and enlarged testing instance of MockGridView's size to big enough to handle 1024-NORMAL layout

Change-Id:I6463b3081154a2651b6ed4969ea0c3c7acd80e9c*/
//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index 069cf80..a014ac6 100644

//Synthetic comment -- @@ -205,6 +205,8 @@
new ScreenConfiguration(960, 540, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_NORMAL),
// WSVGA     | high (191-250)   | large
new ScreenConfiguration(1024, 600, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_LARGE),

// VGA       | medium (141-190) | large
new ScreenConfiguration(640, 480, Density.MEDIUM, Configuration.SCREENLAYOUT_SIZE_LARGE),








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/GridViewTest.java b/tests/tests/widget/src/android/widget/cts/GridViewTest.java
//Synthetic comment -- index 65045d8..8f342b0 100644

//Synthetic comment -- @@ -892,6 +892,12 @@
R.drawable.animated, R.drawable.black,
R.drawable.blue, R.drawable.failed,
R.drawable.pass, R.drawable.red,
};

private final DataSetObservable mDataSetObservable = new DataSetObservable();







