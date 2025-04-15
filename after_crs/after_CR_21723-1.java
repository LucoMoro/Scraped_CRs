/*Add test to verify an uninstall of app wipes its data.

Change-Id:Icf574f786c655e004adac738135fe49da5a5489f*/




//Synthetic comment -- diff --git a/tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java b/tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java
//Synthetic comment -- index f528ee1..78a13d7 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import junit.framework.Test;

import com.android.ddmlib.Log;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.testrunner.ITestRunListener;
import com.android.ddmlib.testrunner.RemoteAndroidTestRunner;
import com.android.ddmlib.testrunner.TestIdentifier;
//Synthetic comment -- @@ -48,6 +49,12 @@
// testAppFailAccessPrivateData constants
private static final String APP_WITH_DATA_APK = "CtsAppWithData.apk";
private static final String APP_WITH_DATA_PKG = "com.android.cts.appwithdata";
    private static final String APP_WITH_DATA_CLASS = 
            "com.android.cts.appwithdata.CreatePrivateDataTest";
    private static final String APP_WITH_DATA_CREATE_METHOD = 
            "testCreatePrivateData";
    private static final String APP_WITH_DATA_CHECK_NOEXIST_METHOD = 
            "testEnsurePrivateDataNotExist";
private static final String APP_ACCESS_DATA_APK = "CtsAppAccessData.apk";
private static final String APP_ACCESS_DATA_PKG = "com.android.cts.appaccessdata";

//Synthetic comment -- @@ -125,30 +132,33 @@
}

/**
     * Test that uninstall of an app removes its private data.
*/
    public void testUninstallRemovesData() throws IOException {
        Log.i(LOG_TAG, "Uninstalling app, verifying data is removed.");
try {
// cleanup test app that might be installed from previous partial test run
getDevice().uninstallPackage(APP_WITH_DATA_PKG);

String installResult = getDevice().installPackage(getTestAppFilePath(APP_WITH_DATA_APK),
false);
assertNull("failed to install app with data", installResult);
// run appwithdata's tests to create private data
            assertTrue("failed to create app's private data", runDeviceTests(APP_WITH_DATA_PKG,
            		APP_WITH_DATA_CLASS, APP_WITH_DATA_CREATE_METHOD));
            
            getDevice().uninstallPackage(APP_WITH_DATA_PKG);

            installResult = getDevice().installPackage(getTestAppFilePath(APP_WITH_DATA_APK),
false);
            assertNull("failed to install app with data second time", installResult);
            // run appwithdata's 'check if file exists' test 
            assertTrue("app's private data still exists after install", runDeviceTests(
                    APP_WITH_DATA_PKG, APP_WITH_DATA_CLASS, APP_WITH_DATA_CHECK_NOEXIST_METHOD));

}
finally {
getDevice().uninstallPackage(APP_WITH_DATA_PKG);
}
}

//Synthetic comment -- @@ -227,7 +237,17 @@
* @return <code>true</code> if all tests passed.
*/
private boolean runDeviceTests(String pkgName) {
    	return runDeviceTests(pkgName, null, null);
    }
    
    /**
     * Helper method that will the specified packages tests on device.
     *
     * @param pkgName Android application package for tests
     * @return <code>true</code> if all tests passed.
     */
    private boolean runDeviceTests(String pkgName, String testClassName, String testMethodName) {
        CollectingTestRunListener listener = doRunTests(pkgName, testClassName, testMethodName);
return listener.didAllTestsPass();
}

//Synthetic comment -- @@ -236,8 +256,12 @@
* @param pkgName Android application package for tests
* @return the {@link CollectingTestRunListener}
*/
    private CollectingTestRunListener doRunTests(String pkgName, String testClassName,
    		String testMethodName) {
RemoteAndroidTestRunner testRunner = new RemoteAndroidTestRunner(pkgName, getDevice());
        if (testClassName != null && testMethodName != null) {
            testRunner.setMethodName(testClassName, testMethodName);
        }    
CollectingTestRunListener listener = new CollectingTestRunListener();
testRunner.run(listener);
return listener;
//Synthetic comment -- @@ -254,7 +278,8 @@

public void testFailed(TestFailure status, TestIdentifier test,
String trace) {
            Log.logAndDisplay(LogLevel.WARN, LOG_TAG, String.format("%s#%s failed: %s",
                    test.getClassName(),
test.getTestName(), trace));
mAllTestsPassed = false;
}
//Synthetic comment -- @@ -264,7 +289,8 @@
}

public void testRunFailed(String errorMessage) {
            Log.logAndDisplay(LogLevel.WARN, LOG_TAG, String.format("test run failed: %s",
                    errorMessage));
mAllTestsPassed = false;
mTestRunErrorMessage = errorMessage;
}








//Synthetic comment -- diff --git a/tests/appsecurity-tests/test-apps/AppWithData/src/com/android/cts/appwithdata/CreatePrivateDataTest.java b/tests/appsecurity-tests/test-apps/AppWithData/src/com/android/cts/appwithdata/CreatePrivateDataTest.java
//Synthetic comment -- index de8cb78..d77a872 100644

//Synthetic comment -- @@ -44,5 +44,14 @@
Context.MODE_PRIVATE);
outputStream.write("file contents".getBytes());
outputStream.close();
        assertTrue(getContext().getFileStreamPath(PRIVATE_FILE_NAME).exists());
    }

    /**
     * Check to ensure the private file created in testCreatePrivateData does not exist.
     * Used to check that uninstall of an app deletes the app's data.
     */
    public void testEnsurePrivateDataNotExist() throws IOException {
        assertFalse(getContext().getFileStreamPath(PRIVATE_FILE_NAME).exists());
}
}







