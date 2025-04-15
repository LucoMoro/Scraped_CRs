/*Fix ConfigurationTest#writeToParcelTest

Bug 2455171

Update ConfigurationTest's writeToParcelTest that has fallen out
of date with changes to Configuration. It still passes on the
emulator, because it was testing the defaults which are all zeros.
Change the test case a bit to test alternating zeros and non-zero
constants to make it fail when things get out of date. Furthermore,
test null and non-null Locales too.

Change-Id:Idfbb8a200cf3e5f73209d3381a40c6bbeebb6102*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/res/cts/ConfigurationTest.java b/tests/tests/content/src/android/content/res/cts/ConfigurationTest.java
//Synthetic comment -- index ea78f91..e986c5c 100644

//Synthetic comment -- @@ -262,29 +262,53 @@
args = {android.os.Parcel.class, int.class}
)
public void testWriteToParcel() {
        assertWriteToParcel(createConfig(null), Parcel.obtain());
        assertWriteToParcel(createConfig(Locale.JAPAN), Parcel.obtain());
    }

    private Configuration createConfig(Locale locale) {
        Configuration config = new Configuration();
        config.fontScale = 13.37f;
        config.mcc = 0;
        config.mnc = 1;
        config.locale = locale;
        config.touchscreen = Configuration.TOUCHSCREEN_STYLUS;
        config.keyboard = Configuration.KEYBOARD_UNDEFINED;
        config.keyboardHidden = Configuration.KEYBOARDHIDDEN_YES;
        config.hardKeyboardHidden = Configuration.KEYBOARDHIDDEN_UNDEFINED;
        config.navigation = Configuration.NAVIGATION_DPAD;
        config.navigationHidden = Configuration.NAVIGATIONHIDDEN_UNDEFINED;
        config.orientation = Configuration.ORIENTATION_PORTRAIT;
        config.screenLayout = Configuration.SCREENLAYOUT_LONG_UNDEFINED;
        return config;
    }

    private void assertWriteToParcel(Configuration config, Parcel parcel) {
        config.writeToParcel(parcel, 0);
parcel.setDataPosition(0);
        assertEquals(config.fontScale, parcel.readFloat());
        assertEquals(config.mcc, parcel.readInt());
        assertEquals(config.mnc, parcel.readInt());
        if (config.locale == null) {
assertEquals(0, parcel.readInt());
} else {
assertEquals(1, parcel.readInt());
            assertEquals(config.locale.getLanguage(),
parcel.readString());
            assertEquals(config.locale.getCountry(),
parcel.readString());
            assertEquals(config.locale.getVariant(),
parcel.readString());
}
        parcel.readInt();
        assertEquals(config.touchscreen, parcel.readInt());
        assertEquals(config.keyboard, parcel.readInt());
        assertEquals(config.keyboardHidden, parcel.readInt());
        assertEquals(config.hardKeyboardHidden, parcel.readInt());
        assertEquals(config.navigation, parcel.readInt());
        assertEquals(config.navigationHidden, parcel.readInt());
        assertEquals(config.orientation, parcel.readInt());
        assertEquals(config.screenLayout, parcel.readInt());
}

}







