/*Harden tests for android.content.res.Configuration

Extend ConfigurationTest with all public fields in Configuration.

Change-Id:I3c9c876722a07ab369268867efeb457688a151a6*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/res/cts/ConfigurationTest.java b/tests/tests/content/src/android/content/res/cts/ConfigurationTest.java
//Synthetic comment -- index 70443b0..f8a074b 100644

//Synthetic comment -- @@ -40,11 +40,19 @@
mConfig.fontScale = 2;
mConfig.mcc = mConfig.mnc = 1;
mConfig.locale = Locale.getDefault();
        mConfig.screenLayout = Configuration.SCREENLAYOUT_LONG_NO
                | Configuration.SCREENLAYOUT_SIZE_NORMAL;
mConfig.touchscreen = Configuration.TOUCHSCREEN_NOTOUCH;
mConfig.keyboard = Configuration.KEYBOARD_NOKEYS;
mConfig.keyboardHidden = Configuration.KEYBOARDHIDDEN_NO;
        mConfig.hardKeyboardHidden = Configuration.HARDKEYBOARDHIDDEN_YES;
mConfig.navigation = Configuration.NAVIGATION_NONAV;
        mConfig.navigationHidden = Configuration.NAVIGATIONHIDDEN_YES;
mConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mConfig.uiMode = Configuration.UI_MODE_TYPE_NORMAL | Configuration.UI_MODE_NIGHT_NO;
        mConfig.screenWidthDp = 240;
        mConfig.screenHeightDp = 320;
        mConfig.smallestScreenWidthDp = 240;
}

public void testConstructor() {
//Synthetic comment -- @@ -57,6 +65,41 @@
final Configuration cfg2 = new Configuration();
assertEquals(0, cfg1.compareTo(cfg2));

        cfg1.smallestScreenWidthDp = 2;
        cfg2.smallestScreenWidthDp = 3;
        assertEquals(-1, cfg1.compareTo(cfg2));
        cfg1.smallestScreenWidthDp = 3;
        cfg2.smallestScreenWidthDp = 2;
        assertEquals(1, cfg1.compareTo(cfg2));

        cfg1.screenHeightDp = 2;
        cfg2.screenHeightDp = 3;
        assertEquals(-1, cfg1.compareTo(cfg2));
        cfg1.screenHeightDp = 3;
        cfg2.screenHeightDp = 2;
        assertEquals(1, cfg1.compareTo(cfg2));

        cfg1.screenWidthDp = 2;
        cfg2.screenWidthDp = 3;
        assertEquals(-1, cfg1.compareTo(cfg2));
        cfg1.screenWidthDp = 3;
        cfg2.screenWidthDp = 2;
        assertEquals(1, cfg1.compareTo(cfg2));

        cfg1.uiMode = 2;
        cfg2.uiMode = 3;
        assertEquals(-1, cfg1.compareTo(cfg2));
        cfg1.uiMode = 3;
        cfg2.uiMode = 2;
        assertEquals(1, cfg1.compareTo(cfg2));

        cfg1.screenLayout = 2;
        cfg2.screenLayout = 3;
        assertEquals(-1, cfg1.compareTo(cfg2));
        cfg1.screenLayout = 3;
        cfg2.screenLayout = 2;
        assertEquals(1, cfg1.compareTo(cfg2));

cfg1.orientation = 2;
cfg2.orientation = 3;
assertEquals(-1, cfg1.compareTo(cfg2));
//Synthetic comment -- @@ -64,6 +107,13 @@
cfg2.orientation = 2;
assertEquals(1, cfg1.compareTo(cfg2));

        cfg1.navigationHidden = 2;
        cfg2.navigationHidden = 3;
        assertEquals(-1, cfg1.compareTo(cfg2));
        cfg1.navigationHidden = 3;
        cfg2.navigationHidden = 2;
        assertEquals(1, cfg1.compareTo(cfg2));

cfg1.navigation = 2;
cfg2.navigation = 3;
assertEquals(-1, cfg1.compareTo(cfg2));
//Synthetic comment -- @@ -71,6 +121,13 @@
cfg2.navigation = 2;
assertEquals(1, cfg1.compareTo(cfg2));

        cfg1.hardKeyboardHidden = 2;
        cfg2.hardKeyboardHidden = 3;
        assertEquals(-1, cfg1.compareTo(cfg2));
        cfg1.hardKeyboardHidden = 3;
        cfg2.hardKeyboardHidden = 2;
        assertEquals(1, cfg1.compareTo(cfg2));

cfg1.keyboardHidden = 2;
cfg2.keyboardHidden = 3;
assertEquals(-1, cfg1.compareTo(cfg2));
//Synthetic comment -- @@ -231,6 +288,44 @@
| ActivityInfo.CONFIG_NAVIGATION
| ActivityInfo.CONFIG_ORIENTATION
| ActivityInfo.CONFIG_UI_MODE, mConfigDefault, config);
        config.screenWidthDp = 1;
        doConfigCompare(ActivityInfo.CONFIG_MCC
                | ActivityInfo.CONFIG_MNC
                | ActivityInfo.CONFIG_LOCALE
                | ActivityInfo.CONFIG_SCREEN_LAYOUT
                | ActivityInfo.CONFIG_TOUCHSCREEN
                | ActivityInfo.CONFIG_KEYBOARD
                | ActivityInfo.CONFIG_KEYBOARD_HIDDEN
                | ActivityInfo.CONFIG_NAVIGATION
                | ActivityInfo.CONFIG_ORIENTATION
                | ActivityInfo.CONFIG_UI_MODE
                | ActivityInfo.CONFIG_SCREEN_SIZE, mConfigDefault, config);
        config.screenWidthDp = 0;
        config.screenHeightDp = 1;
        doConfigCompare(ActivityInfo.CONFIG_MCC
                | ActivityInfo.CONFIG_MNC
                | ActivityInfo.CONFIG_LOCALE
                | ActivityInfo.CONFIG_SCREEN_LAYOUT
                | ActivityInfo.CONFIG_TOUCHSCREEN
                | ActivityInfo.CONFIG_KEYBOARD
                | ActivityInfo.CONFIG_KEYBOARD_HIDDEN
                | ActivityInfo.CONFIG_NAVIGATION
                | ActivityInfo.CONFIG_ORIENTATION
                | ActivityInfo.CONFIG_UI_MODE
                | ActivityInfo.CONFIG_SCREEN_SIZE, mConfigDefault, config);
        config.smallestScreenWidthDp = 1;
        doConfigCompare(ActivityInfo.CONFIG_MCC
                | ActivityInfo.CONFIG_MNC
                | ActivityInfo.CONFIG_LOCALE
                | ActivityInfo.CONFIG_SCREEN_LAYOUT
                | ActivityInfo.CONFIG_TOUCHSCREEN
                | ActivityInfo.CONFIG_KEYBOARD
                | ActivityInfo.CONFIG_KEYBOARD_HIDDEN
                | ActivityInfo.CONFIG_NAVIGATION
                | ActivityInfo.CONFIG_ORIENTATION
                | ActivityInfo.CONFIG_UI_MODE
                | ActivityInfo.CONFIG_SCREEN_SIZE
                | ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE, mConfigDefault, config);
config.fontScale = 2;
doConfigCompare(ActivityInfo.CONFIG_MCC
| ActivityInfo.CONFIG_MNC
//Synthetic comment -- @@ -242,6 +337,8 @@
| ActivityInfo.CONFIG_NAVIGATION
| ActivityInfo.CONFIG_ORIENTATION
| ActivityInfo.CONFIG_UI_MODE
                | ActivityInfo.CONFIG_SCREEN_SIZE
                | ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE
| ActivityInfo.CONFIG_FONT_SCALE, mConfigDefault, config);
}

//Synthetic comment -- @@ -294,6 +391,10 @@
config.navigationHidden = Configuration.NAVIGATIONHIDDEN_UNDEFINED;
config.orientation = Configuration.ORIENTATION_PORTRAIT;
config.screenLayout = Configuration.SCREENLAYOUT_LONG_UNDEFINED;
        config.uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED;
        config.screenWidthDp = Configuration.SCREEN_WIDTH_DP_UNDEFINED;
        config.screenHeightDp = Configuration.SCREEN_HEIGHT_DP_UNDEFINED;
        config.smallestScreenWidthDp = Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED;
return config;
}








