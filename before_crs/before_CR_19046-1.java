/*Fix SettingsTest#testSecureTable

Bug 3188260

Remove the portions of the test that were trying to insert
into the secure table and remove the BrokenTest annotation.

Change-Id:I4e19fd19cb2075100ef3e555a876209e8a05575a*/
//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/SettingsTest.java b/tests/tests/provider/src/android/provider/cts/SettingsTest.java
//Synthetic comment -- index e3ca242..0589f39 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package android.provider.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestTargetClass;

import android.content.ContentResolver;
//Synthetic comment -- @@ -169,50 +168,24 @@
}
}

    @BrokenTest("Cannot access secure settings table")
    public void testSecureTable() throws RemoteException {
final String[] SECURE_PROJECTION = new String[] {
Settings.Secure._ID, Settings.Secure.NAME, Settings.Secure.VALUE
};

        String insertName = "name_insert";
        String insertValue = "value_insert";

        // get provider
ContentResolver cr = mContext.getContentResolver();
IContentProvider provider = cr.acquireProvider(Settings.Secure.CONTENT_URI);

        // Test: insert
        ContentValues value = new ContentValues();
        value.put(Settings.Secure.NAME, insertName);
        value.put(Settings.Secure.VALUE, insertValue);

        provider.insert(Settings.Secure.CONTENT_URI, value);

Cursor cursor = null;
try {
cursor = provider.query(Settings.Secure.CONTENT_URI, SECURE_PROJECTION,
                    Settings.Secure.NAME + "=\"" + insertName + "\"", null, null);
assertNotNull(cursor);
            assertEquals(1, cursor.getCount());
} finally {
            // TODO should clean up more better
            if (cursor != null)
cursor.close();
        }

        try {
            provider.delete(Settings.Secure.CONTENT_URI,
                    Settings.Secure.NAME + "=\"" + insertName + "\"", null);

            cursor = provider.query(Settings.Secure.CONTENT_URI, SECURE_PROJECTION,
                    Settings.Secure.NAME + "=\"" + insertName + "\"", null, null);
            assertNotNull(cursor);
            assertEquals(0, cursor.getCount());
        } finally {
            // TODO should clean up more better
            if (cursor != null)
                cursor.close();
}
}








