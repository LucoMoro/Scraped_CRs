/*Huawei fix for receive message tests failing on Mobile Internet Devices, Personal Media Players and other non-phone devices

Change-Id:I3262b3878ce91ae369216286bc2f9baeb2ba8bc7*/




//Synthetic comment -- diff --git a/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java b/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java
//Synthetic comment -- index 7b14db7..f34e380 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -51,6 +52,11 @@
* Note: this test requires that the device under test reports a valid phone number
*/
public void testReceiveTextMessage() {
        PackageManager packageManager = mContext.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            return;
        }

// register our test receiver to receive SMSs. This won't throw a SecurityException,
// so test needs to wait to determine if it actual receives an SMS
// admittedly, this is a weak verification







