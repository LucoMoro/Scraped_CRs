/*Fix failed test case when running framework auto test.

The method RetryManager.setRetryCount was modified in
f186b07b463447707117e2b54fc02f7ed30d6e43, However the
corresponding test cases were not updated and now fail.

The reason for failing is that setRetryCount no longer resets
the variable mRetryForever as it used to, and therefore
isRetryNeeded() returns true instead of false failing the
testcases.

Change-Id:Icbf23ada8000fab93ee34087e125d9b12d7e7897*/
//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/TelephonyUtilsTest.java b/tests/telephonytests/src/com/android/internal/telephony/TelephonyUtilsTest.java
//Synthetic comment -- index 3757017..f61aef8 100644

//Synthetic comment -- @@ -55,8 +55,8 @@
assertEquals(0, rm.getRetryTimer());

rm.setRetryCount(2);
        assertFalse(rm.isRetryForever());
        assertFalse(rm.isRetryNeeded());
assertEquals(0, rm.getRetryCount());
assertEquals(0, rm.getRetryTimer());
}
//Synthetic comment -- @@ -203,7 +203,7 @@

rm.setRetryCount(1);
assertTrue(rm.isRetryNeeded());
        assertFalse(rm.isRetryForever());
assertEquals(1, rm.getRetryCount());
assertEquals(2000, rm.getRetryTimer());

//Synthetic comment -- @@ -212,7 +212,7 @@
assertTrue(rm.isRetryForever());
rm.resetRetryCount();
assertTrue(rm.isRetryNeeded());
        assertFalse(rm.isRetryForever());
assertEquals(0, rm.getRetryCount());
assertEquals(1000, rm.getRetryTimer());
}







