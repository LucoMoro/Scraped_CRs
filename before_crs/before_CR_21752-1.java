/*Check "Unknown Sources" is Enabled by Default

Bug 4080822

Change-Id:Idba3b1871650eb7dea1e96e630e8ac15f4f03ec1*/
//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/Settings_SecureTest.java b/tests/tests/provider/src/android/provider/cts/Settings_SecureTest.java
//Synthetic comment -- index 6ce4157..9f5d8bb 100644

//Synthetic comment -- @@ -23,6 +23,7 @@

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -178,4 +179,13 @@
assertNotNull(uri);
assertEquals(Uri.withAppendedPath(Secure.CONTENT_URI, name), uri);
}
}







