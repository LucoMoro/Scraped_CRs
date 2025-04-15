/*Make test runner parser detect runs with no tests.

Add logic to differeniate between an incomplete test run, and a test run which
completed successfully but has no tests.

Fixes bughttp://b.android.com/7830Change-Id:I54068de73e068faae6e34779b91665f6d1dc1a47*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/InstrumentationResultParser.java
//Synthetic comment -- index 2bc5710..ff49c44 100755

//Synthetic comment -- @@ -138,6 +138,9 @@
/** True if start of test has already been reported to listener. */
private boolean mTestStartReported = false;

    /** True if the completion of the test run has been detected. */
    private boolean mTestRunFinished = false;

/** True if test run failure has already been reported to listener. */
private boolean mTestRunFailReported = false;

//Synthetic comment -- @@ -226,6 +229,8 @@
line.startsWith(Prefixes.CODE)) {
// Previous status key-value has been collected. Store it.
submitCurrentKeyValue();
            // these codes signal the end of the instrumentation run
            mTestRunFinished = true;
// just ignore the remaining data on this line
} else if (line.startsWith(Prefixes.TIME_REPORT)) {
parseTime(line, Prefixes.TIME_REPORT.length());
//Synthetic comment -- @@ -470,7 +475,7 @@
@Override
public void done() {
super.done();
        if (!mTestRunFailReported && !mTestStartReported && !mTestRunFinished) {
// no results
handleTestRunFailed(NO_TEST_RESULTS_MSG);
} else if (!mTestRunFailReported && mNumTestsExpected > mNumTestsRun) {
//Synthetic comment -- @@ -480,6 +485,11 @@
handleTestRunFailed(message);
} else {
for (ITestRunListener listener : mTestListeners) {
                if (!mTestStartReported) {
                    // test run wasn't started, but it finished successfully. Must be a run with
                    // no tests
                    listener.testRunStarted(0);
                }
listener.testRunEnded(mTestTime);
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/InstrumentationResultParserTest.java
//Synthetic comment -- index ee227f0..ed3a420 100644

//Synthetic comment -- @@ -170,17 +170,12 @@
*/
public void testRunAmFailed() {
StringBuilder output = new StringBuilder();
        addLine(output, "usage: am [subcommand] [options]");
        addLine(output, "start an Activity: am start [-D] [-W] <INTENT>");
        addLine(output, "-D: enable debugging");
        addLine(output, "-W: wait for launch to complete");
        addLine(output, "start a Service: am startservice <INTENT>");
        addLine(output, "Error: Bad component name: wfsdafddfasasdf");

injectTestString(output.toString());

//Synthetic comment -- @@ -189,6 +184,27 @@
}

/**
     * Test parsing of a test run that has no tests.
     * <p/>
     * Expect run to be reported as success.
     */
    public void testRunNoResults() {
        StringBuilder output = new StringBuilder();
        addLine(output, "INSTRUMENTATION_RESULT: stream=");
        addLine(output, "Test results for InstrumentationTestRunner=");
        addLine(output, "Time: 0.001");
        addLine(output, "OK (0 tests)");
        addLine(output, "INSTRUMENTATION_CODE: -1");

        injectTestString(output.toString());

        assertEquals(0, mTestResult.mTestCount);
        assertNull(mTestResult.mRunFailedMessage);
        assertEquals(1, mTestResult.mTestTime);
        assertFalse(mTestResult.mStopped);
    }

    /**
* Builds a common test result using TEST_NAME and TEST_CLASS.
*/
private StringBuilder buildCommonResult() {
//Synthetic comment -- @@ -218,7 +234,6 @@
*/
private void addStackTrace(StringBuilder output) {
addStatusKey(output, "stack", STACK_TRACE);
}

/**
//Synthetic comment -- @@ -234,6 +249,14 @@
}

/**
     * Append a line to output.
     */
    private void addLine(StringBuilder outputBuilder, String lineContent) {
        outputBuilder.append(lineContent);
        addLineBreak(outputBuilder);
    }

    /**
* Append line break characters to output
*/
private void addLineBreak(StringBuilder outputBuilder) {







