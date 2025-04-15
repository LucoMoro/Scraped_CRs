/*Add test to verify an uninstall of app wipes its data.

Change-Id:Icf574f786c655e004adac738135fe49da5a5489f*/
//Synthetic comment -- diff --git a/tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java b/tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java
//Synthetic comment -- index f528ee1..e34e3e8 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import junit.framework.Test;

import com.android.ddmlib.Log;
import com.android.ddmlib.testrunner.ITestRunListener;
import com.android.ddmlib.testrunner.RemoteAndroidTestRunner;
import com.android.ddmlib.testrunner.TestIdentifier;
//Synthetic comment -- @@ -48,6 +49,12 @@
// testAppFailAccessPrivateData constants
private static final String APP_WITH_DATA_APK = "CtsAppWithData.apk";
private static final String APP_WITH_DATA_PKG = "com.android.cts.appwithdata";
private static final String APP_ACCESS_DATA_APK = "CtsAppAccessData.apk";
private static final String APP_ACCESS_DATA_PKG = "com.android.cts.appaccessdata";

//Synthetic comment -- @@ -138,7 +145,8 @@
false);
assertNull("failed to install app with data", installResult);
// run appwithdata's tests to create private data
            assertTrue("failed to create app's private data", runDeviceTests(APP_WITH_DATA_PKG));

installResult = getDevice().installPackage(getTestAppFilePath(APP_ACCESS_DATA_APK),
false);
//Synthetic comment -- @@ -153,6 +161,37 @@
}

/**
* Test that an app cannot instrument another app that is signed with different certificate.
*/
public void testInstrumentationDiffCert() throws IOException {
//Synthetic comment -- @@ -227,7 +266,17 @@
* @return <code>true</code> if all tests passed.
*/
private boolean runDeviceTests(String pkgName) {
        CollectingTestRunListener listener = doRunTests(pkgName);
return listener.didAllTestsPass();
}

//Synthetic comment -- @@ -236,8 +285,12 @@
* @param pkgName Android application package for tests
* @return the {@link CollectingTestRunListener}
*/
    private CollectingTestRunListener doRunTests(String pkgName) {
RemoteAndroidTestRunner testRunner = new RemoteAndroidTestRunner(pkgName, getDevice());
CollectingTestRunListener listener = new CollectingTestRunListener();
testRunner.run(listener);
return listener;
//Synthetic comment -- @@ -254,7 +307,8 @@

public void testFailed(TestFailure status, TestIdentifier test,
String trace) {
            Log.w(LOG_TAG, String.format("%s#%s failed: %s", test.getClassName(),
test.getTestName(), trace));
mAllTestsPassed = false;
}
//Synthetic comment -- @@ -264,7 +318,8 @@
}

public void testRunFailed(String errorMessage) {
            Log.w(LOG_TAG, String.format("test run failed: %s", errorMessage));
mAllTestsPassed = false;
mTestRunErrorMessage = errorMessage;
}








//Synthetic comment -- diff --git a/tests/appsecurity-tests/test-apps/AppWithData/src/com/android/cts/appwithdata/CreatePrivateDataTest.java b/tests/appsecurity-tests/test-apps/AppWithData/src/com/android/cts/appwithdata/CreatePrivateDataTest.java
//Synthetic comment -- index de8cb78..d77a872 100644

//Synthetic comment -- @@ -44,5 +44,14 @@
Context.MODE_PRIVATE);
outputStream.write("file contents".getBytes());
outputStream.close();
}
}







