/*Fix CtsTelephonyTestCases:TelephonyManagerTest--testGetDeivceId fail for Wifi only
device that still return a non-null device id for GMS application "Shazam". Other
popular GMS application may also have the problem if the application check the device
id when running.

Change-Id:I9ac1cc2cfe5a3715e32c3714cd75dfff7c67dc55*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 0528897..5d34f64 100644

//Synthetic comment -- @@ -297,7 +297,7 @@
break;

case TelephonyManager.PHONE_TYPE_NONE:
//                assertNull(deviceId);
assertHardwareId();
assertMacAddressReported();
break;







