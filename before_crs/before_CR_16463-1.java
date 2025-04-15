/*Check TelephonyManager#getDeviceId Returns Null

TelephonyManager#getDeviceId should return null for non-telephony
devices. Add that assertion to the test. Leave the tests that
check that Wifi is supported on a non-telephony device though,
because at least one form of wireless data networking must be
supported...

Change-Id:If4752df120be28526d3f585d0896fb8a1e0bee81*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 9ecbf20..239b3e5 100644

//Synthetic comment -- @@ -293,6 +293,7 @@
break;

case TelephonyManager.PHONE_TYPE_NONE:
assertMacAddressReported();
break;








