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
        final Parcel parcel = Parcel.obtain();
        mConfigDefault.writeToParcel(parcel, 0);

parcel.setDataPosition(0);
        assertEquals(mConfigDefault.fontScale, parcel.readFloat());
        assertEquals(mConfigDefault.mcc, parcel.readInt());
        assertEquals(mConfigDefault.mnc, parcel.readInt());
        if (mConfigDefault.locale == null) {
assertEquals(0, parcel.readInt());
} else {
assertEquals(1, parcel.readInt());
            assertEquals(mConfigDefault.locale.getLanguage(),
parcel.readString());
            assertEquals(mConfigDefault.locale.getCountry(),
parcel.readString());
            assertEquals(mConfigDefault.locale.getVariant(),
parcel.readString());
}
        assertEquals(mConfigDefault.touchscreen, parcel.readInt());
        assertEquals(mConfigDefault.keyboard, parcel.readInt());
        assertEquals(mConfigDefault.keyboardHidden, parcel.readInt());
        assertEquals(mConfigDefault.navigation, parcel.readInt());
        assertEquals(mConfigDefault.orientation, parcel.readInt());
}

}







