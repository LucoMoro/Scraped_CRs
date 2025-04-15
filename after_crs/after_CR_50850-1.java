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
//Synthetic comment -- index 3757017..7f75656 100644

//Synthetic comment -- @@ -55,8 +55,6 @@
assertEquals(0, rm.getRetryTimer());

rm.setRetryCount(2);
assertEquals(0, rm.getRetryCount());
assertEquals(0, rm.getRetryTimer());
}
//Synthetic comment -- @@ -203,7 +201,6 @@

rm.setRetryCount(1);
assertTrue(rm.isRetryNeeded());
assertEquals(1, rm.getRetryCount());
assertEquals(2000, rm.getRetryTimer());








