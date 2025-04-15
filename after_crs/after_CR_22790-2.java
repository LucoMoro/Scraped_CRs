/*Make instrumentation test failure message more verbose.

Bug 3443053

Change-Id:I12ed20ac93db92ea5cf7dcb1984bc81be242ad9b*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index a986380..908c56b 100644

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
                    handleTestRunFailed(String.format("Instrumentation run failed due to '%1$s'",
                            statusValue));
}
} else {
TestResult testInfo = getCurrentTestInfo();
//Synthetic comment -- @@ -520,10 +522,10 @@
float timeSeconds = Float.parseFloat(timeString);
mTestTime = (long) (timeSeconds * 1000);
} catch (NumberFormatException e) {
                Log.w(LOG_TAG, String.format("Unexpected time format %1$s", line));
}
} else {
            Log.w(LOG_TAG, String.format("Unexpected time format %1$s", line));
}
}

//Synthetic comment -- @@ -532,7 +534,7 @@
*/
void handleTestRunFailed(String errorMsg) {
errorMsg = (errorMsg == null ? "Unknown error" : errorMsg);
        Log.i(LOG_TAG, String.format("test run failed: '%1$s'", errorMsg));
if (mLastTestResult != null &&
mLastTestResult.isComplete() &&
StatusCodes.START == mLastTestResult.mCode) {
//Synthetic comment -- @@ -543,7 +545,8 @@
mLastTestResult.mTestName);
for (ITestRunListener listener : mTestListeners) {
listener.testFailed(ITestRunListener.TestFailure.ERROR, testId,
                    String.format("%1$s. Reason: '%2$s'. %3$s", INCOMPLETE_TEST_ERR_MSG_PREFIX,
                            errorMsg, INCOMPLETE_TEST_ERR_MSG_POSTFIX));
listener.testEnded(testId, getAndResetTestMetrics());
}
}
//Synthetic comment -- @@ -579,7 +582,7 @@
handleTestRunFailed(NO_TEST_RESULTS_MSG);
} else if (mNumTestsExpected > mNumTestsRun) {
final String message =
                String.format("%1$s. Expected %2$d tests, received %3$d",
INCOMPLETE_RUN_ERR_MSG_PREFIX, mNumTestsExpected, mNumTestsRun);
handleTestRunFailed(message);
} else {








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java
//Synthetic comment -- index 4c62041..d680509 100644

//Synthetic comment -- @@ -226,9 +226,9 @@
public void run(Collection<ITestRunListener> listeners)
throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException,
IOException {
        final String runCaseCommandStr = String.format("am instrument -w -r %1$s %2$s",
getArgsCommand(), getRunnerPath());
        Log.i(LOG_TAG, String.format("Running %1$s on %2$s", runCaseCommandStr,
mRemoteDevice.getSerialNumber()));
// TODO: allow run name to be configurable
mParser = new InstrumentationResultParser(mPackageName, listeners);
//Synthetic comment -- @@ -236,26 +236,29 @@
try {
mRemoteDevice.executeShellCommand(runCaseCommandStr, mParser, mMaxTimeToOutputResponse);
} catch (IOException e) {
            Log.w(LOG_TAG, String.format("IOException %1$s when running tests %2$s on %3$s",
e.toString(), getPackageName(), mRemoteDevice.getSerialNumber()));
// rely on parser to communicate results to listeners
mParser.handleTestRunFailed(e.toString());
throw e;
} catch (ShellCommandUnresponsiveException e) {
Log.w(LOG_TAG, String.format(
                    "ShellCommandUnresponsiveException %1$s when running tests %2$s on %3$s",
e.toString(), getPackageName(), mRemoteDevice.getSerialNumber()));
            mParser.handleTestRunFailed(String.format(
                    "Failed to receive adb shell test output within %1$d ms. " +
                    "Test may have timed out, or adb connection to device became unresponsive",
                    mMaxTimeToOutputResponse));
throw e;
} catch (TimeoutException e) {
Log.w(LOG_TAG, String.format(
                    "TimeoutException when running tests %1$s on %2$s", getPackageName(),
mRemoteDevice.getSerialNumber()));
mParser.handleTestRunFailed(e.toString());
throw e;
} catch (AdbCommandRejectedException e) {
Log.w(LOG_TAG, String.format(
                    "AdbCommandRejectedException %1$s when running tests %2$s on %3$s",
e.toString(), getPackageName(), mRemoteDevice.getSerialNumber()));
mParser.handleTestRunFailed(e.toString());
throw e;
//Synthetic comment -- @@ -279,7 +282,7 @@
private String getArgsCommand() {
StringBuilder commandBuilder = new StringBuilder();
for (Entry<String, String> argPair : mArgMap.entrySet()) {
            final String argCmd = String.format(" -e %1$s %2$s", argPair.getKey(),
argPair.getValue());
commandBuilder.append(argCmd);
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index 032b879..d8a15cc 100644

//Synthetic comment -- @@ -188,7 +188,7 @@
addLineBreak(output);

mMockListener.testRunStarted(RUN_NAME, 0);
        mMockListener.testRunFailed(EasyMock.contains(errorMessage));
mMockListener.testRunEnded(0, Collections.EMPTY_MAP);

injectAndVerifyTestString(output.toString());







