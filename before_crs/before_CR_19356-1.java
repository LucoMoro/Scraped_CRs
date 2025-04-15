/*Remove Settings_SecureTest#testSecureSettings

Bug 3188260

This test tries to modify secure settings, which it will never be able
to do. There is already a test that checks whether reading is possible,
so delete this duplicate.

Change-Id:I758bc958979823fb280eec7c5fe53fddb8b5b7f1*/
//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/Settings_SecureTest.java b/tests/tests/provider/src/android/provider/cts/Settings_SecureTest.java
//Synthetic comment -- index 7cd586a..fdf7aa7 100644

//Synthetic comment -- @@ -16,17 +16,14 @@

package android.provider.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.test.AndroidTestCase;

@TestTargetClass(android.provider.Settings.Secure.class)
//Synthetic comment -- @@ -44,125 +41,6 @@
@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,
            method = "putInt",
            args = {android.content.ContentResolver.class, java.lang.String.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "putLong",
            args = {android.content.ContentResolver.class, java.lang.String.class, long.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "putFloat",
            args = {android.content.ContentResolver.class, java.lang.String.class, float.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "putString",
            args = {android.content.ContentResolver.class, java.lang.String.class,
                    java.lang.String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getInt",
            args = {android.content.ContentResolver.class, java.lang.String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getLong",
            args = {android.content.ContentResolver.class, java.lang.String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getFloat",
            args = {android.content.ContentResolver.class, java.lang.String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getString",
            args = {android.content.ContentResolver.class, java.lang.String.class}
        )
    })
    @BrokenTest("Cannot access secure settings table")
    public void testSecureSettings() throws SettingNotFoundException {
        /**
         * first query the exist settings in Secure table, and then insert four rows:
         * an int, a long, a float and a String. Get these four rows to check whether
         * insert success and then delete these four rows. At last backup an exist row,
         * update it, then check whether update success.
         */
        // first query exist rows
        Cursor c = cr.query(Secure.CONTENT_URI, null, null, null, null);
        try {
            assertNotNull(c);
            int count = c.getCount();
            c.close();

            // insert four rows
            assertTrue(Secure.putInt(cr, "IntField", 10));
            assertTrue(Secure.putLong(cr, "LongField", 20));
            assertTrue(Secure.putFloat(cr, "FloatField", 30));
            assertTrue(Secure.putString(cr, "StringField", "cts"));

            c = cr.query(Secure.CONTENT_URI, null, null, null, null);
            assertNotNull(c);
            assertEquals(count + 4, c.getCount());
            c.close();

            // get these four rows
            assertEquals(10, Secure.getInt(cr, "IntField"));
            assertEquals(20, Secure.getLong(cr, "LongField"));
            assertEquals(30.0f, Secure.getFloat(cr, "FloatField"), 0.001);
            assertEquals("cts", Secure.getString(cr, "StringField"));

            // delete these rows
            String selection = Secure.NAME + "=\"" + "IntField" + "\"";
            cr.delete(Secure.CONTENT_URI, selection, null);

            selection = Secure.NAME + "=\"" + "LongField" + "\"";
            cr.delete(Secure.CONTENT_URI, selection, null);

            selection = Secure.NAME + "=\"" + "FloatField" + "\"";
            cr.delete(Secure.CONTENT_URI, selection, null);

            selection = Secure.NAME + "=\"" + "StringField" + "\"";
            cr.delete(Secure.CONTENT_URI, selection, null);

            c = cr.query(Secure.CONTENT_URI, null, null, null, null);
            assertNotNull(c);
            assertEquals(count, c.getCount());
            c.close();

            // update an exist row, backup the value first
            selection = "name=\""+ Secure.BLUETOOTH_ON + "\"";
            c = cr.query(Secure.CONTENT_URI, null, selection, null, null);
            assertNotNull(c);
            assertEquals(1, c.getCount());
            c.moveToFirst();
            String name = c.getString(c.getColumnIndexOrThrow(Secure.NAME));
            String store = Secure.getString(cr, name);
            c.close();

            // update this row and check
            assertTrue(Secure.putString(cr, name, "1"));
            assertEquals("1", Secure.getString(cr, name));

            c = cr.query(Secure.CONTENT_URI, null, null, null, null);
            assertNotNull(c);
            assertEquals(count, c.getCount()); // here means no row added, just update

            // restore the value
            assertTrue(Secure.putString(cr, name, store));
        } finally {
            // TODO should clean up more better
            c.close();
        }
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
method = "getInt",
args = {android.content.ContentResolver.class, java.lang.String.class, int.class}
),







