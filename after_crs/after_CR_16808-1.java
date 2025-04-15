/*Store and report metrics emitted during each test case

Change-Id:Ia7e7f6bef73e7ecd30b03973fd966d28ac90464b*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/ITestRunListener.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/ITestRunListener.java
//Synthetic comment -- index 5bcc836..40f89fe 100644

//Synthetic comment -- @@ -65,11 +65,13 @@

/**
* Reports the execution end of an individual test case.
     * If {@link #testFailed} was not invoked, this test passed.  Also returns any key/value
     * metrics which may have been emitted during the test case's execution.
* 
* @param test identifies the test
     * @param testMetrics a {@link Map} of the metrics emitted
*/
    public void testEnded(TestIdentifier test, Map<String, String> testMetrics);

/**
* Reports the failure of a individual test case.








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index c3399f0..04c912b 100755

//Synthetic comment -- @@ -167,6 +167,12 @@
*/
private Map<String, String> mInstrumentationResultBundle = new HashMap<String, String>();

    /**
     * Stores key-value pairs of metrics emitted during the execution of each test case.  Note that
     * standard keys that are stored in the TestResults class are filtered out of this Map.
     */
    private Map<String, String> mTestMetrics = new HashMap<String, String>();

private static final String LOG_TAG = "InstrumentationResultParser";

/** Error message supplied when no parseable test results are received from test run. */
//Synthetic comment -- @@ -261,12 +267,12 @@
}

/**
     * Stores the currently parsed key-value pair in the appropriate place.
*/
private void submitCurrentKeyValue() {
if (mCurrentKey != null && mCurrentValue != null) {
            String statusValue = mCurrentValue.toString();
if (mInInstrumentationResultKey) {
mInstrumentationResultBundle.put(mCurrentKey, statusValue);
if (mCurrentKey.equals(StatusKeys.SHORTMSG)) {
// test run must have failed
//Synthetic comment -- @@ -274,7 +280,6 @@
}
} else {
TestResult testInfo = getCurrentTestInfo();

if (mCurrentKey.equals(StatusKeys.CLASS)) {
testInfo.mTestClass = statusValue.trim();
//Synthetic comment -- @@ -292,6 +297,9 @@
handleTestRunFailed(statusValue);
} else if (mCurrentKey.equals(StatusKeys.STACK)) {
testInfo.mStackTrace = statusValue;
                } else {
                    // Not one of the recognized key/value pairs, so dump it in mTestMetrics
                    mTestMetrics.put(mCurrentKey, statusValue);
}
}

//Synthetic comment -- @@ -300,6 +308,16 @@
}
}

    /**
     * A utility method to return the test metrics from the current test case execution and get
     * ready for the next one.
     */
    private Map<String, String> getAndResetTestMetrics() {
        Map<String, String> retVal = mTestMetrics;
        mTestMetrics = new HashMap<String, String>();
        return retVal;
    }

private TestResult getCurrentTestInfo() {
if (mCurrentTestResult == null) {
mCurrentTestResult = new TestResult();
//Synthetic comment -- @@ -396,7 +414,7 @@
listener.testFailed(ITestRunListener.TestFailure.FAILURE, testId,
getTrace(testInfo));

                    listener.testEnded(testId, getAndResetTestMetrics());
}
mNumTestsRun++;
break;
//Synthetic comment -- @@ -404,20 +422,20 @@
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
//Synthetic comment -- @@ -484,7 +502,7 @@
for (ITestRunListener listener : mTestListeners) {
listener.testFailed(ITestRunListener.TestFailure.ERROR, testId,
String.format("Incomplete: %s", errorMsg));
                listener.testEnded(testId, getAndResetTestMetrics());
}
}
for (ITestRunListener listener : mTestListeners) {







