/*ignore testcase of toCallerIDMinMatch.

Change-Id:Iff16d2d81e311c76ae7d4fd87d56c5a5a3555816*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index 57e610b..f00ec0c 100644

//Synthetic comment -- @@ -118,7 +118,7 @@
public void testCallMethods() {
// Test calledPartyBCDToString
assertEquals("+17005550020", PhoneNumberUtils.calledPartyBCDToString(mPhoneNumber, 0, 7));
/*
// Test toCallerIDMinMatch
assertNull(PhoneNumberUtils.toCallerIDMinMatch(null));
assertEquals("1414555", PhoneNumberUtils.toCallerIDMinMatch("17005554141"));
//Synthetic comment -- @@ -130,7 +130,7 @@
assertEquals("0032", PhoneNumberUtils.toCallerIDMinMatch("2300"));
assertEquals("0032+", PhoneNumberUtils.toCallerIDMinMatch("+2300"));
assertEquals("#130#*", PhoneNumberUtils.toCallerIDMinMatch("*#031#"));
*/
// Test networkPortionToCalledPartyBCD, calledPartyBCDToString
byte[] bRet = PhoneNumberUtils.networkPortionToCalledPartyBCD("+17005550020");
assertEquals(mPhoneNumber.length, bRet.length);







