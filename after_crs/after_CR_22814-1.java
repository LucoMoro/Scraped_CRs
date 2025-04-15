/*remove assertNull(deviceId) for some Non-phone but with 3g connection ability type device.

Change-Id:I2ff2ed554f9c7068210140a4bfcbf5337a1e7fcf*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 6e8aa27..714e9bd 100644

//Synthetic comment -- @@ -296,7 +296,8 @@
break;

case TelephonyManager.PHONE_TYPE_NONE:
                //remove assertNull for some Non-phone but with 3g ability type device.
                //assertNull(deviceId);
assertSerialNumber();
assertMacAddressReported();
break;







