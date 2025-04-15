/*Revert "Fix CtsTelephonyTestCases:TelephonyManagerTest--testGetDeivceId fail for Wifi only"

This reverts commit 89fc8e38678df8ff78cca92cf6c508e3deea7c01.*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 5d34f64..0528897 100644

//Synthetic comment -- @@ -297,7 +297,7 @@
break;

case TelephonyManager.PHONE_TYPE_NONE:
                assertNull(deviceId);
assertHardwareId();
assertMacAddressReported();
break;







