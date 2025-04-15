/*Fix CTS test failure

CTS test case android.telephony.cts.TelephonyManagerTest#testGetDeviceId
was failed on CDMA+LTE device.
When phone is in CDMA+LTE, Android considers it to be PHONE_TYPE_CDMA
and in turn TelephonyManager.getDeviceId() will return MEID as device
ID. However, LTE is a 3GPP protocol, and reasonable to use IMEI for
device ID when phone is in LTE mode.
So, the fix is: added a check for LTE mode to use IMEI instead of
MEID.

Change-Id:I0b42555a9aafb79093218c4fc4b4ca0c06d2fb59*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 0055330..0a5e88e 100644

//Synthetic comment -- @@ -165,7 +165,12 @@
break;

case TelephonyManager.PHONE_TYPE_CDMA:
                assertCdmaDeviceId(deviceId);
break;

case TelephonyManager.PHONE_TYPE_NONE:







