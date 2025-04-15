/*Harden tests for android.content.res.Configuration

Extend ConfigurationTest with all public fields in Configuration.

Change-Id:I3c9c876722a07ab369268867efeb457688a151a6*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/res/cts/ConfigurationTest.java b/tests/tests/content/src/android/content/res/cts/ConfigurationTest.java
//Synthetic comment -- index 70443b0..f8a074b 100644

//Synthetic comment -- @@ -40,11 +40,19 @@
mConfig.fontScale = 2;
mConfig.mcc = mConfig.mnc = 1;
mConfig.locale = Locale.getDefault();
mConfig.touchscreen = Configuration.TOUCHSCREEN_NOTOUCH;
mConfig.keyboard = Configuration.KEYBOARD_NOKEYS;
mConfig.keyboardHidden = Configuration.KEYBOARDHIDDEN_NO;
mConfig.navigation = Configuration.NAVIGATION_NONAV;
mConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
}

public void testConstructor() {
//Synthetic comment -- @@ -57,6 +65,41 @@
final Configuration cfg2 = new Configuration();
assertEquals(0, cfg1.compareTo(cfg2));

cfg1.orientation = 2;
cfg2.orientation = 3;
assertEquals(-1, cfg1.compareTo(cfg2));
//Synthetic comment -- @@ -64,6 +107,13 @@
cfg2.orientation = 2;
assertEquals(1, cfg1.compareTo(cfg2));

cfg1.navigation = 2;
cfg2.navigation = 3;
assertEquals(-1, cfg1.compareTo(cfg2));
//Synthetic comment -- @@ -71,6 +121,13 @@
cfg2.navigation = 2;
assertEquals(1, cfg1.compareTo(cfg2));

cfg1.keyboardHidden = 2;
cfg2.keyboardHidden = 3;
assertEquals(-1, cfg1.compareTo(cfg2));
//Synthetic comment -- @@ -231,6 +288,44 @@
| ActivityInfo.CONFIG_NAVIGATION
| ActivityInfo.CONFIG_ORIENTATION
| ActivityInfo.CONFIG_UI_MODE, mConfigDefault, config);
config.fontScale = 2;
doConfigCompare(ActivityInfo.CONFIG_MCC
| ActivityInfo.CONFIG_MNC
//Synthetic comment -- @@ -242,6 +337,8 @@
| ActivityInfo.CONFIG_NAVIGATION
| ActivityInfo.CONFIG_ORIENTATION
| ActivityInfo.CONFIG_UI_MODE
| ActivityInfo.CONFIG_FONT_SCALE, mConfigDefault, config);
}

//Synthetic comment -- @@ -294,6 +391,10 @@
config.navigationHidden = Configuration.NAVIGATIONHIDDEN_UNDEFINED;
config.orientation = Configuration.ORIENTATION_PORTRAIT;
config.screenLayout = Configuration.SCREENLAYOUT_LONG_UNDEFINED;
return config;
}








