/*Revert "Check "Unknown Sources" is Enabled by Default"

This reverts commit 594e9852cc4d34e966f2389c8be67aebb69fdc53.*/
//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/Settings_SecureTest.java b/tests/tests/provider/src/android/provider/cts/Settings_SecureTest.java
//Synthetic comment -- index c325abf..6ce4157 100644

//Synthetic comment -- @@ -23,7 +23,6 @@

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -179,9 +178,4 @@
assertNotNull(uri);
assertEquals(Uri.withAppendedPath(Secure.CONTENT_URI, name), uri);
}

    public void testUnknownSourcesOffByDefault() throws SettingNotFoundException {
        assertEquals("Device should not ship with 'Unknown Sources' enabled by default.",
                0, Secure.getInt(cr, Settings.Secure.INSTALL_NON_MARKET_APPS));
    }
}







