/*Merge remote branch 'korg/froyo' into manualmerge

Conflicts:
	tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java
	tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java

Change-Id:Ie74c678a6ae142d9861b6965a8ca89ff58ebfa83*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index b08e953..bd8c2604 100644

//Synthetic comment -- @@ -147,9 +147,12 @@
args = {java.lang.String.class, android.net.Uri.class}
)
public void testDialPhoneNumber() {
        Uri uri = Uri.parse("tel:(212)5551212");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        assertCanBeHandled(intent);
}

/**
//Synthetic comment -- @@ -161,8 +164,11 @@
args = {java.lang.String.class, android.net.Uri.class}
)
public void testDialVoicemail() {
        Uri uri = Uri.parse("voicemail:");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        assertCanBeHandled(intent);
}
}








//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioTrackTest.java b/tests/tests/media/src/android/media/cts/AudioTrackTest.java
//Synthetic comment -- index 12cfcad..4adc582 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
@TestTargetClass(AudioTrack.class)
public class AudioTrackTest extends AndroidTestCase {
private String TAG = "AudioTrackTest";
    private final long WAIT_MSEC = 100;
private final int OFFSET_DEFAULT = 0;
private final int OFFSET_NEGATIVE = -10;

//Synthetic comment -- @@ -2620,4 +2620,4 @@
}
}

}
\ No newline at end of file








//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/wifi/cts/WifiInfoTest.java b/tests/tests/net/src/android/net/wifi/cts/WifiInfoTest.java
//Synthetic comment -- index 42243c8..3b1a6c1 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.content.BroadcastReceiver;
import android.content.Context;
//Synthetic comment -- @@ -168,8 +167,6 @@
args = {}
)
})
    @ToBeFixed(bug="1871573", explanation="android.net.wifi.WifiInfo#getNetworkId() return -1 when"
        + " there is wifi connection")
public void testWifiInfoProperties() throws Exception {
// this test case should in Wifi environment
WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
//Synthetic comment -- @@ -189,6 +186,7 @@
Thread.sleep(DURATION);
wifiInfo = mWifiManager.getConnectionInfo();
assertEquals(-1, wifiInfo.getNetworkId());
}

}








//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java
//Synthetic comment -- index c73d31a..2d11883 100644

//Synthetic comment -- @@ -16,19 +16,14 @@

package android.permission.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestTargetClass;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.Suppress;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;

/**
* Verify the Activity related operations require specific permissions.
//Synthetic comment -- @@ -50,43 +45,18 @@
}

/**
     * Verify that adding window of different types in Window Manager requires permissions.
* <p>Requires Permission:
     *   {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW}.
*/
@UiThreadTest
@MediumTest
    @Suppress
    @BrokenTest("This test passes, but crashes the UI thread later on. See issues 1909470, 1910487")
    public void testSystemAlertWindow() {
        final int[] types = new int[] {
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
            };

        AlertDialog dialog = (AlertDialog) (mActivity.getDialog());
        // Use normal window type will success
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);
        dialog.show();

        // Test special window types which need to be check SYSTEM_ALERT_WINDOW
        // permission.
        for (int i = 0; i < types.length; i++) {
            dialog = (AlertDialog) (mActivity.getDialog());
            dialog.getWindow().setType(types[i]);
            try {
                dialog.show();
                // This throws an exception as expected, but only after already adding
                // a new view to the view hierarchy. This later results in a NullPointerException
                // when the activity gets destroyed. Since that crashes the UI thread and causes
                // test runs to abort, this test is currently excluded.
                fail("Add dialog to Window Manager did not throw BadTokenException as expected");
            } catch (BadTokenException e) {
                // Expected
            }
}
}









//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java
//Synthetic comment -- index 6e86967..88d5f1c 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package android.permission.cts;

import android.content.Intent;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
//Synthetic comment -- @@ -33,14 +34,17 @@
*/
@SmallTest
public void testActionCall() {
        Uri uri = Uri.parse("tel:123456");
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.startActivity(intent);
            fail("startActivity(Intent.ACTION_CALL) did not throw SecurityException as expected");
        } catch (SecurityException e) {
            // expected
}
}

//Synthetic comment -- @@ -51,17 +55,20 @@
*/
@SmallTest
public void testCallVoicemail() {
        try {
            //Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED,
            Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED",
                    Uri.fromParts("voicemail", "", null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            fail("startActivity(Intent.ACTION_CALL_PRIVILEGED) did not throw SecurityException as expected");
        } catch (SecurityException e) {
            // expected
}
    }

/**
* Verify that Intent.ACTION_CALL_PRIVILEGED requires permissions.
//Synthetic comment -- @@ -70,14 +77,19 @@
*/
@SmallTest
public void testCall911() {
        //Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED, Uri.parse("tel:911"));
        Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED", Uri.parse("tel:911"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.startActivity(intent);
            fail("startActivity(Intent.ACTION_CALL_PRIVILEGED) did not throw SecurityException as expected");
        } catch (SecurityException e) {
            // expected
}
}









//Synthetic comment -- diff --git a/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java b/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java
//Synthetic comment -- index 7b14db7..f34e380 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -51,6 +52,11 @@
* Note: this test requires that the device under test reports a valid phone number
*/
public void testReceiveTextMessage() {
// register our test receiver to receive SMSs. This won't throw a SecurityException,
// so test needs to wait to determine if it actual receives an SMS
// admittedly, this is a weak verification








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index a97aa96..3177d894 100644

//Synthetic comment -- @@ -16,23 +16,25 @@

package android.telephony.cts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.telephony.SmsManager;
import android.test.AndroidTestCase;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

/**
* Tests for {@link android.telephony.SmsManager}.
//Synthetic comment -- @@ -126,6 +128,10 @@
)
})
public void testSendMessages() throws InterruptedException {

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);








//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewClientTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewClientTest.java
//Synthetic comment -- index 49d68c6..0de14e7 100644

//Synthetic comment -- @@ -31,7 +31,8 @@
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@TestTargetClass(android.webkit.WebViewClient.class)
public class WebViewClientTest extends ActivityInstrumentationTestCase2<WebViewStubActivity> {
private static final long TEST_TIMEOUT = 5000;
//Synthetic comment -- @@ -47,6 +48,7 @@
protected void setUp() throws Exception {
super.setUp();
mWebView = getActivity().getWebView();
}

@Override







