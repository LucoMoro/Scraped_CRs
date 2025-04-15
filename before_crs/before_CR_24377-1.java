/*For non-telephony devices which uses 3G network via GSM, getPhoneType() should be PHONE_TYPE_GSM, and getDeviceId should return null.

Also hasSYstemFeature(PackageManager.FEATURE_TELEPHONY) should be false.

In this test case, when hasSystemFeature(PackageManager.FEATURE_TELEPHONY) is set to false, designated test is not excuted.

So I made modification so that if getPhoneType() is other than PHONE_TYPE_NONE when hasSystemFeature(PackageManager.FEATURE_TELEPHONY) is false, this test is going to be excuted.*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java b/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java
//Synthetic comment -- index ac3d03a..dd52ab2 100644

//Synthetic comment -- @@ -237,6 +237,9 @@
*/
public void testTelephonyFeatures() {
int phoneType = mTelephonyManager.getPhoneType();
switch (phoneType) {
case TelephonyManager.PHONE_TYPE_GSM:
assertAvailable(PackageManager.FEATURE_TELEPHONY);








//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index bd8c2604..8f2a750 100644

//Synthetic comment -- @@ -21,10 +21,12 @@
import dalvik.annotation.TestTargetNew;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.List;
//Synthetic comment -- @@ -33,6 +35,14 @@
public class AvailableIntentsTest extends AndroidTestCase {
private static final String NORMAL_URL = "http://www.google.com/";
private static final String SECURE_URL = "https://www.google.com/";

/**
* Assert target intent can be handled by at least one Activity.
//Synthetic comment -- @@ -148,7 +158,8 @@
)
public void testDialPhoneNumber() {
PackageManager packageManager = mContext.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
Uri uri = Uri.parse("tel:(212)5551212");
Intent intent = new Intent(Intent.ACTION_DIAL, uri);
assertCanBeHandled(intent);
//Synthetic comment -- @@ -165,7 +176,8 @@
)
public void testDialVoicemail() {
PackageManager packageManager = mContext.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
Uri uri = Uri.parse("voicemail:");
Intent intent = new Intent(Intent.ACTION_DIAL, uri);
assertCanBeHandled(intent);








//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java b/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java
//Synthetic comment -- index 3b85e9f..568181c 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.test.AndroidTestCase;
import android.util.Log;

//Synthetic comment -- @@ -53,6 +54,7 @@
private PackageManager mPackageManager;
// must include both mobile data + wifi
private static final int MIN_NUM_NETWORK_TYPES = 2;

@Override
protected void setUp() throws Exception {
//Synthetic comment -- @@ -60,6 +62,8 @@
mCm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
mWifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
mPackageManager = getContext().getPackageManager();
}

@TestTargetNew(
//Synthetic comment -- @@ -256,6 +260,7 @@
/** Test that hipri can be brought up when Wifi is enabled. */
public void testStartUsingNetworkFeature_enableHipri() throws Exception {
if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
|| !mPackageManager.hasSystemFeature(PackageManager.FEATURE_WIFI)) {
// This test requires a mobile data connection and WiFi.
return;








//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java
//Synthetic comment -- index 88d5f1c..92e7716 100644

//Synthetic comment -- @@ -16,9 +16,11 @@

package android.permission.cts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

//Synthetic comment -- @@ -26,6 +28,14 @@
* Verify Phone calling related methods without specific Phone/Call permissions.
*/
public class NoCallPermissionTest extends AndroidTestCase {

/**
* Verify that Intent.ACTION_CALL requires permissions.
//Synthetic comment -- @@ -35,7 +45,8 @@
@SmallTest
public void testActionCall() {
PackageManager packageManager = getContext().getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
Uri uri = Uri.parse("tel:123456");
Intent intent = new Intent(Intent.ACTION_CALL, uri);
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//Synthetic comment -- @@ -56,7 +67,8 @@
@SmallTest
public void testCallVoicemail() {
PackageManager packageManager = getContext().getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
try {
//Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED,
Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED",
//Synthetic comment -- @@ -78,7 +90,8 @@
@SmallTest
public void testCall911() {
PackageManager packageManager = getContext().getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
//Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED, Uri.parse("tel:911"));
Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED",
Uri.parse("tel:911"));








//Synthetic comment -- diff --git a/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java b/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java
//Synthetic comment -- index f34e380..caef4d53 100755

//Synthetic comment -- @@ -43,6 +43,14 @@
"com.android.cts.permission.sms.MESSAGE_SENT";

private static final String LOG_TAG = "NoReceiveSmsPermissionTest";

/**
* Verify that SmsManager.sendTextMessage requires permissions.
//Synthetic comment -- @@ -53,7 +61,8 @@
*/
public void testReceiveTextMessage() {
PackageManager packageManager = mContext.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
return;
}









//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index c201fb7..fc0aea9 100644

//Synthetic comment -- @@ -121,7 +121,8 @@
})
public void testSendMessages() throws InterruptedException {
PackageManager packageManager = mContext.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
return;
}









//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 0528897..6c168934 100644

//Synthetic comment -- @@ -287,6 +287,9 @@
public void testGetDeviceId() {
String deviceId = mTelephonyManager.getDeviceId();
int phoneType = mTelephonyManager.getPhoneType();
switch (phoneType) {
case TelephonyManager.PHONE_TYPE_GSM:
assertImeiDeviceId(deviceId);
//Synthetic comment -- @@ -447,7 +450,8 @@
public void testGetNetworkCountryIso() {
PackageManager packageManager = getContext().getPackageManager();
String countryCode = mTelephonyManager.getNetworkCountryIso();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
assertTrue("Country code '" + countryCode + "' did not match "
+ ISO_COUNTRY_CODE_PATTERN,
Pattern.matches(ISO_COUNTRY_CODE_PATTERN, countryCode));
//Synthetic comment -- @@ -459,7 +463,8 @@
public void testGetSimCountryIso() {
PackageManager packageManager = getContext().getPackageManager();
String countryCode = mTelephonyManager.getSimCountryIso();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
assertTrue("Country code '" + countryCode + "' did not match "
+ ISO_COUNTRY_CODE_PATTERN,
Pattern.matches(ISO_COUNTRY_CODE_PATTERN, countryCode));







