/*Fix CTS fails for Wifi only device and the tablet that doesn't have phone application.
1.CtsPermission2TestCases:
android.permission2.cts.NoReceiveSmsPermissionTest¡XtestReceiveTextMessage
2.CtsPermission2TestCases:
android.permission2.cts.NoReceiveGsmSmsPermissionTest¡XtestReceiveTextMessage
3.CtsPermissionTestCases:
android.permission.cts.NoCallPermissionTest¡XtestCallVoicemail
4.CtsPermissionTestCases: android.permission.cts.NoCallPermissionTest-- testCall911
5.CtsTelephonyTestCases:
android.telephony.cts.TelephonyManagerTest¡XtestGetNetworkCountryIso
6.CtsTelephonyTestCases:
android.telephony.cts.TelephonyManagerTest¡XtestGetSimCountryIso
7.CtsTelephonyTestCases: android.telephony.cts.SmsManagerTest¡XtestSendMessages
8.CtsTelephonyTestCases: android.telephony.gsm.cts.SmsManagerTest¡XtestSendMessages

Change-Id:If2e7e52e779079a3a7db8c8d319f0caaadb975f7*/
//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java
//Synthetic comment -- index 6e86967..746f1ad 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
* Verify Phone calling related methods without specific Phone/Call permissions.
//Synthetic comment -- @@ -60,7 +61,9 @@
fail("startActivity(Intent.ACTION_CALL_PRIVILEGED) did not throw SecurityException as expected");
} catch (SecurityException e) {
// expected
        }
}

/**
//Synthetic comment -- @@ -78,7 +81,9 @@
fail("startActivity(Intent.ACTION_CALL_PRIVILEGED) did not throw SecurityException as expected");
} catch (SecurityException e) {
// expected
        }
}

}








//Synthetic comment -- diff --git a/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java b/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java
//Synthetic comment -- index 7b14db7..cae1059 100755

//Synthetic comment -- @@ -51,6 +51,10 @@
* Note: this test requires that the device under test reports a valid phone number
*/
public void testReceiveTextMessage() {
// register our test receiver to receive SMSs. This won't throw a SecurityException,
// so test needs to wait to determine if it actual receives an SMS
// admittedly, this is a weak verification








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 3f75f94..2b11e9d 100644

//Synthetic comment -- @@ -104,7 +104,8 @@
)
})
public void testSendMessages() throws InterruptedException {

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);









//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 0528897..09ff09a 100644

//Synthetic comment -- @@ -445,6 +445,8 @@
private static final String ISO_COUNTRY_CODE_PATTERN = "[a-z]{2}";

public void testGetNetworkCountryIso() {
PackageManager packageManager = getContext().getPackageManager();
String countryCode = mTelephonyManager.getNetworkCountryIso();
if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
//Synthetic comment -- @@ -457,6 +459,8 @@
}

public void testGetSimCountryIso() {
PackageManager packageManager = getContext().getPackageManager();
String countryCode = mTelephonyManager.getSimCountryIso();
if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {







