/*Huawei CTS fix for testing the international number format with the + sign in the middle of the string

Change-Id:Ic534d5fa87f49a1a957c8b5e62749bc37e3c2d5a*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index dad3dd6..d12f989 100644

//Synthetic comment -- @@ -63,8 +63,8 @@
PhoneNumberUtils.extractNetworkPortion("+1 (700).555-4141*#N"));
assertEquals("170055541", PhoneNumberUtils.extractNetworkPortion(
String.format("1 (700).555-41%c1234", PhoneNumberUtils.PAUSE)));
        assertEquals("**21**17005554141#",
                PhoneNumberUtils.extractNetworkPortion("**21**+17005554141#"));

// Test extractPostDialPortion
assertNull(PhoneNumberUtils.extractPostDialPortion(null));







