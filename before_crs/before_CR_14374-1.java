/*Fix RemoteAndroidTestRunnerTest.

Change the test device mock to return a String for getSerialNumber.

Change-Id:I83647927160c666330ddfb5394afe837577422d7*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java
//Synthetic comment -- index f9af29a..4727153 100644

//Synthetic comment -- @@ -177,7 +177,7 @@
}

public String getSerialNumber() {
            throw new UnsupportedOperationException();
}

public DeviceState getState() {







