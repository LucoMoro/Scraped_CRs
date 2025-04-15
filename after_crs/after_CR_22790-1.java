/*Make instrumentation test failure message more verbose.

Bug 3443053

Change-Id:I12ed20ac93db92ea5cf7dcb1984bc81be242ad9b*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index a986380..c7d7c79 100644

//Synthetic comment -- @@ -201,10 +201,11 @@
static final String NO_TEST_RESULTS_MSG = "No test results";

/** Error message supplied when a test start bundle is parsed, but not the test end bundle. */
    static final String INCOMPLETE_TEST_ERR_MSG_PREFIX = "Test failed to run to completion";
    static final String INCOMPLETE_TEST_ERR_MSG_POSTFIX = "Check device logcat for details";

/** Error message supplied when the test run is incomplete. */
    static final String INCOMPLETE_RUN_ERR_MSG_PREFIX = "Test run failed to complete";

/**
* Creates the InstrumentationResultParser.
//Synthetic comment -- @@ -311,7 +312,8 @@
mInstrumentationResultBundle.put(mCurrentKey, statusValue);
} else if (mCurrentKey.equals(StatusKeys.SHORTMSG)) {
// test run must have failed
                    handleTestRunFailed(String.format("Instrumentation run failed due to '%s'",
                            statusValue));
}
} else {
TestResult testInfo = getCurrentTestInfo();
//Synthetic comment -- @@ -532,7 +534,7 @@
*/
void handleTestRunFailed(String errorMsg) {
errorMsg = (errorMsg == null ? "Unknown error" : errorMsg);
        Log.i(LOG_TAG, String.format("test run failed: '%s'", errorMsg));
if (mLastTestResult != null &&
mLastTestResult.isComplete() &&
StatusCodes.START == mLastTestResult.mCode) {
//Synthetic comment -- @@ -543,7 +545,8 @@
mLastTestResult.mTestName);
for (ITestRunListener listener : mTestListeners) {
listener.testFailed(ITestRunListener.TestFailure.ERROR, testId,
                    String.format("%s. Reason: '%s'. %s", INCOMPLETE_TEST_ERR_MSG_PREFIX, errorMsg,
                            INCOMPLETE_TEST_ERR_MSG_POSTFIX));
listener.testEnded(testId, getAndResetTestMetrics());
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java
//Synthetic comment -- index 4c62041..0d2f8f6 100644

//Synthetic comment -- @@ -245,7 +245,10 @@
Log.w(LOG_TAG, String.format(
"ShellCommandUnresponsiveException %s when running tests %s on %s",
e.toString(), getPackageName(), mRemoteDevice.getSerialNumber()));
            mParser.handleTestRunFailed(String.format(
                    "Failed to receive adb shell test output within %d ms. " +
                    "Test may have timed out, or adb connection to device became unresponsive",
                    mMaxTimeToOutputResponse));
throw e;
} catch (TimeoutException e) {
Log.w(LOG_TAG, String.format(







