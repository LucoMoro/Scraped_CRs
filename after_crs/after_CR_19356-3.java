/*Fix Settings_SecureTest#testSecureSettings

Bug 3188260

This test tries to modify secure settings, which it will never be able
to do. Change the tests to check that the right exceptions are being
thrown.

Change-Id:I758bc958979823fb280eec7c5fe53fddb8b5b7f1*/




//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/Settings_SecureTest.java b/tests/tests/provider/src/android/provider/cts/Settings_SecureTest.java
//Synthetic comment -- index 7cd586a..6ce4157 100644

//Synthetic comment -- @@ -16,14 +16,12 @@

package android.provider.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
//Synthetic comment -- @@ -31,6 +29,15 @@

@TestTargetClass(android.provider.Settings.Secure.class)
public class Settings_SecureTest extends AndroidTestCase {

    private static final String NO_SUCH_SETTING = "NoSuchSetting";

    /**
     * Setting that will have a string value to trigger SettingNotFoundException caused by
     * NumberFormatExceptions for getInt, getFloat, and getLong.
     */
    private static final String STRING_VALUE_SETTING = Secure.ENABLED_ACCESSIBILITY_SERVICES;

private ContentResolver cr;

@Override
//Synthetic comment -- @@ -39,124 +46,19 @@

cr = mContext.getContentResolver();
assertNotNull(cr);
        assertSettingsForTests();
}

    /** Check that the settings that will be used for testing have proper values. */
    private void assertSettingsForTests() {
        assertNull(Secure.getString(cr, NO_SUCH_SETTING));

        String value = Secure.getString(cr, STRING_VALUE_SETTING);
        assertNotNull(value);
try {
            Integer.parseInt(value);
            fail("Shouldn't be able to parse this setting's value for later tests.");
        } catch (NumberFormatException expected) {
}
}

//Synthetic comment -- @@ -183,6 +85,87 @@
assertEquals(30.0f, Secure.getFloat(cr, "float", 30), 0.001);
}

    public void testGetPutInt() {
        assertNull(Secure.getString(cr, NO_SUCH_SETTING));

        try {
            Secure.putInt(cr, NO_SUCH_SETTING, -1);
            fail("SecurityException should have been thrown!");
        } catch (SecurityException expected) {
        }

        try {
            Secure.getInt(cr, NO_SUCH_SETTING);
            fail("SettingNotFoundException should have been thrown!");
        } catch (SettingNotFoundException expected) {
        }

        try {
            Secure.getInt(cr, STRING_VALUE_SETTING);
            fail("SettingNotFoundException should have been thrown!");
        } catch (SettingNotFoundException expected) {
        }
    }

    public void testGetPutFloat() throws SettingNotFoundException {
        assertNull(Secure.getString(cr, NO_SUCH_SETTING));

        try {
            Secure.putFloat(cr, NO_SUCH_SETTING, -1);
            fail("SecurityException should have been thrown!");
        } catch (SecurityException expected) {
        }

        // TODO: Should be fixed to throw SettingNotFoundException.
        try {
            Secure.getFloat(cr, NO_SUCH_SETTING);
            fail("NullPointerException should have been thrown!");
        } catch (NullPointerException expected) {
        }

        try {
            Secure.getFloat(cr, STRING_VALUE_SETTING);
            fail("SettingNotFoundException should have been thrown!");
        } catch (SettingNotFoundException expected) {
        }
    }

    public void testGetPutLong() {
        assertNull(Secure.getString(cr, NO_SUCH_SETTING));

        try {
            Secure.putLong(cr, NO_SUCH_SETTING, -1);
            fail("SecurityException should have been thrown!");
        } catch (SecurityException expected) {
        }

        try {
            Secure.getLong(cr, NO_SUCH_SETTING);
            fail("SettingNotFoundException should have been thrown!");
        } catch (SettingNotFoundException expected) {
        }

        try {
            Secure.getLong(cr, STRING_VALUE_SETTING);
            fail("SettingNotFoundException should have been thrown!");
        } catch (SettingNotFoundException expected) {
        }
    }

    public void testGetPutString() {
        assertNull(Secure.getString(cr, NO_SUCH_SETTING));

        try {
            Secure.putString(cr, NO_SUCH_SETTING, "-1");
            fail("SecurityException should have been thrown!");
        } catch (SecurityException expected) {
        }

        assertNotNull(Secure.getString(cr, STRING_VALUE_SETTING));

        assertNull(Secure.getString(cr, NO_SUCH_SETTING));
    }

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "getUriFor",







