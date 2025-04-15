/*fix sms testcases for non-telephony devices

send/receive sms testing should be disabled for non-telephony devices.

Change-Id:I9e88d3443207eb8f8deb21b8222686be87125edb*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 3f75f94..deb2abe 100644

//Synthetic comment -- @@ -16,20 +16,22 @@

package android.telephony.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

import java.util.ArrayList;

/**
* Tests for {@link android.telephony.SmsManager}.
//Synthetic comment -- @@ -104,6 +106,10 @@
)
})
public void testSendMessages() throws InterruptedException {
        PackageManager packageManager = mContext.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            return;
        }

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);







