/*Suppress WebViewTest#testAccessCertificate

Bug 2841873

Change-Id:If1271b38af5e1ed6e25421debefdef2e91f5e553*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index 0a0ef62..eb88177 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package android.webkit.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.KnownFailure;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -33,11 +34,11 @@
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.AttributeSet;
//Synthetic comment -- @@ -1628,6 +1629,7 @@
args = {}
)
})
    @KnownFailure("Bug 2841873 - Always fails on certain devices and flakey on others.")
public void testAccessCertificate() throws Throwable {
runTestOnUiThread(new Runnable() {
public void run() {







