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

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.test.AndroidTestCase;

@TestTargetClass(android.provider.Settings.Secure.class)
//Synthetic comment -- @@ -44,125 +41,6 @@
@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,
method = "getInt",
args = {android.content.ContentResolver.class, java.lang.String.class, int.class}
),







