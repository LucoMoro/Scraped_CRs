/*Fix instrumentation repetition parsing and multi listener handling.

Change-Id:Ib295bb21c45dd2f52f547f86a5b3c4aaa959c139*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index 908c56b..7e2e52d 100644

//Synthetic comment -- @@ -97,6 +97,7 @@
private static final int START = 1;
private static final int ERROR = -1;
private static final int OK = 0;
}

/** Prefixes used to identify output. */
//Synthetic comment -- @@ -399,15 +400,17 @@
private void parseStatusCode(String line) {
String value = line.substring(Prefixes.STATUS_CODE.length()).trim();
TestResult testInfo = getCurrentTestInfo();
try {
testInfo.mCode = Integer.parseInt(value);
} catch (NumberFormatException e) {
Log.e(LOG_TAG, "Expected integer status code, received: " + value);
}

        // this means we're done with current test result bundle
        reportResult(testInfo);
        clearCurrentTestInfo();
}

/**
//Synthetic comment -- @@ -415,6 +418,7 @@
*
* @see IShellOutputReceiver#isCancelled()
*/
public boolean isCancelled() {
return mIsCancelled;
}
//Synthetic comment -- @@ -439,6 +443,7 @@
}
reportTestRunStarted(testInfo);
TestIdentifier testId = new TestIdentifier(testInfo.mTestClass, testInfo.mTestName);

switch (testInfo.mCode) {
case StatusCodes.START:
//Synthetic comment -- @@ -447,32 +452,36 @@
}
break;
case StatusCodes.FAILURE:
for (ITestRunListener listener : mTestListeners) {
listener.testFailed(ITestRunListener.TestFailure.FAILURE, testId,
getTrace(testInfo));

                    listener.testEnded(testId, getAndResetTestMetrics());
}
mNumTestsRun++;
break;
case StatusCodes.ERROR:
for (ITestRunListener listener : mTestListeners) {
listener.testFailed(ITestRunListener.TestFailure.ERROR, testId,
getTrace(testInfo));
                    listener.testEnded(testId, getAndResetTestMetrics());
}
mNumTestsRun++;
break;
case StatusCodes.OK:
for (ITestRunListener listener : mTestListeners) {
                    listener.testEnded(testId, getAndResetTestMetrics());
}
mNumTestsRun++;
break;
default:
Log.e(LOG_TAG, "Unknown status code received: " + testInfo.mCode);
for (ITestRunListener listener : mTestListeners) {
                    listener.testEnded(testId, getAndResetTestMetrics());
}
mNumTestsRun++;
break;








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index d8a15cc..650e79c 100644

//Synthetic comment -- @@ -123,6 +123,41 @@
}

/**
* Test parsing output for a test failure.
*/
public void testParse_testFailed() {







