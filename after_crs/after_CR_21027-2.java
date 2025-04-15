/*Timing issue in CTS tc "testPermissionDiffCert"

The test case kills an activity that holds some URI permissions
and then immediately after tests that it's no longer possible
to access the URI. Sometimes the change of access isn't able
to reach through the system in time before the test case
checks that the access has been removed and the test case fail.
A delay of 20 ms in the test case seems to be sufficient for the
change of access to pass through properly. I set it to 1 second
to have some margin.

Change-Id:Ide52b47cae57cc5a0c44412d1c480ac0deca5f5a*/




//Synthetic comment -- diff --git a/tests/appsecurity-tests/test-apps/UsePermissionDiffCert/src/com/android/cts/usespermissiondiffcertapp/AccessPermissionWithDiffSigTest.java b/tests/appsecurity-tests/test-apps/UsePermissionDiffCert/src/com/android/cts/usespermissiondiffcertapp/AccessPermissionWithDiffSigTest.java
//Synthetic comment -- index 63a65e4..1723077 100644

//Synthetic comment -- @@ -408,13 +408,11 @@
// Dispose of activity.
ReceiveUriActivity.finishCurInstanceSync();

        synchronized (this) {
            Log.i("**", "******************************* WAITING!!!");
            try {
                wait(100);
            } catch (InterruptedException e) {
}
}

//Synthetic comment -- @@ -495,13 +493,11 @@
// Dispose of activity.
ReceiveUriActivity.finishCurInstanceSync();

        synchronized (this) {
            Log.i("**", "******************************* WAITING!!!");
            try {
                wait(100);
            } catch (InterruptedException e) {
}
}








