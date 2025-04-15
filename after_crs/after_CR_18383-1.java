/*Change PhoneNumberUtils MIN_MATCH to match 6 digits.

Argentina and some other Latin American countries
use 6 digits phone numbers. This change extends the
correct number matching digits for these countries.

Change-Id:Id36982b5348d20584fdd01bf87fcaa9739f5311eSigned-off-by: David Sobreira <davidps.marques@lge.com>*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index 5e45e67..3a3dc66 100644

//Synthetic comment -- @@ -121,11 +121,11 @@

// Test toCallerIDMinMatch
assertNull(PhoneNumberUtils.toCallerIDMinMatch(null));
        assertEquals("141455", PhoneNumberUtils.toCallerIDMinMatch("17005554141"));
        assertEquals("141455", PhoneNumberUtils.toCallerIDMinMatch("1-700-555-4141"));
        assertEquals("141455", PhoneNumberUtils.toCallerIDMinMatch("1-700-555-4141,1234"));
        assertEquals("141455", PhoneNumberUtils.toCallerIDMinMatch("1-700-555-4141;1234"));
        assertEquals("NN1455", PhoneNumberUtils.toCallerIDMinMatch("1-700-555-41NN"));
assertEquals("", PhoneNumberUtils.toCallerIDMinMatch(""));
assertEquals("0032", PhoneNumberUtils.toCallerIDMinMatch("2300"));
assertEquals("0032+", PhoneNumberUtils.toCallerIDMinMatch("+2300"));







