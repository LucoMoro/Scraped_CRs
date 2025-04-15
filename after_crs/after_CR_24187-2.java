/*Fix instrumentation repetition parsing and multi listener handling.

Change-Id:Ib295bb21c45dd2f52f547f86a5b3c4aaa959c139*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index 908c56b..7e2e52d 100644

//Synthetic comment -- @@ -97,6 +97,7 @@
private static final int START = 1;
private static final int ERROR = -1;
private static final int OK = 0;
        private static final int IN_PROGRESS = 2;
}

/** Prefixes used to identify output. */
//Synthetic comment -- @@ -399,15 +400,17 @@
private void parseStatusCode(String line) {
String value = line.substring(Prefixes.STATUS_CODE.length()).trim();
TestResult testInfo = getCurrentTestInfo();
        testInfo.mCode = StatusCodes.ERROR;
try {
testInfo.mCode = Integer.parseInt(value);
} catch (NumberFormatException e) {
Log.e(LOG_TAG, "Expected integer status code, received: " + value);
}
        if (testInfo.mCode != StatusCodes.IN_PROGRESS) {
            // this means we're done with current test result bundle
            reportResult(testInfo);
            clearCurrentTestInfo();
        }
}

/**
//Synthetic comment -- @@ -415,6 +418,7 @@
*
* @see IShellOutputReceiver#isCancelled()
*/
    @Override
public boolean isCancelled() {
return mIsCancelled;
}
//Synthetic comment -- @@ -439,6 +443,7 @@
}
reportTestRunStarted(testInfo);
TestIdentifier testId = new TestIdentifier(testInfo.mTestClass, testInfo.mTestName);
        Map<String, String> metrics;

switch (testInfo.mCode) {
case StatusCodes.START:
//Synthetic comment -- @@ -447,32 +452,36 @@
}
break;
case StatusCodes.FAILURE:
                metrics = getAndResetTestMetrics();
for (ITestRunListener listener : mTestListeners) {
listener.testFailed(ITestRunListener.TestFailure.FAILURE, testId,
getTrace(testInfo));

                    listener.testEnded(testId, metrics);
}
mNumTestsRun++;
break;
case StatusCodes.ERROR:
                metrics = getAndResetTestMetrics();
for (ITestRunListener listener : mTestListeners) {
listener.testFailed(ITestRunListener.TestFailure.ERROR, testId,
getTrace(testInfo));
                    listener.testEnded(testId, metrics);
}
mNumTestsRun++;
break;
case StatusCodes.OK:
                metrics = getAndResetTestMetrics();
for (ITestRunListener listener : mTestListeners) {
                    listener.testEnded(testId, metrics);
}
mNumTestsRun++;
break;
default:
                metrics = getAndResetTestMetrics();
Log.e(LOG_TAG, "Unknown status code received: " + testInfo.mCode);
for (ITestRunListener listener : mTestListeners) {
                    listener.testEnded(testId, metrics);
}
mNumTestsRun++;
break;








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index d8a15cc..650e79c 100644

//Synthetic comment -- @@ -123,6 +123,41 @@
}

/**
     * Test parsing output for a test that produces repeated metrics values
     * <p/>
     * This mimics launch performance test output.
     */
    public void testParse_repeatedTestMetrics() {
        StringBuilder output = new StringBuilder();
        // add test start output
        addCommonStatus(output);
        addStartCode(output);

        addStatusKey(output, "currentiterations", "1");
        addStatusCode(output, "2");
        addStatusKey(output, "currentiterations", "2");
        addStatusCode(output, "2");
        addStatusKey(output, "currentiterations", "3");
        addStatusCode(output, "2");

        // add test end
        addCommonStatus(output);
        addStatusKey(output, "numiterations", "3");
        addSuccessCode(output);

        final Capture<Map<String, String>> captureMetrics = new Capture<Map<String, String>>();
        mMockListener.testRunStarted(RUN_NAME, 1);
        mMockListener.testStarted(TEST_ID);
        mMockListener.testEnded(EasyMock.eq(TEST_ID), EasyMock.capture(captureMetrics));
        mMockListener.testRunEnded(0, Collections.EMPTY_MAP);

        injectAndVerifyTestString(output.toString());

        assertEquals("3", captureMetrics.getValue().get("currentiterations"));
        assertEquals("3", captureMetrics.getValue().get("numiterations"));
    }

    /**
* Test parsing output for a test failure.
*/
public void testParse_testFailed() {







