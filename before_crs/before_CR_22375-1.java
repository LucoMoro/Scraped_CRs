/*Merge remote branch 'korg/froyo' into manualmerge*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContentResolverSyncTestCase.java b/tests/tests/content/src/android/content/cts/ContentResolverSyncTestCase.java
//Synthetic comment -- index 032f10a..ecad3e4 100644

//Synthetic comment -- @@ -211,12 +211,13 @@
/**
* Test if we can set and get the MasterSyncAutomatically switch
*/
    public void testGetAndSetMasterSyncAutomatically() {
ContentResolver.setMasterSyncAutomatically(true);
assertEquals(true, ContentResolver.getMasterSyncAutomatically());

ContentResolver.setMasterSyncAutomatically(false);
assertEquals(false, ContentResolver.getMasterSyncAutomatically());
}

/**








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index 57e610b..23891e0 100644

//Synthetic comment -- @@ -121,11 +121,11 @@

// Test toCallerIDMinMatch
assertNull(PhoneNumberUtils.toCallerIDMinMatch(null));
        assertEquals("1414555", PhoneNumberUtils.toCallerIDMinMatch("17005554141"));
        assertEquals("1414555", PhoneNumberUtils.toCallerIDMinMatch("1-700-555-4141"));
        assertEquals("1414555", PhoneNumberUtils.toCallerIDMinMatch("1-700-555-4141,1234"));
        assertEquals("1414555", PhoneNumberUtils.toCallerIDMinMatch("1-700-555-4141;1234"));
        assertEquals("NN14555", PhoneNumberUtils.toCallerIDMinMatch("1-700-555-41NN"));
assertEquals("", PhoneNumberUtils.toCallerIDMinMatch(""));
assertEquals("0032", PhoneNumberUtils.toCallerIDMinMatch("2300"));
assertEquals("0032+", PhoneNumberUtils.toCallerIDMinMatch("+2300"));







