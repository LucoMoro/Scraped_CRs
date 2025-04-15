/*World Phone Support in testTelephonyFeatures

Bug 2990592

Some phones support both GSM and CDMA but
TelephonyManager#getPhoneType can only report one at a given time.
Thus, don't assert that the other telephony features are not set.

Change-Id:I469dad4d34ebcc084899a863673bacf49c79bdac*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java b/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java
//Synthetic comment -- index b865c75..c978919 100644

//Synthetic comment -- @@ -38,7 +38,6 @@

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -240,17 +239,19 @@
int phoneType = mTelephonyManager.getPhoneType();
switch (phoneType) {
case TelephonyManager.PHONE_TYPE_GSM:
                assertAvailable(PackageManager.FEATURE_TELEPHONY);
                assertAvailable(PackageManager.FEATURE_TELEPHONY_GSM);
break;

case TelephonyManager.PHONE_TYPE_CDMA:
                assertAvailable(PackageManager.FEATURE_TELEPHONY);
                assertAvailable(PackageManager.FEATURE_TELEPHONY_CDMA);
break;

case TelephonyManager.PHONE_TYPE_NONE:
                assertNotAvailable(PackageManager.FEATURE_TELEPHONY);
                assertNotAvailable(PackageManager.FEATURE_TELEPHONY_CDMA);
                assertNotAvailable(PackageManager.FEATURE_TELEPHONY_GSM);
break;

default:
//Synthetic comment -- @@ -258,40 +259,6 @@
}
}

public void testTouchScreenFeatures() {
ConfigurationInfo configInfo = mActivityManager.getDeviceConfigurationInfo();
if (configInfo.reqTouchScreen != Configuration.TOUCHSCREEN_NOTOUCH) {







